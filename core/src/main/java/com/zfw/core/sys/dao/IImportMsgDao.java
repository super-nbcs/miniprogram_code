package com.zfw.core.sys.dao;

import com.zfw.core.dao.ICommonDao;
import com.zfw.core.sys.entity.ImportMsg;
import org.springframework.data.jpa.repository.Query;

/**
 * @Author:zfw
 * @Date:2019/8/30
 * @Content:
 */
public interface IImportMsgDao extends ICommonDao<ImportMsg,Integer> {

    ImportMsg findByErrorFileName(String errorFileName);

    ImportMsg findByFileName(String fileName);

    @Query(value = "select count(id) from t_import_error_msg where state=20",nativeQuery = true)
    long countUntreated();
}
