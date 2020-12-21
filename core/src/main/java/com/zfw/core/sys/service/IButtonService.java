package com.zfw.core.sys.service;

import com.zfw.core.service.ICommonService;
import com.zfw.core.sys.entity.Button;

public interface IButtonService extends ICommonService<Button,Integer> {
    boolean existsByBtnSign(String btnSign);

    void deleteBtn(Integer id);
}
