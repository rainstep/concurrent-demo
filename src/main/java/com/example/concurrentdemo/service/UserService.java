package com.example.concurrentdemo.service;

public interface UserService {
    double getAmountByUserId(int userId);

    boolean addMountByUserId(int userId, double addAmount);

    boolean saveAddMountByUserId(int userId, double addAmount);

    boolean subtractMountByUserId(int userId, double subtractAmount);

    boolean saveSubtractMountByUserId(int userId, double subtractAmount);
}
