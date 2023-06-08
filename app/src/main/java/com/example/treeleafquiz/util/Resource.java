package com.example.treeleafquiz.util;



public class Resource<T> {
    private T data;
    private String message;
    private State state;

    public Resource(T data, String message, State state) {
        this.data = data;
        this.message = message;
        this.state = state;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public State getState() {
        return state;
    }

    public enum State {
        LOADING,
        SUCCESS,
        ERROR
    }
}

