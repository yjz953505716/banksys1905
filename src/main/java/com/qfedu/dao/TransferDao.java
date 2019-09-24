package com.qfedu.dao;

import com.qfedu.entity.Transfer;
import com.qfedu.vo.TransferVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface TransferDao {

    public void add(Transfer transfer);

    public List<TransferVO> findAll(@Param("uid") Integer uid, @Param("beginTime") Date beginTime,@Param("endTime") Date endTime);
}
