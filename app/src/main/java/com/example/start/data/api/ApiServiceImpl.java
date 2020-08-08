package com.example.start.data.api;

import com.example.start.data.model.UserModal;
import com.rx2androidnetworking.Rx2AndroidNetworking;

import io.reactivex.Single;


public class ApiServiceImpl implements ApiService {
    @Override
    public Single<UserModal> login() {
        return Rx2AndroidNetworking.post("")
                .build()
                .getObjectSingle(UserModal.class);
    }
}