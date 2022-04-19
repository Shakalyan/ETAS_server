package com.example.etas_server.dto;

import com.example.etas_server.model.User;

public class Request<T> {

    private User user;
    private T data;

    public Request() {
    }

    public Request(User user, T data) {
        this.user = user;
        this.data = data;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
