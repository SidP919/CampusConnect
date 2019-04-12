package com.example.android.campusconnect;

public class User {
    String userName;
    String rollNo;
    String email;
    String userType;

    public User(String userName, String rollNo, String email, String userType) {
        this.userName = userName;
        this.rollNo = rollNo;
        this.email = email;
        this.userType = userType;
    }

    public User() {
    }

    public String getUserName() {
        return userName;
    }

    public String getRollNo() {
        return rollNo;
    }

    public String getEmail() {
        return email;
    }

    public String getUserType() {
        return userType;
    }
}
