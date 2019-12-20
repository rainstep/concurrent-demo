package com.example.concurrentdemo.service;

import com.example.concurrentdemo.dao.UserDao;
import com.example.concurrentdemo.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserDao userDao;

    @Override
    public double getAmountByUserId(int userId) {
        return userDao.get(userId).getAmount();
    }

    @Override
    public boolean addMountByUserId(int userId, double addAmount) {
        User user = userDao.get(userId);
        Double amount = user.getAmount();
        if (addAmount < 0) {
            logger.error("金额不能为负");
            return false;
        }
        logger.info("amount: {}", amount);
        user.setAmount(amount + addAmount);
        userDao.update(user);
        return true;
    }

    @Override
    public boolean subtractMountByUserId(int userId, double subtractAmount) {
        User user = userDao.get(userId);
        Double amount = user.getAmount();
        if (subtractAmount < 0) {
            logger.error("金额不能为负");
            return false;
        }
        if (amount < subtractAmount) {
            logger.error("金额不足");
            return false;
        }
        user.setAmount(amount - subtractAmount);
        userDao.update(user);
        return true;
    }

    @Override
    public boolean saveAddMountByUserId(int userId, double addAmount) {
        int result = userDao.addMountByUserId(userId, addAmount);
        return result > 0;
    }

    @Override
    public boolean saveSubtractMountByUserId(int userId, double subtractAmount) {
        int result = userDao.subtractMountByUserId(userId, subtractAmount);
        return result > 0;
    }
}
