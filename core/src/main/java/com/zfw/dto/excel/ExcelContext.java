package com.zfw.dto.excel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.zfw.core.sys.entity.DictData;
import com.zfw.core.sys.service.IDeptService;
import com.zfw.core.sys.service.IDictDataService;
import com.zfw.core.sys.service.IRoleService;
import com.zfw.core.sys.service.IUserService;
import com.zfw.utils.SpringContextUtils;

/**
 * @Author:zfw
 * @Date:2020-08-11
 * @Content: 导出dto的基类
 */
public class ExcelContext {
    public static final String SUCCESS = "成功";

    @ExcelIgnore
    private IDictDataService iDictDataService = null;
    @ExcelIgnore
    private IUserService userService = null;
    @ExcelIgnore
    private IRoleService roleService = null;
    @ExcelIgnore
    private IDeptService deptService = null;

    public IDeptService getDeptService() {
        if (deptService == null) {
            deptService = SpringContextUtils.getBean(IDeptService.class);
        }
        return deptService;
    }

    public IRoleService getRoleService() {
        if (roleService == null) {
            roleService = SpringContextUtils.getBean(IRoleService.class);
        }
        return roleService;
    }

    public IUserService getUserService() {
        if (userService == null) {
            userService = SpringContextUtils.getBean(IUserService.class);
        }
        return userService;
    }


    private IDictDataService getDictDataService() {
        if (iDictDataService == null) {
            iDictDataService = SpringContextUtils.getBean(IDictDataService.class);
        }
        return iDictDataService;
    }

    /**
     * 通过typeName和value获取DictData
     *
     * @param typeName
     * @param value
     * @return
     */
    protected DictData getDictData(String typeName, String value) {
        DictData dictData = getDictDataService().findByTypeNameAndValue(typeName, value);
        return dictData;
    }


    /**
     * 通过typeName和label获取DictData
     *
     * @param typeName
     * @param label
     * @return
     */
    protected DictData getDictDataByTypeNameAndLabel(String typeName, String label) {
        DictData dictData = getDictDataService().findByTypeNameAndLabel(typeName, label);
        return dictData;
    }

}
