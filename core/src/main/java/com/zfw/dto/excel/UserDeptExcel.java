package com.zfw.dto.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.zfw.core.sys.entity.Dept;
import com.zfw.core.sys.service.IDeptService;
import com.zfw.core.sys.service.IUserService;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @Author:zfw
 * @Date:2020-10-09
 * @Content: 批量导入，修改组织架构
 */
@Data
@ContentRowHeight(15)
@HeadRowHeight(20)
@ColumnWidth(20)
public class UserDeptExcel extends ExcelContext{

    @ExcelProperty(index = 0, value = "学(工)号")
    @NumberFormat("#")
    private String userName;

    @ExcelProperty(index = 1, value = "部门(组织架构全路径，参考示例)")
    private String dept;

    @ExcelProperty(index = 2,value = "备注")
    private String remark;

    public void updateDeptCode(UserDeptExcel userDeptExcel){
        IUserService userService = getUserService();
        IDeptService deptService = getDeptService();
        if (StringUtils.isAnyBlank(userDeptExcel.getUserName(),userDeptExcel.getDept())){
            userDeptExcel.setRemark("学工号或部门（组织架构）不能为空，请检查");
            return;
        }
        if (!userService.existsByUserName(userDeptExcel.getUserName())){
            userDeptExcel.setRemark(String.format("系统找不到此学工号【%s】",userDeptExcel.getUserName()));
            return;
        }
        if (!StringUtils.startsWith(userDeptExcel.getDept(),"/")){
            userDeptExcel.setDept("/"+userDeptExcel.getDept());
        }
        if (!deptService.existsByPath(userDeptExcel.getDept())) {
            userDeptExcel.setRemark("不存在此部门，请检查");
            return;
        }
        Dept dept = deptService.findTop1ByPath(userDeptExcel.getDept());
        userService.updateDeptCode(userDeptExcel.getUserName(),dept.getId(),dept.getPath(),dept.getCode());
        userDeptExcel.setRemark(SUCCESS);
    }

    public void updateDeptCodes(List<UserDeptExcel> userDeptExcels){
        for (UserDeptExcel userDeptExcel : userDeptExcels) {
            updateDeptCode(userDeptExcel);
        }
    }
}
