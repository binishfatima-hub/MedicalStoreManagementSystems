# MediCare — the version that runs with no server

This folder is the **whole working project as one website**. There is no
Java, no MySQL server, and nothing to install. Open the link and it runs.

## The link

Once GitHub Pages is switched on (see below), the address is:

```
https://YOUR-USERNAME.github.io/YOUR-REPO/
```

## Logins

| Panel | Login |
|---|---|
| Admin | `admin` / `admin123` |
| User | `priya@gmail.com` / `priya123` |
| User | `rahul@gmail.com` / `rahul123` |

New customers can also register themselves from the Register page.

## How it works without a server

`sql.js` is **real SQLite**, compiled so it can run inside a browser. So the
project still has genuine tables and genuine SQL — `CREATE TABLE`, `INSERT`,
`SELECT`, `UPDATE` — there just is no separate database server.

```
Before                              Now
------                              ---
page  ->  Java (Spring Boot)        page  ->  api.js
             ->  MySQL server                    ->  SQLite in the browser
```

`api.js` still has the same four functions (`apiGet`, `apiPost`, `apiPut`,
`apiDelete`) and the same addresses (`/api/medicines`, `/api/orders` ...), so
**not one of the 18 HTML pages had to be changed**. Only what happens behind
those functions is different: they run SQL instead of calling a server.

Every rule from the Java version was carried over:

- the selling price can never fall below the purchase price, discount included
- stock is checked before an order is saved
- prices come from the database, never from the page
- profit is left out when a purchase price was never filled in

## Where the data is kept

The whole database is saved in **IndexedDB**, which is storage the browser
keeps on the computer. Closing the tab, refreshing, or shutting the machine
down loses nothing — reopening the site loads the saved database back.

One thing to know: the data sits in **that person's browser**. If two people
open the link, each gets their own copy, both starting from the same 50
medicines and the same logins. Nothing they do affects each other.

There is a **Database** card in *Admin Panel → Profile* that shows the row
counts, lets you download the real `medicare.sqlite` file (it opens in DB
Browser for SQLite), and resets everything back to the starting data.

## Files

```
docs/
├── index.html              the first page (User Panel / Admin Panel)
├── user/                   11 pages - register, login, shop, cart,
│                           checkout, bill, my orders, profile...
├── admin/                  7 pages - dashboard, medicines, stock report,
│                           sales report, users, profile
└── assets/
    ├── css/style.css       one stylesheet for everything
    ├── vendor/
    │   ├── sql-wasm.js     the SQLite engine
    │   └── sql-wasm.wasm   (kept here on purpose, so the site does not
    │                        depend on any outside website)
    └── js/
        ├── schema.js       the SQL that builds and fills the database
        ├── db.js           opens SQLite, runs the SQL, saves it
        ├── sha256.js       hashes passwords (same hash as before)
        ├── api.js          all the rules and calculations
        ├── session.js      who is logged in
        ├── common.js       money, dates, badges
        └── cart.js         the shopping cart
```

## Turning GitHub Pages on

In the repository on GitHub:

1. **Settings** → **Pages** (left side)
2. **Source**: `Deploy from a branch`
3. **Branch**: `main`, **Folder**: `/docs`
4. **Save**

Wait a minute, refresh, and the link appears at the top of that page.

## The Java version is still here

`MedicareBackend/` and `MediCareWebsite/` in the parent folder are untouched —
the original Java + Spring Boot + MySQL project. This folder is a second way
to run the same thing, for when there is nothing to install on.
