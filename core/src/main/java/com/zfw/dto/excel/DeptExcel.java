package com.zfw.dto.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.zfw.core.sys.entity.Dept;
import com.zfw.core.sys.entity.DictData;
import lombok.Data;

/**
 * @Author:zfw
 * @Date:2020/8/5
 * @Content: 组织架构导入、导出
 */
@Data
@ContentRowHeight(15)
@HeadRowHeight(20)
@ColumnWidth(20)
public class DeptExcel extends ExcelContext{
    @ExcelProperty(index = 0,value="全名称")
    private String path;
    @ExcelProperty(index = 1,value = "标识")
    private String flag;
    @ExcelProperty(index = 2,value = "标识值(普通部门[0]，学院[1]，专业[2]，年级[3]，班级[4])")
    private Integer flagValue;
    @ExcelProperty(index = 3,value = "名称")
    private String name;
    @ExcelProperty(index = 4,value = "备注")
    private String remark;


    public DeptExcel of(Dept dept){
        DictData dictData = getDictData("sys_dict_dept_flag", String.valueOf(dept.getFlag()));
        this.setFlag(dictData==null?"无":dictData.getLabel());
        //this.setCode(dept.getCode());
        this.setFlagValue(Integer.valueOf(dictData.getValue()));
        this.setPath(dept.getPath());
        this.setName(dept.getName());
        return this;
    }

}
