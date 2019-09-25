package com.example.volleycrudexample;

import java.util.Comparator;

public class UserIdComparator implements Comparator<UserModel> {
    @Override
    public int compare(UserModel userModel, UserModel t1) {
        int value = t1.getUserId() - userModel.getUserId();
        return value;
    }
}
