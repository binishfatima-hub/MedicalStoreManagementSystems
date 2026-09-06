/* ============================================================
   api.js  -  every call to the Spring Boot backend goes
   through this file, so the address is written only once.
   ============================================================ */

/*
 * Where is the backend?
 *
 * The backend serves these pages itself, so normally the API is on the
 * SAME address as the page you are looking at. Working that out at run
 * time means the same files run on your computer AND on the internet
 * without editing anything.
 *
 *   http://localhost:8080/user/login.html   ->  http://localhost:8080
 *   https://medicare.onrender.com/...       ->  https://medicare.onrender.com
 *
 * The two special cases below are for when the page did NOT come from
 * the backend - opening the .html file straight from the folder, or
 * using a separate preview server. Then we fall back to the usual
 * local address of the backend.
 */
const BACKEND_FALLBACK = "http://localhost:8080";

const API_BASE = (function () {

    // The page was opened straight from the folder (file:///C:/...).
    // There is no server behind it, so use the usual local address.
    if (window.location.protocol === "file:") {
        return BACKEND_FALLBACK;
    }

    // IntelliJ's built-in preview always uses port 63342. That server
    // only sends files, it has no API, so point at the backend instead.
    if (window.location.port === "63342") {
        return BACKEND_FALLBACK;
    }

    // Normal case: the backend itself served this page, so the API is
    // on exactly the same address - whatever the port or domain is.
    //
    //   http://localhost:8080/...        ->  http://localhost:8080
    //   http://localhost:9999/...        ->  http://localhost:9999
    //   https://medicare.onrender.com/.. ->  https://medicare.onrender.com
    //
    // This is why the same files work on your computer and online
    // without changing anything.
    return window.location.origin;
})();


/**
 * Sends a request to the backend and returns the JSON answer.
 *
 *   await apiGet("/api/medicines")
 *   await apiPost("/api/auth/user/login", { email, password })
 *   await apiPut("/api/medicines/5", medicine)
 *   await apiDelete("/api/medicines/5")
 */
async function apiRequest(path, method, body) {

    const options = {
        method: method,
        headers: { "Content-Type": "application/json" }
    };

    if (body !== undefined) {
        options.body = JSON.stringify(body);
    }

    let response;

    try {
        response = await fetch(API_BASE + path, options);

    } catch (error) {
        // fetch itself failed -> the backend is not running
        throw new Error(
            "Cannot reach the MediCare backend on " + API_BASE +
            ". Please start the MedicareBackend application first."
        );
    }

    const text = await response.text();

    let data = null;

    if (text) {
        try {
            data = JSON.parse(text);
        } catch (error) {
            data = text;
        }
    }

    if (!response.ok) {
        const message =
            (data && data.message) ? data.message : "Request failed";
        throw new Error(message);
    }

    return data;
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
