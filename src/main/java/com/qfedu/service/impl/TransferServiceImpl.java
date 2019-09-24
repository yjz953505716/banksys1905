package com.qfedu.service.impl;

import com.github.pagehelper.PageHelper;
import com.qfedu.dao.TransferDao;
import com.qfedu.dao.UserDao;
import com.qfedu.entity.Transfer;
import com.qfedu.entity.User;
import com.qfedu.service.TransferService;
import com.qfedu.vo.TransferVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TransferServiceImpl implements TransferService {

    @Autowired
    private TransferDao transferDao;

    @Autowired
    private UserDao userDao;

    @Override
    public void transfer(String sourceCode, String descCode, Double money) {

        User sourceUser = userDao.findByCode(sourceCode);
        User user = userDao.findByCode(descCode);

        if(sourceCode.equals(descCode)){
            throw new RuntimeException("转账卡号不能相同");
        }
        if(user == null){
            throw new RuntimeException("转账卡号输入有误");
        }
        if(sourceUser.getBalance() < money){
            throw new RuntimeException("余额不足");
        }

        Transfer sTransfer = new Transfer();
        sTransfer.setUid(sourceUser.getUid());
        sTransfer.setMoney(0 - money);
        sTransfer.setBalance(sourceUser.getBalance() - money);
        transferDao.add(sTransfer);
        //同时对user表里的数据进行修改
        sourceUser.setBalance(sourceUser.getBalance() - money);
        userDao.updateUser(sourceUser);

        Transfer dTransfer = new Transfer();
        dTransfer.setUid(user.getUid());
        dTransfer.setMoney(money);
        dTransfer.setBalance(user.getBalance() + money);
        transferDao.add(dTransfer);
        user.setBalance(user.getBalance()+money);
        userDao.updateUser(user);
    }

    @Override
    public List<TransferVO> findAllTransferInfo(Integer uid, Date beginTime, Date endTime,Integer page,Integer limit) {

        // 设置查询的页码和每页显示的记录数
        // 该语句后面，一定要紧跟着查询用的方法
        PageHelper.startPage(page,limit);
        List<TransferVO> list = transferDao.findAll(uid, beginTime, endTime);
        return list;
    }
}
