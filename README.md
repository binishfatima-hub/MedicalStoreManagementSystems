# MediCare — Medical Store Management System

A college project with **two separate panels** sharing **one MySQL database**:

| | Admin Panel | User Panel |
|---|---|---|
| Who | Store owner / staff | Customers |
| Login with | username + password | email + password |
| Database table | `admin` | `users` |
| Can register online? | No (created in the database) | Yes |
| Can do | Add / edit / delete medicines, update stock, manage users, read stock &amp; sales reports | Register, browse medicines, cart, checkout, printable bill, own order history |

Nothing on the website is dummy data — every number is read live from MySQL.

---

## 1. Folder structure

```
MedicalStoreManagementSystems/
│
├── database/
│   └── schema.sql                  ← run this ONCE to create the database
│
├── MedicareBackend/                ← Java + Spring Boot REST API (port 8080)
│   └── src/main/java/com/example/medicarebackend/
│       ├── model/                  Admin, User, Medicine, Order   (= the tables)
│       ├── repository/             the database queries
│       ├── service/                all the rules and calculations
│       ├── controller/             the web addresses (/api/...)
│       ├── dto/                    the shapes sent to / from the browser
│       ├── util/                   PasswordUtil (SHA-256 hashing)
│       └── config/                 CorsConfig
│
├── MediCareWebsite/                ← the website (HTML + CSS + JS)
│   ├── index.html                  landing page: User Panel or Admin Panel
│   │
│   ├── assets/
│   │   ├── css/style.css           ONE stylesheet for the whole website
│   │   └── js/
│   │       ├── api.js              every backend call goes through here
│   │       ├── session.js          who is logged in (user / admin)
│   │       ├── common.js           money, dates, badges, alerts
│   │       └── cart.js             the shopping cart
│   │
│   ├── user/                       ===== USER PANEL =====
│   │   ├── register.html           create an account
│   │   ├── login.html              login with email
│   │   ├── home.html               user dashboard
│   │   ├── medicines.html          browse + add to cart
│   │   ├── cart.html               the cart
│   │   ├── checkout.html           place the order
│   │   ├── bill.html               printable GST bill
│   │   ├── my-orders.html          own order history
│   │   ├── profile.html            edit details + change password
│   │   ├── about.html
│   │   └── contact.html
│   │
│   └── admin/                      ===== ADMIN PANEL =====
│       ├── login.html              admin login
│       ├── dashboard.html          live stock + sales numbers
│       ├── medicines.html          ADD / EDIT / DELETE / UPDATE STOCK
│       ├── stock-report.html       full inventory report
│       ├── sales-report.html       bills, top sellers, day-wise sales
│       ├── users.html              registered users, block / delete
│       └── profile.html            admin details + change password
│
└── MedicalStoreManagementSystem/   ← Java Swing desktop app (optional extra)
                                       its login now also checks the `admin`
                                       table, so there is ONE admin password
                                       for the whole project
```

---

## 2. How to run it

> **Copied this project from GitHub?** The code comes with it, but the
> **database does not** — a MySQL database lives on a computer, not in Git.
> That is exactly what Step 1 below is for: it builds your own empty copy
> of the database on your machine.

### Step 1 — Create the database

Open **MySQL Workbench** (or a terminal) and run `database/schema.sql`.

From a terminal:

```bash
mysql -u root -p < database/schema.sql
```

This creates the `medical_store` database with the tables
`admin`, `users`, `medicine`, `orders`, and inserts the default admin.

**Optional** — to start with 50 common medicines already in stock instead
of typing them in one by one:

```bash
mysql -u root -p < database/seed-data.sql
```

### Step 2 — Point the backend at your MySQL

Open `MedicareBackend/src/main/resources/application.properties` and change
the username / password to your own MySQL login:

```properties
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

### Step 3 — Start the backend

In IntelliJ, run `MedicareBackendApplication`. Or from a terminal:

```bash
cd MedicareBackend
mvnw spring-boot:run
```

Wait for **"Started MedicareBackendApplication"**. It runs on `http://localhost:8080`.

### Step 4 — Open the website

Once the backend is running, just open this in your browser:

**<http://localhost:8080>**

That is all. The backend hands out the HTML/CSS/JS files as well, so one
running program gives you both the website and the API.

> Do **not** open the .html files by double-clicking them. A page opened as
> `file://...` is blocked by the browser from calling the backend, so every
> page would show "Cannot reach the MediCare backend".
>
> (If you prefer, right-click `MediCareWebsite/index.html` in IntelliJ and
> choose **Open in Browser** — that works too, because it also serves the
> page over `http://`.)

---

## 2b. Putting this project on GitHub

**What goes to GitHub:** all the code, plus `database/schema.sql` and
`database/seed-data.sql`.

**What does NOT go to GitHub:** the MySQL database itself. Git stores files,
and a database is not a file in your project — it lives inside MySQL on your
computer.

That sounds like a problem, because the **logins are in the database too**.
It is not, because the two SQL files rebuild everything:

| Table | Who fills it after a clone | Works straight away? |
|---|---|---|
| `admin` | `schema.sql` inserts it | **Yes** — `admin` / `admin123` |
| `users` | `seed-data.sql` adds 2 samples, or people register themselves | **Yes** — or register a new one |
| `medicine` | `seed-data.sql` adds 50 medicines | **Yes** |
| `orders` | starts empty, fills up as bills are made | empty, by design |

The password column never holds the plain password — it holds a SHA-256 hash.
The SQL files use MySQL's `SHA2('admin123', 256)`, which produces exactly the
same hash as the Java code in `PasswordUtil.java`, so these logins really work.

Your own data (the medicines you edited, customers who registered on your
machine, bills you generated) stays on **your** computer only.

### Before you push

Open `MedicareBackend/src/main/resources/application.properties` and look at
this line:

```properties
spring.datasource.password=fatima
```

That is your real MySQL password, and it will be visible to everyone who can
see the repository. If you use that password anywhere else, replace it with a
placeholder before pushing:

```properties
spring.datasource.password=CHANGE_THIS_TO_YOUR_MYSQL_PASSWORD
```

...and put your real one back when you want to run it locally.

### Commands

```bash
git init
git add .
git commit -m "MediCare - Medical Store Management System"
git branch -M main
git remote add origin https://github.com/YOUR-USERNAME/YOUR-REPO.git
git push -u origin main
```

The `.gitignore` file already keeps `target/`, `out/`, `.idea/` and `.iml`
files out, so only the real source code is uploaded.

### What the other person does after cloning

1. install MySQL, then run the two SQL files:
   ```bash
   mysql -u root -p < database/schema.sql
   mysql -u root -p < database/seed-data.sql
   ```
2. put their own MySQL password in `application.properties`
3. open `MedicareBackend/pom.xml` in IntelliJ → **Add as Maven Project**
4. run `MedicareBackendApplication`
5. open <http://localhost:8080> and log in with

   | Panel | Login |
   |---|---|
   | Admin | `admin` / `admin123` |
   | User | `priya@gmail.com` / `priya123` |
   | User | `rahul@gmail.com` / `rahul123` |

This was tested by building a brand-new empty database from these two files
and logging in with all three accounts — they work.

---

## 3. Logins

**Admin panel** — `admin/login.html`

```
username : admin
password : admin123
```

Change it from **Admin Profile → Change Admin Password** after the first login.

**User panel** — `user/register.html`

There is no admin account and no user account beyond the one above; register
your own customer account from the register page (name, email, mobile,
password, confirm password). No email verification — you can login immediately.

---

## 4. Database tables

### `admin`
| Column | Type | Note |
|---|---|---|
| id | INT | primary key |
| name | VARCHAR(100) | |
| username | VARCHAR(50) | **login id**, unique |
| email | VARCHAR(100) | unique |
| mobile | VARCHAR(15) | |
| password | VARCHAR(100) | SHA-256 hash |
| created_at | DATETIME | |

### `users`
| Column | Type | Note |
|---|---|---|
| id | INT | primary key |
| name | VARCHAR(100) | |
| email | VARCHAR(100) | **login id**, unique |
| mobile | VARCHAR(15) | 10 digits |
| password | VARCHAR(100) | SHA-256 hash |
| status | VARCHAR(20) | ACTIVE / BLOCKED |
| created_at | DATETIME | |

### `medicine`
| Column | Meaning |
|---|---|
| id, name, company | |
| **purchase_price** | what the STORE paid the supplier (cost) |
| **price** | the MRP charged to the customer (selling price) |
| **discount_percent** | how much off the MRP the customer gets |
| quantity, expiry_date | |

The customer finally pays `price - (price x discount_percent / 100)`.
The backend **refuses to save** a medicine where that final amount falls
below `purchase_price`, so the store can never sell at a loss.

### `orders`
One row = one medicine line of a bill. All lines of one bill share the same
`bill_number`.

| Column | Note |
|---|---|
| bill_number | e.g. `BILL-20260906-162120` |
| user_id, user_email | which account placed it (empty = counter sale) |
| customer_name, mobile | |
| medicine_id, medicine_name, quantity | |
| **purchase_price** | cost at the time of sale, for the profit report |
| **price** | MRP at the time of sale |
| **discount_percent**, **discount** | the discount that was applied |
| subtotal, gst, total | see the formula below |
| order_date | |

Prices are **copied** into the bill at the moment of sale, so an old bill
still shows what was actually charged even if the admin changes the price later.

One bill line is calculated like this:

```
subtotal = price x quantity          (MRP, before discount)
discount = subtotal x discount_percent / 100
taxable  = subtotal - discount       (what the customer pays for goods)
gst      = taxable x 5%
total    = taxable + gst
profit   = taxable - (purchase_price x quantity)
```

---

## 5. API endpoints

| Method | Path | Used by |
|---|---|---|
| POST | `/api/auth/admin/login` | admin login |
| POST | `/api/auth/user/register` | user registration |
| POST | `/api/auth/user/login` | user login |
| GET | `/api/medicines` | both panels |
| GET | `/api/medicines/search?keyword=` | search |
| POST | `/api/medicines` | admin — add |
| PUT | `/api/medicines/{id}` | admin — edit |
| DELETE | `/api/medicines/{id}` | admin — delete |
| PUT | `/api/medicines/{id}/stock?action=ADD\|REMOVE\|SET&amount=` | admin — stock update |
| POST | `/api/orders` | checkout |
| GET | `/api/orders/bills` | admin — all bills |
| GET | `/api/orders/bills/user/{userId}` | user — my orders |
| GET | `/api/orders/bills/{billNumber}` | printable bill |
| DELETE | `/api/orders/bills/{billNumber}` | admin — delete bill |
| GET | `/api/users` | admin — user list |
| PUT | `/api/users/{id}` | user — edit profile |
| PUT | `/api/users/{id}/password` | user — change password |
| PUT | `/api/users/{id}/status?status=` | admin — block / unblock |
| DELETE | `/api/users/{id}` | admin — delete user |
| GET | `/api/admins/{id}` , `PUT /api/admins/{id}` , `PUT /api/admins/{id}/password` | admin profile |
| GET | `/api/reports/dashboard` | admin dashboard |
| GET | `/api/reports/stock` | stock report |
| GET | `/api/reports/sales?from=&to=` | sales report |

---

## 6. Things worth knowing (good for the viva)

- **Passwords are never stored as plain text.** They are hashed with SHA-256
  (`util/PasswordUtil.java`). The same password always gives the same hash, so
  at login we hash what was typed and compare the two hashes.
- **The price always comes from the database.** The checkout page only sends
  the medicine id and the quantity, so nobody can change a price from the browser.
- **Placing an order is one transaction** (`@Transactional` in `OrderService`).
  Either every line is saved and every stock reduced, or nothing happens —
  half a bill can never be saved.
- **Stock is checked before anything is saved**, and the same medicine appearing
  twice in a cart is added up before the check.
- **`ddl-auto=none`** means Hibernate never changes the tables. The tables come
  only from `database/schema.sql`, so the database stays exactly as designed.
- **Three different prices, never mixed up.** `purchase_price` is the cost,
  `price` is the MRP, and `discount_percent` is what comes off the MRP.
  The admin form shows a live green/red box with the profit per unit while
  you type, so a mistake is visible before saving.
- **The store can never sell at a loss.** Two rules are checked, and both are
  written twice on purpose - once in the browser (`priceProblem()` in
  `admin/medicines.html`, which greys out the Save button) and once on the
  server (`validate()` in `MedicineService.java`, which rejects the request).
  The browser copy gives instant feedback; the server copy is the one that
  actually protects the data, because a browser check can be bypassed.
    1. selling price cannot be below the purchase price, and
    2. the discount cannot pull the final price below the purchase price -
       the error even tells the admin the highest discount that is allowed.
- **The discount is per medicine, set by the admin.** The bill is created in
  the user panel where the admin is not present, so a discount typed at
  checkout would make no sense. Instead the admin sets a % on each medicine
  and it applies automatically to every sale of it.
- **Profit is never guessed.** If a sold line has no purchase price recorded
  (an old bill, or a medicine whose cost was left at 0), the reports show
  `COST NOT SET` and leave that line OUT of the profit total, with a note
  saying how many lines were skipped. Showing the full selling price as
  "profit" would be wrong.
- **One program runs everything.** `spring.web.resources.static-locations`
  in `application.properties` points Spring Boot at the `MediCareWebsite`
  folder, so the same server gives out the web pages and the API. That is
  why there is no second web server to start, and why the pages and the API
  are on the same address (no cross-origin problem).
- **Low stock** = 10 units or less. **Expiring soon** = within 30 days.
  Both are calculated in `Medicine.java`.
- **No password is hardcoded anywhere.** The Swing desktop app
  (`MedicalStoreManagementSystem`) used to accept `admin` / `1234` written in
  the Java file; it now reads the same `admin` row from MySQL and compares the
  same SHA-256 hash, so changing the password in the admin panel changes it for
  the desktop app too.
