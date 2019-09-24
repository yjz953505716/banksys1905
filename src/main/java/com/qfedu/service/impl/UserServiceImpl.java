package com.qfedu.service.impl;

import com.qfedu.dao.UserDao;
import com.qfedu.entity.User;
import com.qfedu.redis.RedisService;
import com.qfedu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RedisService redisService;

    @Override
    public User login(String bankCode, String password) {
        User user = userDao.findByCode(bankCode);
        if (user == null){
            throw new RuntimeException("卡号错误");
        }
        if (!user.getPassword().equals(password)){
            throw new RuntimeException("密码错误");
        }
        return user;
    }

    @Override
    public Double findBalance(String bankCode) {

        // 先看redis中是否缓存了余额数据，如果有，直接返回该数据；
        // 如果没有，从数据库中查询，将查到数据放到reids缓存里
        Double balance = redisService.getBalance("balance", bankCode);
        if (balance == null){
            User user = userDao.findByCode(bankCode);
            if (user == null){
                throw new RuntimeException("卡号错误");
            }
            Double userBalance = user.getBalance();
            //放入缓存
            redisService.setBalance("balance",bankCode,userBalance);
        }

        return balance;
    }

    @Override
    public Double findBalance2(String bankCode){
        User user = userDao.findByCode(bankCode);
        if (user == null){
            throw new RuntimeException("卡号错误");
        }
        redisService.setBalance("balance2",bankCode,user.getBalance());
        return redisService.getBalance("balance2", bankCode);
    }

    @Override
    public void updatePass(String code,String password,String newPass,String newTowPass) {
        User user = userDao.findByPass(password);
        User byCode = userDao.findByCode(code);

        if (user == null && user != byCode){
            throw new RuntimeException("现密码不对");
        }
        if (!newPass.equals(newTowPass)){
            throw new RuntimeException("再次密码不对");
        }
        user.setPassword(newPass);
        userDao.updateUser(user);
    }
}
