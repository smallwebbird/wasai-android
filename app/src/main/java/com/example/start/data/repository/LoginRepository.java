package com.example.start.data.repository;

import com.example.start.data.api.ApiHelper;
import com.example.start.data.model.UserModal;

import io.reactivex.Single;

public class LoginRepository {
    private ApiHelper apiHelper;
    public LoginRepository (ApiHelper apiHelper) {
        this.apiHelper = apiHelper;
    }
    public Single<UserModal> login () {
        return apiHelper.login();
    }
}
