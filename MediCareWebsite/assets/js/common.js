/* ============================================================
   common.js  -  small helpers used by many pages:
   money / date formatting, coloured badges, alerts and toasts.
   ============================================================ */

/** 1234.5  ->  "Rs. 1,234.50" */
function money(value) {

    const number = Number(value || 0);

    return "Rs. " + number.toLocaleString("en-IN", {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    });
}

/** "2027-12-31"  ->  "31 Dec 2027" */
function formatDate(value) {

    if (!value) {
        return "-";
    }

    const date = new Date(value);

    if (isNaN(date)) {
        return value;
    }

    return date.toLocaleDateString("en-IN", {
        day: "2-digit",
        month: "short",
        year: "numeric"
    });
}

/** "2026-09-06T14:32:10"  ->  "06 Sep 2026, 02:32 pm" */
function formatDateTime(value) {

    if (!value) {
        return "-";
    }

    const date = new Date(value);

    if (isNaN(date)) {
        return value;
    }

    return date.toLocaleString("en-IN", {
        day: "2-digit",
        month: "short",
        year: "numeric",
        hour: "2-digit",
        minute: "2-digit"
    });
}

/** Today as "2026-09-06" - used to fill date inputs. */
function todayString() {

    const date = new Date();

    const month = String(date.getMonth() + 1).padStart(2, "0");
    const day = String(date.getDate()).padStart(2, "0");

    return date.getFullYear() + "-" + month + "-" + day;
}

/** Removes < and > so a medicine name can never break the page. */
function safe(text) {

    if (text === null || text === undefined) {
        return "";
    }

    return String(text)
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;");
}


/* ---------------- stock / expiry badges ---------------- */

/** Green / orange / red badge for the quantity left. */
function stockBadge(quantity) {

    const q = Number(quantity || 0);

    if (q <= 0) {
        return '<span class="badge red">OUT OF STOCK</span>';
    }

    if (q <= 10) {
        return '<span class="badge orange">LOW STOCK (' + q + ')</span>';
    }

    return '<span class="badge green">IN STOCK (' + q + ')</span>';
}

/** Badge for the expiry date. */
function expiryBadge(expiryDate) {

    if (!expiryDate) {
        return '<span class="badge grey">NO DATE</span>';
    }

    const expiry = new Date(expiryDate);
    const today = new Date();

    const thirtyDays = new Date();
    thirtyDays.setDate(today.getDate() + 30);

    if (expiry < today) {
        return '<span class="badge red">EXPIRED</span>';
    }

    if (expiry <= thirtyDays) {
        return '<span class="badge orange">EXPIRING SOON</span>';
    }

    return '<span class="badge green">OK</span>';
}


/* ---------------- messages ---------------- */

/**
 * Shows the coloured message box of a page.
 *   showAlert("alertBox", "Medicine added", "success")
 *   type = "success" | "error" | "info"
 */
function showAlert(elementId, message, type) {

    const box = document.getElementById(elementId);

    if (!box) {
        return;
    }

    box.className = "alert show " + (type || "info");
    box.textContent = message;
}

function hideAlert(elementId) {

    const box = document.getElementById(elementId);

    if (box) {
        box.className = "alert";
    }
}

/** Small popup at the bottom of the screen. */
function toast(message, type) {

    let box = document.getElementById("toast");

    if (!box) {
        box = document.createElement("div");
        box.id = "toast";
        document.body.appendChild(box);
    }

    box.textContent = message;
    box.className = "show " + (type || "");

    clearTimeout(window.__toastTimer);

    window.__toastTimer = setTimeout(function () {
        box.className = "";
    }, 2600);
}


/** 2 decimal places, as a number. 27.000000001 -> 27 */
function round2(value) {
    return Math.round(Number(value || 0) * 100) / 100;
}

/** Drops useless zeros so a percent reads nicely: 10.0 -> "10",  12.5 -> "12.5" */
function trimNumber(value) {

    const number = round2(value);

    return Number.isInteger(number) ? String(number) : String(number);
}

/** Badge for the discount percent, used in a few tables. */
function discountBadge(percent) {

    const p = Number(percent || 0);

    if (p <= 0) {
        return '<span style="color:#6b7b90;">-</span>';
    }

    return '<span class="badge blue">' + trimNumber(p) + '% OFF</span>';
}
