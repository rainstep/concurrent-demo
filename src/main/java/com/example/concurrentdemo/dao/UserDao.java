package com.example.concurrentdemo.dao;

import com.example.concurrentdemo.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {
    @Select("select user_id, user_name, amount from user where user_id = #{userId}")
    @ResultType(User.class)
    User get(int userId);

    @Insert("insert into user(user_name, amount) values(#{userName}, #{amount})")
    int insert(User user);

    @Update("update user set user_name = #{userName}, amount=#{amount} where user_id = #{userId}")
    int update(User user);

    @Update("update user set amount= amount + #{addAmount} where user_id = #{userId}")
    int addMountByUserId(int userId, double addAmount);

    @Update("update user set amount= amount - #{subtractAmount} where user_id = #{userId} and amount >= #{subtractAmount}")
    int subtractMountByUserId(int userId, double subtractAmount);
}
