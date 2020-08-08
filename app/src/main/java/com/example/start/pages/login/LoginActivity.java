package com.example.start.pages.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.start.base.ViewModelFactory;
import com.example.start.data.api.ApiHelper;
import com.example.start.data.api.ApiServiceImpl;
import com.example.start.databinding.ActivityLoginBinding;
import com.example.start.R;
import com.example.start.utils.Resource;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {
    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;
    private boolean pwdVisible = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        setViewModel();
        binding.setLoginViewModel(loginViewModel);
        // 只有绑定了这个方法，xml中的数据才会随着liveData数据源变化而变化
        binding.setLifecycleOwner(this);
        // 点击密码眼睛的监听函数
        setEyeClickEvent();
        setButtonClickEvent();
        setNameKeyEvent();
        setPwdKeyEvent();
        setLoginStatusObserver();
    }

    // 设置viewModal
    private void setViewModel() {
        loginViewModel = ViewModelProviders.of(this, new ViewModelFactory(new ApiHelper(new ApiServiceImpl())))
                                        .get(LoginViewModel.class);
    }
    // 设置隐藏展示密码的功能
    private void setEyeClickEvent() {
        binding.loginEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!pwdVisible) {
                    binding.loginEye.setImageResource(R.drawable.login_pwd_visible);
                    binding.loginPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    binding.loginEye.setImageResource(R.drawable.login_pwd_hidden);
                    binding.loginPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                pwdVisible = !pwdVisible;
                binding.loginPwd.setSelection(binding.loginPwd.getText().length());
            }
        });
    }
    // 设置login登录按钮点击之后的
    private void setButtonClickEvent() {
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String loginName = binding.loginName.getText().toString();
                String loginPwd = binding.loginPwd.getText().toString();
                if (loginName == null || loginName.isEmpty()) {
                    binding.loginNameValidate.setVisibility(View.VISIBLE);
                    return;
                }
                if (loginPwd == null || loginPwd.isEmpty()) {
                    binding.loginPwdValidate.setVisibility(View.VISIBLE);
                    return;
                }
                // 执行到这说明密码和账号都不为空，调用viewModal中的处理登录逻辑
                loginViewModel.onLogin();
            }
        });
    }
    // 监听用户输入
    private void setNameKeyEvent() {
        binding.loginName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (count > 0) {
                    binding.loginNameValidate.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    // 监听用户输入
    private void setPwdKeyEvent() {
        binding.loginPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (count > 0) {
                    binding.loginPwdValidate.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    private void setLoginStatusObserver() {
        loginViewModel.login.observe(this, new Observer<Resource<Boolean>>() {
            @Override
            public void onChanged(Resource<Boolean> resource) {
                    System.out.println(resource.status);
                    switch (resource.status) {
                        case Error:
                            Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_LONG).show();
                            break;
                        case Loading:
                            break;
                        case Success:
                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                            break;
                        default:
                            System.out.println("没有此状态");
                    }
            }
        });
    }
}
