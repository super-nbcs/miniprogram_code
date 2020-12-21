package com.zfw.dto.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.zfw.core.sys.entity.Progress;
import com.zfw.core.sys.entity.User;
import com.zfw.core.sys.service.IUserService;
import com.zfw.utils.StringUtilsEx;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @Author:zfw
 * @Date:2020-10-19
 * @Content: 批量导入，退宿
 */

@Data
@ContentRowHeight(15)
@HeadRowHeight(20)
@ColumnWidth(20)
public class UserRemoveExcel extends ExcelContext{

    @ExcelProperty(index = 0, value = "学号")
    @NumberFormat("#")
    private String userName;
    @ExcelProperty(index = 1,value = "备注")
    private String remark;

    public void userRemove(UserRemoveExcel userRemoveExcel){
        IUserService userService = getUserService();
        if (StringUtils.isBlank(userRemoveExcel.getUserName())){
            userRemoveExcel.setRemark("学号不能为空");
            return;
        }
        if (!userService.existsByUserName(userRemoveExcel.getUserName())){
            userRemoveExcel.setRemark("系统中无此学号");
            return;
        }
        User user = userService.findByUserName(userRemoveExcel.getUserName());
        if (user.getRoleId()!=3){
            userRemoveExcel.setRemark("此学号角色在系统中不学生");
            return;
        }
        Progress progress = new Progress().setProgressFlag(StringUtilsEx.getUUID2());
        userService.deleteUserAndBack(user.getId(), progress);
        userRemoveExcel.setRemark(progress.getRemarks());
    }

    public void userRemoves(List<UserRemoveExcel> userRemoveExcels){
        for (UserRemoveExcel userRemoveExcel : userRemoveExcels) {
            userRemove(userRemoveExcel);
        }
    }
}
