package com.zfw.dto.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 错误人员信息导出表格
 */
@Data
@ContentRowHeight(15)
@HeadRowHeight(20)
@ColumnWidth(20)
@Accessors(chain = true)
public class UserExcelError {

    @ExcelProperty(index = 0,value="姓名")
    private String name;

    @ExcelProperty(index = 1,value = "性别")
    private String gender;

    @ExcelProperty(index = 2,value = "工号")
    @NumberFormat("#")
    private String number;

    @ExcelProperty(index = 3,value = "手机号")
    @NumberFormat("#")
    private String phone;

    @ExcelProperty(index = 4,value = "职位")
    private String duty;

    @ExcelProperty(index = 5,value = "部门")
    private String dept;

    @ExcelProperty(index = 6,value = "身份证号")
    private String idCard;

    @ExcelProperty(index = 7,value = "入职时间")
    private String entryTime;

    @ExcelProperty(index = 8,value = "生日")
    private String birthday;

    @ExcelProperty(index = 9,value = "录入设备")
    private String deviceList;

    @ExcelProperty(index = 10,value = "绑定时段")
    private String scheduleList;

    @ExcelProperty(index = 11,value = "错误原因")
    private String errorMsg;

}
