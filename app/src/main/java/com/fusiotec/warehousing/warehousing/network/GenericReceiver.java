package com.fusiotec.warehousing.warehousing.network;

/**
 * Created by Owner on 3/22/2017.
 */

public class GenericReceiver {

    private int success;
    private String data;
    private String message;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }
}