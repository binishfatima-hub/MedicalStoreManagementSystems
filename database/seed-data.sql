-- ============================================================
--  MediCare - sample medicine stock (50 common medicines)
-- ============================================================
--  OPTIONAL. Run this only if you want the store to start with
--  medicines already in it, instead of adding them by hand.
--
--  Run database/schema.sql FIRST, then this file:
--      mysql -u root -p < database/seed-data.sql
--
--  purchase_price = what the store paid  (cost)
--  price          = MRP charged to the customer
--  discount_percent = % off the MRP
--
--  A few rows are deliberately low stock / out of stock /
--  expiring / expired, so the dashboard alerts and the
--  reports actually have something to show.
-- ============================================================

USE medical_store;

-- start from a clean medicine table
DELETE FROM medicine;
ALTER TABLE medicine AUTO_INCREMENT = 1;

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


-- ============================================================
--  SAMPLE CUSTOMER LOGINS
-- ============================================================
--  The ADMIN login is not here - it is already created by
--  database/schema.sql (username: admin, password: admin123).
--
--  Normally customers create their own account from the
--  Register page. These two are only so that the user panel
--  can be opened straight away for a demo.
--
--      priya@gmail.com   password: priya123
--      rahul@gmail.com   password: rahul123
--
--  SHA2(...,256) is MySQL's SHA-256, exactly the same hash the
--  Java code makes, so these logins really work. The plain
--  password is never stored - only the hash.
-- ============================================================

DELETE FROM users WHERE email IN ('priya@gmail.com', 'rahul@gmail.com');

INSERT INTO users (name, email, mobile, password, status)
VALUES
    ('Priya Sharma', 'priya@gmail.com', '9876543210',
     SHA2('priya123', 256), 'ACTIVE'),

    ('Rahul Verma',  'rahul@gmail.com', '9123456780',
     SHA2('rahul123', 256), 'ACTIVE');


-- ============================================================
--  Check what was created
-- ============================================================
SELECT COUNT(*) AS medicines_added FROM medicine;
SELECT COUNT(*) AS customer_logins  FROM users;
SELECT COUNT(*) AS admin_logins     FROM admin;
