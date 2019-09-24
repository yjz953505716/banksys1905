package com.qfedu.controller;

import com.github.pagehelper.Page;
import com.qfedu.common.JsonResult;
import com.qfedu.entity.User;
import com.qfedu.service.TransferService;
import com.qfedu.utils.StrUtils;
import com.qfedu.vo.TransferVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class TransferController {

    @Autowired
    private TransferService transferService;


    @RequestMapping("/transfer.do")
    public JsonResult transfer(String otherCode, Double money, HttpSession session){

        User user = (User)session.getAttribute(StrUtils.LOGIN_INFO);
        if(user == null){
            throw new RuntimeException("还未登陆");
        }

        transferService.transfer(user.getBankCode(), otherCode, money);

        return new JsonResult(0, "转账成功");

    }

    @RequestMapping("/transferInfo.do")
    public Map<String,Object> listTransferInfo(@DateTimeFormat(pattern = "yyyy-MM-dd") Date beginTime, @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime, Integer page, Integer limit, HttpSession session){
        Map<String, Object> map = new HashMap<>();
        User user = (User) session.getAttribute(StrUtils.LOGIN_INFO);
        if (user == null){
            map.put("code",1);
            map.put("msg","未登录");
            return map;
        }
        List<TransferVO> voList = transferService.findAllTransferInfo(user.getUid(), beginTime, endTime,page,limit);
        map.put("code", 0);
        //总记录数
        map.put("count",((Page)voList).getTotal());
        map.put("data",voList);
        return map;
    }

    // @ExceptionHandler 修饰处理异常的方法
    // 注解的参数表示要处理的异常，注解修饰的方法，要使用异常作为参数
//    @ExceptionHandler(Exception.class)
//    public JsonResult exception(Exception ex){
//
//        return new JsonResult(1, ex.getMessage());
//    }
}
