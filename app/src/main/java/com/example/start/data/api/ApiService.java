package com.example.start.data.api;

import com.example.start.data.model.UserModal;

import io.reactivex.Single;

public interface ApiService {
    public Single<UserModal> login();
}
