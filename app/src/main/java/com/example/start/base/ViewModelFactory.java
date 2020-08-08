package com.example.start.base;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.start.data.api.ApiHelper;
import com.example.start.data.repository.LoginRepository;
import com.example.start.pages.login.LoginViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private ApiHelper apiHelper;
    public ViewModelFactory(ApiHelper apiHelper) {
        this.apiHelper = apiHelper;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T)(new LoginViewModel(new LoginRepository(apiHelper)));
        }
        throw new IllegalArgumentException("不认识的viewModel名字");
    }
}
