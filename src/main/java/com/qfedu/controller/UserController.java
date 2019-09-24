package com.qfedu.controller;

import com.qfedu.common.JsonResult;
import com.qfedu.entity.User;
import com.qfedu.service.UserService;
import com.qfedu.utils.StrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login.do")
    public JsonResult login(String code, String password, HttpSession session){

        try {
            User user = userService.login(code, password);
            session.setAttribute(StrUtils.LOGIN_INFO,user);
            return new JsonResult(0,null);
        } catch (Exception e) {
            e.printStackTrace(); //是打印异常的堆栈信息，指明错误原因
            return new JsonResult(1,e.getMessage());
        }

    }

    @RequestMapping("/exitLogin.do")
    public JsonResult exitLogin(HttpSession session){
        session.removeAttribute(StrUtils.LOGIN_INFO);
        session.invalidate();
        return new JsonResult(0,null);
    }


    @RequestMapping("/code.do")
    public JsonResult code(HttpSession session){
        User user = (User) session.getAttribute(StrUtils.LOGIN_INFO);
        if (user == null){
            return new JsonResult(1,"还未登录");
        }
        return new JsonResult(0,user.getBankCode());
    }

    @RequestMapping("/balance.do")
    public JsonResult balance(HttpSession session){
        User user = (User) session.getAttribute(StrUtils.LOGIN_INFO);
        if (user == null){
            return new JsonResult(1,"还未登录");
        }
//        Double balance = userService.findBalance2(user.getBankCode());
        Double balance = user.getBalance();
        return new JsonResult(0,balance);
    }

    @RequestMapping("/upPass.do")
    public JsonResult upPass(String password,String newPass,String newTowPass,HttpSession session){
        User user = (User) session.getAttribute(StrUtils.LOGIN_INFO);
        if (user == null){
            return new JsonResult(1,"还未登录");
        }
        userService.updatePass(user.getBankCode(), password, newPass, newTowPass);
        return new JsonResult(0,"修改成功");
    }
}
