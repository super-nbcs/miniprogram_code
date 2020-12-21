package com.zfw.core.sys.service;

import com.zfw.core.service.ICommonService;
import com.zfw.core.sys.entity.ImportMsg;

/**
 * @Author:zfw
 * @Date:2019/8/30
 * @Content:
 */
public interface IImportMsgService extends ICommonService<ImportMsg,Integer> {

    ImportMsg findByErrorFileName(String errorFileName);
    ImportMsg findByFileName(String fileName);

    long countUntreated();
}
