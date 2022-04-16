package com.example.etas_server.dto;

import com.google.gson.Gson;

public class Response {

    private int statusCode;
    private String data;

    public Response() {
    }

    public Response(int statusCode, String data) {
        this.statusCode = statusCode;
        this.data = data;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
