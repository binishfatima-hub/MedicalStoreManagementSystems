/* ============================================================
   schema.js  -  the SQL that builds the MediCare database
   inside the browser and fills it with the starting data.

   It is the same design as database/schema.sql, written in
   SQLite's wording instead of MySQL's:

       MySQL                          SQLite
       INT AUTO_INCREMENT      ->     INTEGER ... AUTOINCREMENT
       DOUBLE                  ->     REAL
       ENGINE=InnoDB           ->     (not needed)
       SHA2('admin123', 256)   ->     the finished hash, written out

   This is kept as a JavaScript string on purpose - a browser
   cannot read a .sql file from the folder when the page is
   opened directly from the disk, but it can always read this.
   ============================================================ */

const DATABASE_SCHEMA = `

-- ============================================================
--  1. ADMIN  -  the store owner. Not created from the website.
--     Login:  admin  /  admin123
-- ============================================================
CREATE TABLE IF NOT EXISTS admin (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    name        TEXT NOT NULL,
    username    TEXT NOT NULL UNIQUE,
    email       TEXT NOT NULL UNIQUE,
    mobile      TEXT,
    password    TEXT NOT NULL,
    created_at  TEXT NOT NULL DEFAULT (datetime('now','localtime'))
);

-- ============================================================
--  2. USERS  -  customers. They register themselves.
--     The email is the login id.
-- ============================================================
CREATE TABLE IF NOT EXISTS users (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    name        TEXT NOT NULL,
    email       TEXT NOT NULL UNIQUE,
    mobile      TEXT NOT NULL,
    password    TEXT NOT NULL,
    status      TEXT NOT NULL DEFAULT 'ACTIVE',
    created_at  TEXT NOT NULL DEFAULT (datetime('now','localtime'))
);

-- ============================================================
--  3. MEDICINE  -  the stock.
--     purchase_price   what the store paid      (cost)
--     price            the MRP for the customer (selling price)
--     discount_percent how much off the MRP
-- ============================================================
CREATE TABLE IF NOT EXISTS medicine (
    id                INTEGER PRIMARY KEY AUTOINCREMENT,
    name              TEXT NOT NULL,
    company           TEXT,
    purchase_price    REAL NOT NULL DEFAULT 0,
    price             REAL,
    discount_percent  REAL NOT NULL DEFAULT 0,
    quantity          INTEGER,
    expiry_date       TEXT
);

-- ============================================================
--  4. ORDERS  -  one row is ONE medicine line of a bill.
--     All lines of a bill share the same bill_number.
-- ============================================================
CREATE TABLE IF NOT EXISTS orders (
    id                INTEGER PRIMARY KEY AUTOINCREMENT,
    bill_number       TEXT NOT NULL,
    user_id           INTEGER,
    user_email        TEXT,
    customer_name     TEXT NOT NULL,
    mobile            TEXT NOT NULL,
    medicine_id       INTEGER NOT NULL,
    medicine_name     TEXT NOT NULL,
    purchase_price    REAL NOT NULL DEFAULT 0,
    quantity          INTEGER NOT NULL,
    price             REAL NOT NULL,
    discount_percent  REAL NOT NULL DEFAULT 0,
    subtotal          REAL NOT NULL,
    discount          REAL NOT NULL DEFAULT 0,
    gst               REAL NOT NULL,
    total             REAL NOT NULL,
    order_date        TEXT NOT NULL
);
`;


/* ============================================================
   The starting data. This runs only the first time, when there
   is no saved database in the browser yet.

   Passwords are stored as a SHA-256 hash, never as plain text.
   These are the exact same hashes the MySQL version had.
   ============================================================ */

const DATABASE_SEED = `

-- admin  /  admin123
INSERT INTO admin (name, username, email, mobile, password) VALUES
    ('Store Admin', 'admin', 'admin@medicare.com', '9999999999',
     '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9');

-- two sample customers, so the user panel can be opened at once
--   priya@gmail.com  /  priya123
--   rahul@gmail.com  /  rahul123
INSERT INTO users (name, email, mobile, password, status) VALUES
    ('Priya Sharma', 'priya@gmail.com', '9876543210',
     '0a8b8dfad3f637d7d30fed7b108c5c5986c4775d14cab26ec9279866eba99116', 'ACTIVE'),
    ('Rahul Verma', 'rahul@gmail.com', '9123456780',
     '687f6da20de59091e67f594827cdee268125feb3aed1c3bad77d0f6761eb198a', 'ACTIVE');

-- 50 common medicines
INSERT INTO medicine
    (name, company, purchase_price, price, discount_percent, quantity, expiry_date)
VALUES
    ('Dolo 650mg', 'Micro Labs', 21.5, 30.8, 5, 250, '2028-04-30'),
    ('Crocin Advance 500mg', 'GSK', 20, 28, 0, 180, '2028-02-28'),
    ('Combiflam Tablet', 'Sanofi', 32, 44, 10, 140, '2027-11-30'),
    ('Calpol 500mg', 'GSK', 14, 20, 0, 200, '2028-06-30'),
    ('Brufen 400mg', 'Abbott', 30, 42, 5, 90, '2026-09-30'),
    ('Meftal Spas Tablet', 'Blue Cross', 38, 52, 0, 75, '2028-01-31'),
    ('Disprin Tablet', 'Reckitt', 6, 9, 0, 300, '2028-03-31'),
    ('Azithral 500mg', 'Alembic', 78, 108, 10, 60, '2028-05-31'),
    ('Augmentin 625 Duo', 'GSK', 145, 198, 5, 45, '2027-12-31'),
    ('Amoxyclav 625mg', 'Cipla', 120, 165, 8, 49, '2028-02-29'),
    ('Cifran 500mg', 'Cipla', 60, 84, 0, 8, '2027-09-30'),
    ('Zifi 200mg', 'FDC', 95, 132, 10, 40, '2028-04-30'),
    ('Taxim-O 200mg', 'Alkem', 88, 122, 5, 55, '2028-01-31'),
    ('Monocef 200mg', 'Aristo', 105, 145, 0, 6, '2027-12-31'),
    ('Doxy-1 L Forte', 'USV', 45, 62, 0, 80, '2028-03-31'),
    ('Pan-D Capsule', 'Alkem', 130, 178, 10, 120, '2028-05-31'),
    ('Pantop 40mg', 'Aristo', 82, 112, 5, 95, '2028-02-28'),
    ('Omez 20mg', 'Dr Reddys', 58, 80, 0, 110, '2027-11-30'),
    ('Rantac 150mg', 'JB Chemicals', 22, 31, 0, 160, '2028-01-31'),
    ('Digene Gel 200ml', 'Abbott', 105, 145, 8, 65, '2027-10-31'),
    ('Eno Fruit Salt 100g', 'GSK', 90, 125, 0, 85, '2028-06-30'),
    ('Cyclopam Tablet', 'Indoco', 40, 55, 0, 70, '2026-10-05'),
    ('Norflox-TZ Tablet', 'Cipla', 72, 99, 5, 60, '2028-03-31'),
    ('Electral Powder', 'FDC', 15, 22, 0, 240, '2028-08-31'),
    ('Cetzine 10mg', 'GSK', 22, 32, 10, 150, '2028-04-30'),
    ('Allegra 120mg', 'Sanofi', 145, 199, 5, 69, '2028-02-29'),
    ('Montair-LC Tablet', 'Cipla', 155, 215, 8, 55, '2028-01-31'),
    ('Sinarest Tablet', 'Centaur', 62, 86, 0, 90, '2027-11-30'),
    ('Vicks Action 500', 'Procter & Gamble', 40, 55, 0, 130, '2026-07-31'),
    ('Otrivin Nasal Spray', 'GSK', 78, 108, 5, 45, '2027-12-31'),
    ('Benadryl Cough Syrup', 'Johnson & Johnson', 105, 145, 0, 50, '2028-03-31'),
    ('Ascoril LS Syrup 100ml', 'Glenmark', 118, 162, 5, 50, '2027-10-31'),
    ('Glycomet 500mg', 'USV', 32, 45, 0, 100, '2028-02-28'),
    ('Glimestar M2', 'Mankind', 98, 135, 5, 60, '2028-04-30'),
    ('Januvia 50mg', 'MSD', 320, 440, 10, 4, '2028-01-31'),
    ('Amlokind 5mg', 'Mankind', 18, 26, 0, 136, '2028-06-30'),
    ('Telma 40mg', 'Glenmark', 128, 176, 8, 75, '2028-03-31'),
    ('Ecosprin 75mg', 'USV', 8, 12, 0, 200, '2028-05-31'),
    ('Atorva 10mg', 'Zydus', 72, 100, 5, 85, '2028-02-29'),
    ('Thyronorm 50mcg', 'Abbott', 115, 158, 0, 65, '2028-04-30'),
    ('Shelcal 500', 'Torrent', 105, 145, 10, 110, '2028-07-31'),
    ('Zincovit Tablet', 'Apex Labs', 78, 108, 5, 130, '2028-06-30'),
    ('Becosules Capsule', 'Pfizer', 38, 53, 0, 150, '2028-05-31'),
    ('Limcee 500mg', 'Abbott', 22, 31, 0, 175, '2028-08-31'),
    ('Neurobion Forte', 'Merck', 32, 45, 0, 120, '2028-03-31'),
    ('Volini Gel 30g', 'Sun Pharma', 105, 145, 8, 70, '2028-01-31'),
    ('Moov Cream 30g', 'Reckitt', 95, 132, 0, 0, '2027-12-31'),
    ('Betadine Ointment 20g', 'Win-Medicare', 88, 122, 0, 55, '2028-02-28'),
    ('Candid Cream 20g', 'Glenmark', 92, 128, 5, 48, '2028-04-30'),
    ('Soframycin Cream 30g', 'Sanofi', 68, 95, 0, 52, '2028-03-31');
`;
