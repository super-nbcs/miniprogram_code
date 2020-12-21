package com.zfw.core.sys.service.impl;

import com.zfw.core.service.impl.CommonServiceImpl;
import com.zfw.core.sys.dao.IButtonDao;
import com.zfw.core.sys.entity.Button;
import com.zfw.core.sys.service.IButtonService;
import com.zfw.core.sys.service.IRoleButtonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ButtonServiceImpl extends CommonServiceImpl<Button, Integer> implements IButtonService {
    private Logger logger= LoggerFactory.getLogger(ButtonServiceImpl.class);
    @Autowired
    private IButtonDao iButtonDao;

    @Autowired
    private IRoleButtonService iRoleButtonService;
    @Override
    public boolean existsByBtnSign(String btnSign) {
        return iButtonDao.existsByBtnSign(btnSign);
    }

    @Override
    @Transactional
    public void deleteBtn(Integer id) {
        iRoleButtonService.deleteAllByBtnId(id);
        this.deleteById(id);
    }
}
