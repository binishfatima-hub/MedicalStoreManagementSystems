-- ============================================================
--  MediCare - Medical Store Management System
--  MySQL Database Schema
-- ============================================================
--  How to run this file:
--      mysql -u root -p < database/schema.sql
--  (or open it in MySQL Workbench and press the lightning bolt)
-- ============================================================

CREATE DATABASE IF NOT EXISTS medical_store;
USE medical_store;


-- ============================================================
--  1. ADMIN TABLE
--  Only the store owner / staff. Admins are NOT registered
--  from the website, they are created here by the developer.
--  Login is done with  username + password.
-- ============================================================
CREATE TABLE IF NOT EXISTS admin (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    username    VARCHAR(50)  NOT NULL UNIQUE,
    email       VARCHAR(100) NOT NULL UNIQUE,
    mobile      VARCHAR(15),
    password    VARCHAR(100) NOT NULL,          -- SHA-256 hash
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;


-- ============================================================
--  2. USERS TABLE (customers)
--  Users register themselves from the website.
--  Login is done with  email + password  (email = user id).
-- ============================================================
CREATE TABLE IF NOT EXISTS users (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    email       VARCHAR(100) NOT NULL UNIQUE,   -- this is the login id
    mobile      VARCHAR(15)  NOT NULL,
    password    VARCHAR(100) NOT NULL,          -- SHA-256 hash
    status      VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;


-- ============================================================
--  3. MEDICINE TABLE (stock)
--  Managed only by the admin panel.
-- ============================================================
--  THREE money fields, do not mix them up:
--    purchase_price    what the STORE paid the supplier  (cost)
--    price             the MRP charged to the customer   (selling price)
--    discount_percent  how much off the MRP the customer gets
--
--  The customer finally pays:  price - (price * discount_percent / 100)
--  The backend refuses to save a medicine where that final amount
--  falls below purchase_price, so the store never sells at a loss.
CREATE TABLE IF NOT EXISTS medicine (
    id                INT AUTO_INCREMENT PRIMARY KEY,
    name              VARCHAR(100) NOT NULL,
    company           VARCHAR(100),
    purchase_price    DOUBLE NOT NULL DEFAULT 0,
    price             DOUBLE,
    discount_percent  DOUBLE NOT NULL DEFAULT 0,
    quantity          INT,
    expiry_date       DATE
) ENGINE=InnoDB;


-- ============================================================
--  4. ORDERS TABLE (sales)
--  One row = one medicine line inside a bill.
--  All rows of the same bill share the same bill_number.
-- ============================================================
--  The prices are COPIED here at the moment of the sale, so an old
--  bill still shows what was actually charged even if the admin
--  changes the medicine's price tomorrow.
--
--  One line is calculated like this:
--    subtotal = price x quantity              (MRP, before discount)
--    discount = subtotal x discount_percent / 100
--    taxable  = subtotal - discount
--    gst      = taxable x 5%
--    total    = taxable + gst
--    profit   = taxable - (purchase_price x quantity)
CREATE TABLE IF NOT EXISTS orders (
    id                INT AUTO_INCREMENT PRIMARY KEY,
    bill_number       VARCHAR(50)  NOT NULL,
    user_id           INT          NULL,        -- which user bought it
    user_email        VARCHAR(100) NULL,
    customer_name     VARCHAR(100) NOT NULL,
    mobile            VARCHAR(20)  NOT NULL,
    medicine_id       INT          NOT NULL,
    medicine_name     VARCHAR(100) NOT NULL,
    purchase_price    DOUBLE       NOT NULL DEFAULT 0,   -- cost at sale time
    quantity          INT          NOT NULL,
    price             DOUBLE       NOT NULL,             -- MRP at sale time
    discount_percent  DOUBLE       NOT NULL DEFAULT 0,
    subtotal          DOUBLE       NOT NULL,             -- before discount
    discount          DOUBLE       NOT NULL DEFAULT 0,
    gst               DOUBLE       NOT NULL,
    total             DOUBLE       NOT NULL,
    order_date        DATETIME     NOT NULL
) ENGINE=InnoDB;


-- ============================================================
--  5. UPGRADE for an older database
--  If the orders table already existed without the user
--  columns, these two lines add them. Ignore the error
--  "Duplicate column name" - it only means it is already done.
-- ============================================================
-- ALTER TABLE orders ADD COLUMN user_id    INT          NULL AFTER bill_number;
-- ALTER TABLE orders ADD COLUMN user_email VARCHAR(100) NULL AFTER user_id;
--
-- ALTER TABLE medicine ADD COLUMN purchase_price   DOUBLE NOT NULL DEFAULT 0 AFTER company;
-- ALTER TABLE medicine ADD COLUMN discount_percent DOUBLE NOT NULL DEFAULT 0 AFTER price;
--
-- ALTER TABLE orders ADD COLUMN purchase_price   DOUBLE NOT NULL DEFAULT 0 AFTER medicine_name;
-- ALTER TABLE orders ADD COLUMN discount_percent DOUBLE NOT NULL DEFAULT 0 AFTER price;
-- ALTER TABLE orders ADD COLUMN discount         DOUBLE NOT NULL DEFAULT 0 AFTER subtotal;


-- ============================================================
--  6. DEFAULT ADMIN LOGIN
--  Username : admin
--  Password : admin123
--  Passwords are stored as a SHA-256 hash, never as plain text.
-- ============================================================
INSERT INTO admin (name, username, email, mobile, password)
SELECT 'Store Admin', 'admin', 'admin@medicare.com', '9999999999',
       SHA2('admin123', 256)
WHERE NOT EXISTS (SELECT 1 FROM admin WHERE username = 'admin');


-- ============================================================
--  Useful checks
-- ============================================================
-- SELECT * FROM admin;
-- SELECT * FROM users;
-- SELECT * FROM medicine;
-- SELECT * FROM orders;
