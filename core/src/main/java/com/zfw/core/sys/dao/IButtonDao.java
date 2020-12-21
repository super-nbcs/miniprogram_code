package com.zfw.core.sys.dao;


import com.zfw.core.dao.ICommonDao;
import com.zfw.core.sys.entity.Button;

public interface IButtonDao extends ICommonDao<Button,Integer> {
    boolean existsByBtnSign(String btnSign);
}
