package com.example.start.utils;

import androidx.annotation.Nullable;

public class Resource<T> {
    public Status status;
    public T data;
    public String message;

    public Resource() {

    }

    public Resource(Status status,@Nullable T data, @Nullable String message) {
        this.data = data;
        this.status = status;
        this.message = message;
    }

    public Resource<T> success(T data) {
        return new Resource<T>(Status.Success, data, null);
    }

    public Resource<T> error(T data, String message) {
        return new Resource<T>(Status.Error, data, message);
    }

    public Resource<T> loading(T data) {
        return new Resource<T>(Status.Loading, data, null);
    }
}
