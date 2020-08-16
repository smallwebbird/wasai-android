package com.example.start.pages.login;

import android.content.Intent;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.start.data.model.TestModel;
import com.example.start.data.repository.LoginRepository;
import com.example.start.pages.custombottomNavigation.CustomTabActivity;
import com.example.start.pages.home.HomeFragment;
import com.example.start.utils.CustomApplication;
import com.example.start.utils.Resource;

public class LoginViewModel extends ViewModel {
    public MutableLiveData<Resource<Boolean>> login = new MutableLiveData<>();
    private LoginRepository loginRepository;
    public LoginViewModel (LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }
    // dataBinding绑定的字段
    public MutableLiveData<String> name = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();

    // 处理登录的逻辑
    public void onLogin() {
        Boolean loginStatus = false;
        TestModel testModel = new TestModel();
        Resource<Boolean> resource = new Resource<>();
        System.out.println(name.getValue()+"--"+testModel.name + "--"+password.getValue()+"--"+testModel.password);
        if (name.getValue().equals(testModel.name) && password.getValue().equals(testModel.password)) {
            loginStatus = true;
            login.postValue(resource.success(loginStatus));
            CustomApplication customApplication = CustomApplication.getInstance();
            Intent customerBottomNavigationIntent = new Intent(customApplication, CustomTabActivity.class);
            // 使用application的context
            customerBottomNavigationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            customApplication.startActivity(customerBottomNavigationIntent);
        } else {
            loginStatus = false;
            login.postValue(resource.error(loginStatus, "密码错误"));
        }
    }
}
