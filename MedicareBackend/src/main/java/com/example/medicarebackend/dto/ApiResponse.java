package com.example.medicarebackend.dto;

/**
 * Every API sends back the same simple shape, so the JavaScript on the
 * website always knows what to look for:
 *
 *   { "success": true,  "message": "Login successful", "data": { ... } }
 *   { "success": false, "message": "Wrong password",   "data": null }
 */
public class ApiResponse {

    private boolean success;
    private String message;
    private Object data;

    public ApiResponse(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static ApiResponse ok(String message) {
        return new ApiResponse(true, message, null);
    }

    public static ApiResponse ok(String message, Object data) {
        return new ApiResponse(true, message, data);
    }

    public static ApiResponse fail(String message) {
        return new ApiResponse(false, message, null);
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }
}
