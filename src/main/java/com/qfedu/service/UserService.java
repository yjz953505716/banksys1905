package com.qfedu.service;

import com.qfedu.entity.User;

public interface UserService {

    public User login(String bankCode, String password);

    public Double findBalance(String bankCode);
    public Double findBalance2(String bankCode);

    public void updatePass(String code,String password,String newPass,String newTowPass);
}
