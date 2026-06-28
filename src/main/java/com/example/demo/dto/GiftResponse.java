package com.example.demo.dto;

public class GiftResponse {

    private boolean success;
    private String message;
    private Object data;

    public GiftResponse() {}

    public GiftResponse(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    // ================= FACTORY =================
    public static GiftResponse success(Object data) {
        return new GiftResponse(true, "OK", data);
    }

    public static GiftResponse fail(String message) {
        return new GiftResponse(false, message, null);
    }

    // ================= GETTER SETTER =================
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}