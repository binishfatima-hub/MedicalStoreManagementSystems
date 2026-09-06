/* ============================================================
   session.js  -  remembers who is logged in.

   Two completely separate logins are kept:

     medicareUser   -> the customer  (user panel,  /user)
     medicareAdmin  -> the store admin (admin panel, /admin)

   They are stored separately, so logging out of one panel does
   not log you out of the other.

   sessionStorage is used, which means the login is forgotten
   when the browser tab is closed.
   ============================================================ */

const USER_KEY = "medicareUser";
const ADMIN_KEY = "medicareAdmin";


/* ---------------- USER ---------------- */

function saveUser(user) {
    sessionStorage.setItem(USER_KEY, JSON.stringify(user));
}

function getUser() {

    const raw = sessionStorage.getItem(USER_KEY);

    return raw ? JSON.parse(raw) : null;
}

function logoutUser(loginPage) {
    sessionStorage.removeItem(USER_KEY);
    window.location.href = loginPage || "login.html";
}

/**
 * Put this on every user page:  requireUser();
 * If nobody is logged in, the browser goes back to the login page.
 */
function requireUser(loginPage) {

    const user = getUser();

    if (!user) {
        window.location.href = loginPage || "login.html";
        return null;
    }

    return user;
}


/* ---------------- ADMIN ---------------- */

function saveAdmin(admin) {
    sessionStorage.setItem(ADMIN_KEY, JSON.stringify(admin));
}

function getAdmin() {

    const raw = sessionStorage.getItem(ADMIN_KEY);

    return raw ? JSON.parse(raw) : null;
}

function logoutAdmin(loginPage) {
    sessionStorage.removeItem(ADMIN_KEY);
    window.location.href = loginPage || "login.html";
}

/** Put this on every admin page:  requireAdmin(); */
function requireAdmin(loginPage) {

    const admin = getAdmin();

    if (!admin) {
        window.location.href = loginPage || "login.html";
        return null;
    }

    return admin;
}
