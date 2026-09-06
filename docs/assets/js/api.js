/* ============================================================
   api.js  -  every page talks to the database through this file.

   In the Java version these same four functions (apiGet, apiPost,
   apiPut, apiDelete) sent a request over the network to a Spring
   Boot server. Here they run the SQL directly against the SQLite
   database in the browser instead.

   The function names, the addresses like "/api/medicines", and the
   shape of the answers are all exactly the same as before, which is
   why not one of the HTML pages had to change.

   All the rules that used to live in the Java service classes are
   kept here:
     - the selling price can never fall below the purchase price
     - stock is checked before an order is saved
     - the price always comes from the database, never the page
     - profit is left out when the purchase price was never filled
   ============================================================ */


/* ============================================================
   PART 1 - small helpers
   ============================================================ */

/** Money to 2 decimal places. */
function round2db(value) {
    return Math.round((Number(value) || 0) * 100) / 100;
}

function num(value) {
    return Number(value) || 0;
}

function isBlank(text) {
    return text === null || text === undefined || String(text).trim() === "";
}

/** The answer shape every page expects. */
function ok(message, data) {
    return { success: true, message: message, data: data === undefined ? null : data };
}

function fail(message) {
    return { success: false, message: message, data: null };
}

/** "2026-09-06 21:52:47" -> "2026-09-06T21:52:47" so dates parse everywhere. */
function isoDate(text) {
    return text ? String(text).replace(" ", "T") : text;
}

/** Today as "2026-09-06". */
function todayISO() {

    const d = new Date();

    return d.getFullYear() + "-" +
           String(d.getMonth() + 1).padStart(2, "0") + "-" +
           String(d.getDate()).padStart(2, "0");
}

/** Right now as "2026-09-06T21:52:47". */
function nowISO() {

    const d = new Date();

    return todayISO() + "T" +
           String(d.getHours()).padStart(2, "0") + ":" +
           String(d.getMinutes()).padStart(2, "0") + ":" +
           String(d.getSeconds()).padStart(2, "0");
}

/** 12.0 -> "12" ,  12.5 -> "12.5" */
function trimPercent(value) {

    const n = round2db(value);

    return Number.isInteger(n) ? String(n) : String(n);
}


/* ============================================================
   PART 2 - turning database rows into what the pages expect

   The database uses names like purchase_price; the pages use
   purchasePrice. These functions translate, and also work out
   the extra values the Java classes used to calculate.
   ============================================================ */

/** GST charged on medicines. Change this one line to change the rate. */
const GST_PERCENT = 5.0;

/** One row of the medicine table. */
function mapMedicine(row) {

    const purchasePrice = num(row.purchase_price);
    const price = num(row.price);
    const discountPercent = num(row.discount_percent);
    const quantity = num(row.quantity);

    const discountAmount = round2db(price * discountPercent / 100);
    const finalPrice = round2db(price - discountAmount);
    const profitPerUnit = round2db(finalPrice - purchasePrice);

    /* stock status - same limits as the Java version */
    let stockStatus = "IN STOCK";

    if (quantity <= 0) {
        stockStatus = "OUT OF STOCK";
    } else if (quantity <= 10) {
        stockStatus = "LOW STOCK";
    }

    /* expiry status */
    let expiryStatus = "OK";

    if (row.expiry_date) {

        const expiry = new Date(row.expiry_date);
        const today = new Date(todayISO());
        const inThirtyDays = new Date(todayISO());

        inThirtyDays.setDate(inThirtyDays.getDate() + 30);

        if (expiry < today) {
            expiryStatus = "EXPIRED";
        } else if (expiry < inThirtyDays) {
            expiryStatus = "EXPIRING SOON";
        }
    }

    return {
        id: row.id,
        name: row.name,
        company: row.company,
        purchasePrice: purchasePrice,
        price: price,
        discountPercent: discountPercent,
        quantity: quantity,
        expiryDate: row.expiry_date,

        discountAmount: discountAmount,
        finalPrice: finalPrice,
        profitPerUnit: profitPerUnit,
        profitPercent: purchasePrice > 0
            ? round2db(profitPerUnit / purchasePrice * 100)
            : 0,
        stockValue: round2db(purchasePrice * quantity),
        stockSellValue: round2db(finalPrice * quantity),
        purchasePriceMissing: purchasePrice <= 0,
        lossMaking: purchasePrice > 0 && finalPrice < purchasePrice,
        stockStatus: stockStatus,
        expiryStatus: expiryStatus
    };
}

/** One row of the users table. The password hash is never sent on. */
function mapUser(row) {

    return {
        id: row.id,
        name: row.name,
        email: row.email,
        mobile: row.mobile,
        status: row.status,
        createdAt: isoDate(row.created_at)
    };
}

function mapAdmin(row) {

    return {
        id: row.id,
        name: row.name,
        username: row.username,
        email: row.email,
        mobile: row.mobile,
        createdAt: isoDate(row.created_at)
    };
}

/** One medicine line of a bill. */
function mapOrderLine(row) {

    const subtotal = num(row.subtotal);
    const discount = num(row.discount);
    const purchasePrice = num(row.purchase_price);
    const quantity = num(row.quantity);

    const taxable = round2db(subtotal - discount);
    const cost = round2db(purchasePrice * quantity);

    return {
        id: row.id,
        billNumber: row.bill_number,
        userId: row.user_id,
        userEmail: row.user_email,
        customerName: row.customer_name,
        mobile: row.mobile,
        medicineId: row.medicine_id,
        medicineName: row.medicine_name,
        purchasePrice: purchasePrice,
        quantity: quantity,
        price: num(row.price),
        discountPercent: num(row.discount_percent),
        subtotal: subtotal,
        discount: discount,
        gst: num(row.gst),
        total: num(row.total),
        orderDate: isoDate(row.order_date),

        taxableAmount: taxable,
        costAmount: cost,
        profit: round2db(taxable - cost),
        costKnown: purchasePrice > 0
    };
}

/** Several lines that share one bill number, added up into one bill. */
function makeBill(billNumber, lines) {

    let totalQuantity = 0, subtotal = 0, discount = 0;
    let gst = 0, total = 0, cost = 0, costKnown = true;

    lines.forEach(function (line) {
        totalQuantity += line.quantity;
        subtotal += line.subtotal;
        discount += line.discount;
        gst += line.gst;
        total += line.total;
        cost += line.costAmount;

        if (!line.costKnown) {
            costKnown = false;
        }
    });

    const taxable = round2db(subtotal - discount);

    return {
        billNumber: billNumber,
        customerName: lines.length ? lines[0].customerName : "",
        mobile: lines.length ? lines[0].mobile : "",
        userEmail: lines.length ? lines[0].userEmail : null,
        orderDate: lines.length ? lines[0].orderDate : null,
        itemCount: lines.length,
        totalQuantity: totalQuantity,
        subtotal: round2db(subtotal),
        discount: round2db(discount),
        taxable: taxable,
        gst: round2db(gst),
        total: round2db(total),
        cost: round2db(cost),
        profit: round2db(taxable - round2db(cost)),
        costKnown: costKnown,
        items: lines
    };
}

/** Groups order lines into bills, keeping the order they came in. */
function groupIntoBills(lines) {

    const order = [];
    const grouped = {};

    lines.forEach(function (line) {

        if (!grouped[line.billNumber]) {
            grouped[line.billNumber] = [];
            order.push(line.billNumber);
        }

        grouped[line.billNumber].push(line);
    });

    return order.map(function (billNumber) {
        return makeBill(billNumber, grouped[billNumber]);
    });
}


/* ============================================================
   PART 3 - the rules

   The price rule is the important one, and it is written once
   here so ADD and EDIT can never disagree.
   ============================================================ */

function validateMedicine(m) {

    if (isBlank(m.name)) {
        return "Medicine name is required";
    }

    const purchasePrice = num(m.purchasePrice);
    const price = num(m.price);
    const discountPercent = num(m.discountPercent);

    if (purchasePrice < 0) {
        return "Purchase price must be 0 or more";
    }

    if (price < 0) {
        return "Selling price must be 0 or more";
    }

    if (discountPercent < 0 || discountPercent > 100) {
        return "Discount must be between 0 and 100 percent";
    }

    if (num(m.quantity) < 0) {
        return "Quantity must be 0 or more";
    }

    /* the store must never sell below what it paid */

    if (purchasePrice > 0 && price < purchasePrice) {

        return "Selling price (Rs. " + price.toFixed(2) + ") cannot be less than "
             + "the purchase price (Rs. " + purchasePrice.toFixed(2) + "). "
             + "The store would lose Rs. " + (purchasePrice - price).toFixed(2)
             + " on every unit.";
    }

    const finalPrice = round2db(price - (price * discountPercent / 100));

    if (purchasePrice > 0 && finalPrice < purchasePrice) {

        const maxDiscount = round2db((price - purchasePrice) / price * 100);

        return "A " + trimPercent(discountPercent) + "% discount brings the price "
             + "down to Rs. " + finalPrice.toFixed(2) + ", which is below the "
             + "purchase price of Rs. " + purchasePrice.toFixed(2) + ". The highest "
             + "discount you can give on this medicine is "
             + trimPercent(maxDiscount) + "%.";
    }

    return null;                           // null means "no problem"
}


/* ============================================================
   PART 4 - the handlers, one per address
   ============================================================ */

const HANDLERS = {

    /* ---------------- LOGIN AND REGISTRATION ---------------- */

    "POST /api/auth/admin/login": function (body) {

        if (isBlank(body.username) || isBlank(body.password)) {
            return fail("Please enter username and password");
        }

        const row = DB.queryOne(
            "SELECT * FROM admin WHERE username = ?", [body.username.trim()]);

        if (!row || !passwordMatches(body.password, row.password)) {
            return fail("Invalid username or password");
        }

        return ok("Login successful", mapAdmin(row));
    },

    "POST /api/auth/user/register": function (body) {

        const name = (body.name || "").trim();
        const email = (body.email || "").trim().toLowerCase();
        const mobile = (body.mobile || "").trim();
        const password = body.password;

        if (isBlank(name)) {
            return fail("Please enter your name");
        }

        if (isBlank(email) || email.indexOf("@") < 0 || email.indexOf(".") < 0) {
            return fail("Please enter a valid email address");
        }

        if (!/^[0-9]{10}$/.test(mobile)) {
            return fail("Mobile number must be exactly 10 digits");
        }

        if (isBlank(password) || password.length < 4) {
            return fail("Password must be at least 4 characters");
        }

        if (password !== body.confirmPassword) {
            return fail("Password and confirm password do not match");
        }

        const existing = DB.queryOne("SELECT id FROM users WHERE email = ?", [email]);

        if (existing) {
            return fail("This email is already registered. Please login.");
        }

        DB.run(
            "INSERT INTO users (name, email, mobile, password, status, created_at) " +
            "VALUES (?, ?, ?, ?, 'ACTIVE', ?)",
            [name, email, mobile, hashPassword(password), nowISO()]);

        const saved = DB.queryOne("SELECT * FROM users WHERE email = ?", [email]);

        return ok("Registration successful! You can login now.", mapUser(saved));
    },

    "POST /api/auth/user/login": function (body) {

        if (isBlank(body.email) || isBlank(body.password)) {
            return fail("Please enter email and password");
        }

        const row = DB.queryOne("SELECT * FROM users WHERE email = ?",
                                [body.email.trim().toLowerCase()]);

        if (!row || !passwordMatches(body.password, row.password)) {
            return fail("Invalid email or password");
        }

        if (String(row.status).toUpperCase() === "BLOCKED") {
            return fail("Your account has been blocked by the admin");
        }

        return ok("Login successful", mapUser(row));
    },


    /* ---------------- MEDICINES ---------------- */

    "GET /api/medicines": function () {

        return DB.query("SELECT * FROM medicine ORDER BY name COLLATE NOCASE ASC")
                 .map(mapMedicine);
    },

    "GET /api/medicines/search": function (body, params) {

        const keyword = (params.keyword || "").trim();

        if (!keyword) {
            return HANDLERS["GET /api/medicines"]();
        }

        const like = "%" + keyword + "%";

        return DB.query(
            "SELECT * FROM medicine WHERE name LIKE ? OR company LIKE ? " +
            "ORDER BY name COLLATE NOCASE ASC", [like, like]).map(mapMedicine);
    },

    "GET /api/medicines/:id": function (body, params, id) {

        const row = DB.queryOne("SELECT * FROM medicine WHERE id = ?", [id]);

        return row ? ok("Found", mapMedicine(row)) : fail("Medicine not found");
    },

    "POST /api/medicines": function (body) {

        const problem = validateMedicine(body);

        if (problem) {
            return fail(problem);
        }

        DB.run(
            "INSERT INTO medicine " +
            "(name, company, purchase_price, price, discount_percent, quantity, expiry_date) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)",
            [body.name.trim(), body.company || null, num(body.purchasePrice),
             num(body.price), num(body.discountPercent), num(body.quantity),
             body.expiryDate || null]);

        const saved = DB.queryOne("SELECT * FROM medicine WHERE id = ?",
                                  [DB.lastInsertId()]);

        return ok("Medicine added successfully", mapMedicine(saved));
    },

    "PUT /api/medicines/:id": function (body, params, id) {

        const existing = DB.queryOne("SELECT id FROM medicine WHERE id = ?", [id]);

        if (!existing) {
            return fail("Medicine not found");
        }

        const problem = validateMedicine(body);

        if (problem) {
            return fail(problem);
        }

        DB.run(
            "UPDATE medicine SET name = ?, company = ?, purchase_price = ?, " +
            "price = ?, discount_percent = ?, quantity = ?, expiry_date = ? " +
            "WHERE id = ?",
            [body.name.trim(), body.company || null, num(body.purchasePrice),
             num(body.price), num(body.discountPercent), num(body.quantity),
             body.expiryDate || null, id]);

        const saved = DB.queryOne("SELECT * FROM medicine WHERE id = ?", [id]);

        return ok("Medicine updated successfully", mapMedicine(saved));
    },

    "DELETE /api/medicines/:id": function (body, params, id) {

        const existing = DB.queryOne("SELECT id FROM medicine WHERE id = ?", [id]);

        if (!existing) {
            return fail("Medicine not found");
        }

        DB.run("DELETE FROM medicine WHERE id = ?", [id]);

        return ok("Medicine deleted successfully");
    },

    /* action = ADD | REMOVE | SET */
    "PUT /api/medicines/:id/stock": function (body, params, id) {

        const row = DB.queryOne("SELECT * FROM medicine WHERE id = ?", [id]);

        if (!row) {
            return fail("Medicine not found");
        }

        const amount = num(params.amount);
        const action = String(params.action || "").toUpperCase();

        if (amount < 0) {
            return fail("Quantity cannot be negative");
        }

        const current = num(row.quantity);
        let updated;

        if (action === "ADD") {
            updated = current + amount;

        } else if (action === "REMOVE") {
            updated = current - amount;

            if (updated < 0) {
                return fail("Cannot remove " + amount + " units, only " +
                            current + " in stock");
            }

        } else if (action === "SET") {
            updated = amount;

        } else {
            return fail("Action must be ADD, REMOVE or SET");
        }

        DB.run("UPDATE medicine SET quantity = ? WHERE id = ?", [updated, id]);

        const saved = DB.queryOne("SELECT * FROM medicine WHERE id = ?", [id]);

        return ok("Stock updated: " + current + " -> " + updated, mapMedicine(saved));
    },


    /* ---------------- ORDERS ---------------- */

    "POST /api/orders": function (body) {

        if (!body.items || body.items.length === 0) {
            return fail("Your cart is empty");
        }

        if (isBlank(body.customerName)) {
            return fail("Customer name is required");
        }

        if (!/^[0-9]{10}$/.test((body.mobile || "").trim())) {
            return fail("Mobile number must be exactly 10 digits");
        }

        /* ---- check every medicine BEFORE saving anything ---- */

        const wanted = {};      // medicine id -> total quantity asked for
        const medicines = {};

        for (let i = 0; i < body.items.length; i++) {

            const item = body.items[i];
            const quantity = num(item.quantity);

            if (!item.medicineId) {
                return fail("Invalid item in cart");
            }

            if (quantity <= 0) {
                return fail("Quantity must be at least 1");
            }

            const row = DB.queryOne("SELECT * FROM medicine WHERE id = ?",
                                    [item.medicineId]);

            if (!row) {
                return fail("A medicine in your cart no longer exists");
            }

            // the same medicine can appear twice in a cart, so add it up
            const total = (wanted[row.id] || 0) + quantity;
            const available = num(row.quantity);

            if (total > available) {
                return fail("Not enough stock for " + row.name +
                            " (available: " + available + ")");
            }

            medicines[row.id] = row;
            wanted[row.id] = total;
        }

        /* ---- save the bill ---- */

        const now = new Date();
        const stamp = todayISO().replace(/-/g, "") + "-" +
                      String(now.getHours()).padStart(2, "0") +
                      String(now.getMinutes()).padStart(2, "0") +
                      String(now.getSeconds()).padStart(2, "0");

        const billNumber = "BILL-" + stamp;
        const orderDate = nowISO();
        const savedLines = [];

        Object.keys(wanted).forEach(function (key) {

            const medicine = medicines[key];
            const quantity = wanted[key];

            /* the price comes from the DATABASE, never from the page */
            const price = num(medicine.price);
            const purchasePrice = num(medicine.purchase_price);
            const discountPercent = num(medicine.discount_percent);

            const subtotal = round2db(price * quantity);
            const discount = round2db(subtotal * discountPercent / 100);
            const taxable = round2db(subtotal - discount);
            const gst = round2db(taxable * GST_PERCENT / 100);
            const total = round2db(taxable + gst);

            DB.run(
                "INSERT INTO orders (bill_number, user_id, user_email, customer_name, " +
                "mobile, medicine_id, medicine_name, purchase_price, quantity, price, " +
                "discount_percent, subtotal, discount, gst, total, order_date) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                [billNumber, body.userId || null, body.userEmail || null,
                 body.customerName.trim(), body.mobile.trim(), medicine.id,
                 medicine.name, purchasePrice, quantity, price, discountPercent,
                 subtotal, discount, gst, total, orderDate]);

            /* reduce the stock */
            DB.run("UPDATE medicine SET quantity = quantity - ? WHERE id = ?",
                   [quantity, medicine.id]);

            savedLines.push(mapOrderLine(
                DB.queryOne("SELECT * FROM orders WHERE id = ?", [DB.lastInsertId()])));
        });

        return ok("Order placed successfully", makeBill(billNumber, savedLines));
    },

    "GET /api/orders/lines": function () {

        return DB.query("SELECT * FROM orders ORDER BY order_date DESC, id DESC")
                 .map(mapOrderLine);
    },

    "GET /api/orders/bills": function () {

        return groupIntoBills(
            DB.query("SELECT * FROM orders ORDER BY order_date DESC, id DESC")
              .map(mapOrderLine));
    },

    "GET /api/orders/bills/user/:id": function (body, params, id) {

        return groupIntoBills(
            DB.query("SELECT * FROM orders WHERE user_id = ? " +
                     "ORDER BY order_date DESC, id DESC", [id]).map(mapOrderLine));
    },

    "GET /api/orders/bills/:billNumber": function (body, params, billNumber) {

        const lines = DB.query("SELECT * FROM orders WHERE bill_number = ? ORDER BY id",
                               [billNumber]).map(mapOrderLine);

        if (lines.length === 0) {
            return fail("Bill not found");
        }

        return ok("Found", makeBill(billNumber, lines));
    },

    "DELETE /api/orders/bills/:billNumber": function (body, params, billNumber) {

        const lines = DB.query("SELECT id FROM orders WHERE bill_number = ?",
                               [billNumber]);

        if (lines.length === 0) {
            return fail("Bill not found");
        }

        DB.run("DELETE FROM orders WHERE bill_number = ?", [billNumber]);

        return ok("Bill " + billNumber + " deleted");
    },


    /* ---------------- USERS ---------------- */

    "GET /api/users": function () {

        return DB.query("SELECT * FROM users ORDER BY id").map(mapUser);
    },

    "GET /api/users/:id": function (body, params, id) {

        const row = DB.queryOne("SELECT * FROM users WHERE id = ?", [id]);

        return row ? ok("Found", mapUser(row)) : fail("User not found");
    },

    "PUT /api/users/:id": function (body, params, id) {

        const row = DB.queryOne("SELECT * FROM users WHERE id = ?", [id]);

        if (!row) {
            return fail("User not found");
        }

        const name = isBlank(body.name) ? row.name : body.name.trim();
        let mobile = row.mobile;

        if (body.mobile !== undefined && body.mobile !== null) {

            if (!/^[0-9]{10}$/.test(String(body.mobile).trim())) {
                return fail("Mobile number must be exactly 10 digits");
            }

            mobile = String(body.mobile).trim();
        }

        DB.run("UPDATE users SET name = ?, mobile = ? WHERE id = ?",
               [name, mobile, id]);

        return ok("Profile updated successfully",
                  mapUser(DB.queryOne("SELECT * FROM users WHERE id = ?", [id])));
    },

    "PUT /api/users/:id/password": function (body, params, id) {

        const row = DB.queryOne("SELECT * FROM users WHERE id = ?", [id]);

        if (!row) {
            return fail("User not found");
        }

        if (!passwordMatches(body.oldPassword, row.password)) {
            return fail("Current password is wrong");
        }

        if (isBlank(body.newPassword) || body.newPassword.length < 4) {
            return fail("New password must be at least 4 characters");
        }

        if (body.newPassword !== body.confirmPassword) {
            return fail("New password and confirm password do not match");
        }

        DB.run("UPDATE users SET password = ? WHERE id = ?",
               [hashPassword(body.newPassword), id]);

        return ok("Password updated successfully");
    },

    "PUT /api/users/:id/status": function (body, params, id) {

        const row = DB.queryOne("SELECT * FROM users WHERE id = ?", [id]);

        if (!row) {
            return fail("User not found");
        }

        const status = String(params.status || "").toUpperCase();

        if (status !== "ACTIVE" && status !== "BLOCKED") {
            return fail("Status must be ACTIVE or BLOCKED");
        }

        DB.run("UPDATE users SET status = ? WHERE id = ?", [status, id]);

        return ok("User is now " + status,
                  mapUser(DB.queryOne("SELECT * FROM users WHERE id = ?", [id])));
    },

    "DELETE /api/users/:id": function (body, params, id) {

        const row = DB.queryOne("SELECT id FROM users WHERE id = ?", [id]);

        if (!row) {
            return fail("User not found");
        }

        DB.run("DELETE FROM users WHERE id = ?", [id]);

        return ok("User deleted successfully");
    },


    /* ---------------- ADMIN PROFILE ---------------- */

    "GET /api/admins/:id": function (body, params, id) {

        const row = DB.queryOne("SELECT * FROM admin WHERE id = ?", [id]);

        return row ? ok("Found", mapAdmin(row)) : fail("Admin not found");
    },

    "PUT /api/admins/:id": function (body, params, id) {

        const row = DB.queryOne("SELECT * FROM admin WHERE id = ?", [id]);

        if (!row) {
            return fail("Admin not found");
        }

        DB.run("UPDATE admin SET name = ?, email = ?, mobile = ? WHERE id = ?",
               [isBlank(body.name) ? row.name : body.name.trim(),
                isBlank(body.email) ? row.email : body.email.trim(),
                isBlank(body.mobile) ? row.mobile : body.mobile.trim(), id]);

        return ok("Profile updated successfully",
                  mapAdmin(DB.queryOne("SELECT * FROM admin WHERE id = ?", [id])));
    },

    "PUT /api/admins/:id/password": function (body, params, id) {

        const row = DB.queryOne("SELECT * FROM admin WHERE id = ?", [id]);

        if (!row) {
            return fail("Admin not found");
        }

        if (!passwordMatches(body.oldPassword, row.password)) {
            return fail("Current password is wrong");
        }

        if (isBlank(body.newPassword) || body.newPassword.length < 4) {
            return fail("New password must be at least 4 characters");
        }

        if (body.newPassword !== body.confirmPassword) {
            return fail("New password and confirm password do not match");
        }

        DB.run("UPDATE admin SET password = ? WHERE id = ?",
               [hashPassword(body.newPassword), id]);

        return ok("Password updated successfully");
    },


    /* ---------------- REPORTS ---------------- */

    "GET /api/reports/dashboard": function () {

        const medicines = DB.query("SELECT * FROM medicine").map(mapMedicine);
        const lines = DB.query("SELECT * FROM orders").map(mapOrderLine);
        const users = DB.query("SELECT * FROM users");

        let totalStock = 0, inStock = 0, lowStock = 0, outOfStock = 0;
        let expired = 0, expiringSoon = 0, missingCost = 0;
        let stockValue = 0, stockSellValue = 0;

        medicines.forEach(function (m) {

            totalStock += m.quantity;
            stockValue += m.stockValue;
            stockSellValue += m.stockSellValue;

            if (m.purchasePriceMissing) missingCost++;

            if (m.stockStatus === "OUT OF STOCK") outOfStock++;
            else if (m.stockStatus === "LOW STOCK") lowStock++;
            else inStock++;

            if (m.expiryStatus === "EXPIRED") expired++;
            else if (m.expiryStatus === "EXPIRING SOON") expiringSoon++;
        });

        const today = todayISO();
        const allBills = [], todayBills = [];

        let itemsSold = 0, totalSales = 0, todaySales = 0;
        let totalDiscount = 0, totalProfit = 0, todayProfit = 0;
        let linesWithoutCost = 0;

        lines.forEach(function (line) {

            itemsSold += line.quantity;
            totalSales += line.total;
            totalDiscount += line.discount;

            if (line.costKnown) {
                totalProfit += line.profit;
            } else {
                linesWithoutCost++;
            }

            if (allBills.indexOf(line.billNumber) < 0) {
                allBills.push(line.billNumber);
            }

            if (line.orderDate && line.orderDate.slice(0, 10) === today) {

                todaySales += line.total;

                if (line.costKnown) {
                    todayProfit += line.profit;
                }

                if (todayBills.indexOf(line.billNumber) < 0) {
                    todayBills.push(line.billNumber);
                }
            }
        });

        let activeUsers = 0;

        users.forEach(function (u) {
            if (String(u.status).toUpperCase() !== "BLOCKED") activeUsers++;
        });

        return {
            totalMedicines: medicines.length,
            totalStock: totalStock,
            inStock: inStock,
            lowStock: lowStock,
            outOfStock: outOfStock,
            expired: expired,
            expiringSoon: expiringSoon,
            missingCost: missingCost,
            stockValue: round2db(stockValue),
            stockSellValue: round2db(stockSellValue),

            totalBills: allBills.length,
            itemsSold: itemsSold,
            totalSales: round2db(totalSales),
            totalDiscount: round2db(totalDiscount),
            totalProfit: round2db(totalProfit),
            linesWithoutCost: linesWithoutCost,
            todayBills: todayBills.length,
            todaySales: round2db(todaySales),
            todayProfit: round2db(todayProfit),

            totalUsers: users.length,
            activeUsers: activeUsers,
            blockedUsers: users.length - activeUsers
        };
    },

    "GET /api/reports/stock": function () {

        const medicines = DB.query(
            "SELECT * FROM medicine ORDER BY name COLLATE NOCASE ASC").map(mapMedicine);

        let totalStock = 0, lowStock = 0, outOfStock = 0;
        let expired = 0, expiringSoon = 0, missingCost = 0, discounted = 0;
        let stockValue = 0, stockSellValue = 0;

        medicines.forEach(function (m) {

            totalStock += m.quantity;
            stockValue += m.stockValue;
            stockSellValue += m.stockSellValue;

            if (m.purchasePriceMissing) missingCost++;
            if (m.discountPercent > 0) discounted++;
            if (m.stockStatus === "LOW STOCK") lowStock++;
            if (m.stockStatus === "OUT OF STOCK") outOfStock++;
            if (m.expiryStatus === "EXPIRED") expired++;
            if (m.expiryStatus === "EXPIRING SOON") expiringSoon++;
        });

        return {
            summary: {
                totalMedicines: medicines.length,
                totalStock: totalStock,
                lowStock: lowStock,
                outOfStock: outOfStock,
                expired: expired,
                expiringSoon: expiringSoon,
                missingCost: missingCost,
                discounted: discounted,
                stockValue: round2db(stockValue),
                stockSellValue: round2db(stockSellValue),
                expectedProfit: round2db(stockSellValue - stockValue)
            },
            medicines: medicines
        };
    },

    "GET /api/reports/sales": function (body, params) {

        let sql = "SELECT * FROM orders";
        const values = [];

        if (params.from && params.to) {
            // the whole "to" day counts, so compare on the date part only
            sql += " WHERE substr(order_date, 1, 10) BETWEEN ? AND ?";
            values.push(params.from, params.to);
        }

        sql += " ORDER BY order_date DESC, id DESC";

        const lines = DB.query(sql, values).map(mapOrderLine);

        let subtotal = 0, discountTotal = 0, gst = 0, total = 0;
        let cost = 0, profit = 0, itemsSold = 0, linesWithoutCost = 0;

        const perMedicine = {};
        const perDay = {};

        lines.forEach(function (line) {

            subtotal += line.subtotal;
            discountTotal += line.discount;
            gst += line.gst;
            total += line.total;
            cost += line.costAmount;
            itemsSold += line.quantity;

            if (line.costKnown) {
                profit += line.profit;
            } else {
                linesWithoutCost++;
            }

            const name = line.medicineName;

            if (!perMedicine[name]) {
                perMedicine[name] = {
                    medicineName: name, quantitySold: 0,
                    revenue: 0, discount: 0, profit: 0, costKnown: true
                };
            }

            const top = perMedicine[name];
            top.quantitySold += line.quantity;
            top.revenue += line.total;
            top.discount += line.discount;

            if (line.costKnown) {
                top.profit += line.profit;
            } else {
                top.costKnown = false;
            }

            if (line.orderDate) {
                const day = line.orderDate.slice(0, 10);
                perDay[day] = round2db((perDay[day] || 0) + line.total);
            }
        });

        const topMedicines = Object.keys(perMedicine)
            .map(function (name) {

                const t = perMedicine[name];

                return {
                    medicineName: t.medicineName,
                    quantitySold: t.quantitySold,
                    revenue: round2db(t.revenue),
                    discount: round2db(t.discount),
                    profit: round2db(t.profit),
                    costKnown: t.costKnown
                };
            })
            .sort(function (a, b) { return b.revenue - a.revenue; })
            .slice(0, 10);

        const bills = groupIntoBills(lines);

        return {
            summary: {
                billCount: bills.length,
                itemsSold: itemsSold,
                subtotal: round2db(subtotal),
                discount: round2db(discountTotal),
                taxable: round2db(subtotal - discountTotal),
                gst: round2db(gst),
                totalSales: round2db(total),
                cost: round2db(cost),
                profit: round2db(profit),
                linesWithoutCost: linesWithoutCost,
                averageBill: bills.length ? round2db(total / bills.length) : 0
            },
            bills: bills,
            topMedicines: topMedicines,
            dayWise: perDay
        };
    }
};


/* ============================================================
   PART 5 - finding the right handler for an address

   "/api/medicines/12/stock?action=ADD&amount=5"
        becomes
   handler "PUT /api/medicines/:id/stock", id = 12,
   params = { action: "ADD", amount: "5" }
   ============================================================ */

function splitPath(path) {

    const questionMark = path.indexOf("?");
    const params = {};

    if (questionMark >= 0) {

        path.slice(questionMark + 1).split("&").forEach(function (pair) {

            if (!pair) return;

            const bits = pair.split("=");
            params[decodeURIComponent(bits[0])] =
                decodeURIComponent((bits[1] || "").replace(/\+/g, " "));
        });

        path = path.slice(0, questionMark);
    }

    return { path: path, params: params };
}

/** Tries each known address until one fits. */
function findHandler(method, path) {

    const wanted = path.split("/").filter(Boolean);

    const keys = Object.keys(HANDLERS);

    for (let k = 0; k < keys.length; k++) {

        const key = keys[k];
        const space = key.indexOf(" ");

        if (key.slice(0, space) !== method) {
            continue;
        }

        const pattern = key.slice(space + 1).split("/").filter(Boolean);

        if (pattern.length !== wanted.length) {
            continue;
        }

        let matched = true;
        let value = null;

        for (let i = 0; i < pattern.length; i++) {

            if (pattern[i].charAt(0) === ":") {
                value = decodeURIComponent(wanted[i]);      // :id, :billNumber

            } else if (pattern[i] !== wanted[i]) {
                matched = false;
                break;
            }
        }

        if (matched) {
            return { handler: HANDLERS[key], value: value };
        }
    }

    return null;
}


/* ============================================================
   PART 6 - the four functions every page uses

   They are "async" and they throw on failure, exactly like the
   old fetch-based ones, so the pages did not have to change.
   ============================================================ */

async function apiRequest(path, method, body) {

    await DB.open();                       // makes sure the database is ready

    const split = splitPath(path);
    const found = findHandler(method, split.path);

    if (!found) {
        throw new Error("Unknown address: " + method + " " + split.path);
    }

    let result;

    try {
        result = found.handler(body || {}, split.params, found.value);

    } catch (error) {
        console.error(error);
        throw new Error("Something went wrong: " + error.message);
    }

    /* Anything that changed the data is written to the browser's
       storage before we hand control back. Pages often move to a
       new page as soon as they get an answer (checkout jumps to
       the bill), so the save cannot be left for later. */
    if (method !== "GET") {
        await DB.flush();
    }

    return result;
}

function apiGet(path) {
    return apiRequest(path, "GET");
}

function apiPost(path, body) {
    return apiRequest(path, "POST", body);
}

function apiPut(path, body) {
    return apiRequest(path, "PUT", body);
}

function apiDelete(path) {
    return apiRequest(path, "DELETE");
}
