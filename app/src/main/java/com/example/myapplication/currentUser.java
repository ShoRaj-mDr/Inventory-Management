package com.example.myapplication;

public class currentUser {

    public static boolean hasData;
    public static boolean loggingIn;
    public static boolean loggingOut;

    public static boolean admin;
    public static boolean customer;
    public static boolean employee;

    public static String name;
    public static String id;
    public static String phone;
    public static String email;

    public void clearUser(){
        admin=false;
        customer=false;
        employee=false;
        hasData=false;
    }

}
