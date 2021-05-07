package com.example.testfirebase.registration;

public class Employee {

    private String name;
    private String email;
    private String password;
    private String post;

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean anyFieldIsEmpty(){
        return name.isEmpty() || email.isEmpty() || password.isEmpty();
    }
}
