package com.example.start.data.model;

public class TestModel {
    public String name = "lzh";
    public String password = "xtlzh1314";

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
    // 让以上俩个属性成为只读
    private void setName(String name) {
        this.name = name;
    }

    private void setPassword(String password) {
        this.password = password;
    }
}
