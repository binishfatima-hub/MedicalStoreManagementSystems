/* ============================================================
   api.js  -  every call to the Spring Boot backend goes
   through this file, so the address is written only once.
   ============================================================ */

/* If you run the backend on a different port, change it here only. */
const API_BASE = "http://localhost:8080";


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
