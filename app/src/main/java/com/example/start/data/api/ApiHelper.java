package com.example.start.data.api;

import com.example.start.data.model.UserModal;

import io.reactivex.Single;

public class ApiHelper {
    private ApiService apiService;

    public ApiHelper(ApiService apiService) {
        this.apiService = apiService;
    }

    public Single<UserModal> login() {
        return apiService.login();
    }
}
