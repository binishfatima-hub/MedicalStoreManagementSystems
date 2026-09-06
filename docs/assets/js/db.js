/* ============================================================
   db.js  -  the MediCare database, running inside the browser.

   HOW THIS WORKS

   sql.js is the real SQLite engine, compiled so it can run in a
   browser. So this is genuine SQLite with genuine SQL - the same
   CREATE TABLE, INSERT, SELECT and UPDATE statements a normal
   database uses. There is simply no separate server.

   WHERE THE DATA LIVES

   The whole database is one block of bytes. After every change it
   is saved into IndexedDB, which is storage the browser keeps on
   the computer. So closing the tab, refreshing, or even switching
   the computer off does not lose anything - opening the site again
   loads the saved database back.

   The very first time somebody opens the site there is nothing
   saved, so the tables are created and filled from schema.js.

   ONE THING TO KNOW: the data is saved in THAT person's browser.
   Two different people opening the link each get their own copy,
   both starting from the same 50 medicines and the same logins.
   ============================================================ */

const DB = (function () {

    const DB_NAME = "medicareStorage";     // the IndexedDB database
    const STORE = "files";                 // the box inside it
    const KEY = "medicare.sqlite";         // the name we save under

    let database = null;                   // the open SQLite database
    let saveTimer = null;


    /* ---------- IndexedDB: a tiny box to keep the file in ---------- */

    function openStorage() {

        return new Promise(function (resolve, reject) {

            const request = indexedDB.open(DB_NAME, 1);

            request.onupgradeneeded = function () {
                request.result.createObjectStore(STORE);
            };

            request.onsuccess = function () { resolve(request.result); };
            request.onerror = function () { reject(request.error); };
        });
    }

    function readSavedBytes() {

        return openStorage().then(function (storage) {

            return new Promise(function (resolve) {

                const request = storage
                    .transaction(STORE, "readonly")
                    .objectStore(STORE)
                    .get(KEY);

                request.onsuccess = function () { resolve(request.result || null); };
                request.onerror = function () { resolve(null); };
            });
        }).catch(function () {
            return null;                   // private window, storage blocked...
        });
    }

    function writeSavedBytes(bytes) {

        return openStorage().then(function (storage) {

            return new Promise(function (resolve) {

                const request = storage
                    .transaction(STORE, "readwrite")
                    .objectStore(STORE)
                    .put(bytes, KEY);

                request.onsuccess = function () { resolve(true); };
                request.onerror = function () { resolve(false); };
            });
        }).catch(function () {
            return false;
        });
    }

    function clearSaved() {

        return openStorage().then(function (storage) {

            return new Promise(function (resolve) {

                const request = storage
                    .transaction(STORE, "readwrite")
                    .objectStore(STORE)
                    .delete(KEY);

                request.onsuccess = function () { resolve(true); };
                request.onerror = function () { resolve(false); };
            });
        }).catch(function () {
            return false;
        });
    }


    /* ---------- opening the database ---------- */

    /**
     * Called once by every page before it does anything else.
     * Loads the saved database, or builds a fresh one.
     */
    async function open() {

        if (database) {
            return database;               // already open on this page
        }

        // start the SQLite engine (the .wasm file sits next to it)
        const SQL = await initSqlJs({
            locateFile: function (file) {
                return resolveVendorPath(file);
            }
        });

        const saved = await readSavedBytes();

        if (saved) {
            // there is a database from last time - open it
            database = new SQL.Database(new Uint8Array(saved));

        } else {
            // first ever visit - create the tables and fill them
            database = new SQL.Database();
            database.run(DATABASE_SCHEMA);
            database.run(DATABASE_SEED);
            await save();
        }

        return database;
    }


    /**
     * Works out the folder the sql-wasm files are in, whichever
     * page is asking (the pages sit in /user, /admin or the root).
     */
    function resolveVendorPath(file) {

        const scripts = document.getElementsByTagName("script");

        for (let i = 0; i < scripts.length; i++) {

            const src = scripts[i].src || "";

            if (src.indexOf("sql-wasm.js") >= 0) {
                return src.replace("sql-wasm.js", file);
            }
        }

        return file;
    }


    /* ---------- saving ---------- */

    /** Writes the whole database back into the browser's storage. */
    async function save() {

        if (!database) {
            return;
        }

        await writeSavedBytes(database.export());
    }

    /**
     * Save, but wait a moment first. When something does several
     * writes in a row (placing an order writes many rows) this
     * saves once at the end instead of after every single row.
     */
    function saveSoon() {

        clearTimeout(saveTimer);
        saveTimer = setTimeout(save, 150);
    }

    /**
     * Save RIGHT NOW and wait until it is done.
     *
     * This matters because a page often moves somewhere else the
     * moment a write finishes - placing an order jumps to the bill
     * page. If the save were still waiting its turn, the next page
     * would load the old database and the new bill would be missing.
     */
    async function flush() {

        clearTimeout(saveTimer);
        await save();
    }


    /* ---------- running SQL ---------- */

    /**
     * A SELECT. Returns an array of plain objects, one per row:
     *   query("SELECT * FROM medicine WHERE id = ?", [5])
     */
    function query(sql, values) {

        const statement = database.prepare(sql);
        const rows = [];

        statement.bind(values || []);

        while (statement.step()) {
            rows.push(statement.getAsObject());
        }

        statement.free();

        return rows;
    }

    /** The same, but when you only want the first row (or null). */
    function queryOne(sql, values) {

        const rows = query(sql, values);

        return rows.length > 0 ? rows[0] : null;
    }

    /** An INSERT, UPDATE or DELETE. Saves afterwards. */
    function run(sql, values) {

        database.run(sql, values || []);
        saveSoon();
    }

    /** The id MySQL would have called LAST_INSERT_ID(). */
    function lastInsertId() {

        const row = queryOne("SELECT last_insert_rowid() AS id");

        return row ? row.id : null;
    }


    /* ---------- extras ---------- */

    /** Throw everything away and start again from the 50 medicines. */
    async function resetToOriginal() {

        await clearSaved();

        if (database) {
            database.close();
            database = null;
        }

        await open();
    }

    /** Hand the whole database to the user as a .sqlite file. */
    function downloadFile() {

        const blob = new Blob([database.export()],
                              { type: "application/octet-stream" });

        const link = document.createElement("a");
        link.href = URL.createObjectURL(blob);
        link.download = "medicare.sqlite";
        link.click();

        URL.revokeObjectURL(link.href);
    }

    /** How many rows each table holds - used by the reset screen. */
    function counts() {

        return {
            admin: queryOne("SELECT COUNT(*) AS n FROM admin").n,
            users: queryOne("SELECT COUNT(*) AS n FROM users").n,
            medicine: queryOne("SELECT COUNT(*) AS n FROM medicine").n,
            orders: queryOne("SELECT COUNT(*) AS n FROM orders").n
        };
    }

    return {
        open: open,
        query: query,
        queryOne: queryOne,
        run: run,
        lastInsertId: lastInsertId,
        save: save,
        flush: flush,
        resetToOriginal: resetToOriginal,
        downloadFile: downloadFile,
        counts: counts
    };
})();
