package com.zfw.core.sys.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.zfw.core.annotation.UnAuthorized;
import com.zfw.core.annotation.Validate;
import com.zfw.core.exception.GlobalException;
import com.zfw.core.sys.entity.Dept;
import com.zfw.core.sys.entity.ImportMsg;
import com.zfw.core.sys.service.IDeptService;
import com.zfw.core.sys.service.IImportMsgService;
import com.zfw.dto.excel.DeptExcel;
import com.zfw.utils.DateUtils;
import com.zfw.utils.FileStore.FileStoreUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.zfw.core.constant.Constant.NOT_FOUND_ID;
import static com.zfw.dto.excel.ExcelContext.SUCCESS;

/**
 * @Author:zfw
 * @Date:2019/8/6
 * @Content:
 */
@Api(tags = "部门（组织结构）相关接口")
@Controller
public class DeptController extends BaseController {

    @Autowired
    private IDeptService iDeptService;

    @Autowired
    private IImportMsgService iImportMsgService;


    @ApiOperation(value = "获取所有部门")
    @GetMapping("depts")
    public JSONObject getDepartments() {
        return success(iDeptService.findAll());
    }


    @ApiOperation(value = "获取所有树形结构部门")
    @GetMapping("depts/tree")
    public JSONObject getDepartmentsTree() {
        return success(iDeptService.findAllTopTreeDept());
    }

    @ApiOperation(value = "传入部门id，返回当前部门下的树形结构")
    @GetMapping("depts/tree/{id}")
    public JSONObject getDeptTreeByParentId(@PathVariable("id") Integer id) {
        Dept dept = iDeptService.findAllTreeDeptByDeptId(id);
        List<Dept> depts = new ArrayList<>();
        depts.add(dept);
        return success(depts);
    }

    @ApiOperation(value = "通过id查找部门", notes = "id不能为null")
    @GetMapping("/dept/{id}")
    public JSONObject getDepartmentById(@PathVariable("id") Integer id) {
        if (iDeptService.existsById(id)) {
            Dept department = iDeptService.getById(id);
            return success(department);
        } else {
            throw new GlobalException(NOT_FOUND_ID.CODE, NOT_FOUND_ID.ZH_CODE + id, NOT_FOUND_ID.EN_CODE + id);
        }
    }

    @ApiOperation(value = "创建部门", notes = "根据业务需求创建部门名")
    @Validate
    @PostMapping("dept")
    public JSONObject createDepartment(Dept department) {
        Dept save = iDeptService.addDept(department);
        return success(save);
    }


    @ApiOperation(value = "通过id删除部门", notes = "id不能为null")
    @DeleteMapping("dept/{id}")
    public JSONObject deleteDepartmentById(@PathVariable("id") Integer id) {
        if (iDeptService.existsById(id)) {
            iDeptService.deleteDept(id);
            return success();
        } else {
            throw new GlobalException(NOT_FOUND_ID.CODE, NOT_FOUND_ID.ZH_CODE + id, NOT_FOUND_ID.EN_CODE + id);
        }
    }

    @ApiOperation(value = "修改部门")
    @PutMapping("dept")
    public JSONObject updateDepartment(Dept department) {
        if (iDeptService.existsById(department.getId())) {
            iDeptService.updateDepartment(department);
            new Thread(()->iDeptService.updateAllPath()).start();
            return success();
        } else {
            throw new GlobalException(NOT_FOUND_ID.CODE, NOT_FOUND_ID.ZH_CODE + department.getId(), NOT_FOUND_ID.EN_CODE + department.getId());
        }
    }


    @ApiOperation(value = "传入父id,获取下面所有的子部门")
    @GetMapping("dept/parentId/{parentId}")
    public JSONObject getDeptByParentId(@PathVariable(value = "parentId") int parentId) {
        List<Dept> depts = iDeptService.findByParentIdOrderBySort(parentId);
        return success(depts);
    }

    @ApiOperation(value = "通过flag标识获取所有，flag的值去字典中查找")
    @GetMapping("depts/flag/{flag}")
    public JSONObject getDeptsByFlag(@PathVariable("flag") Integer flag){
        List<Dept> depts = iDeptService.findByFlagOrderBySort(flag);
        return success(depts);
    }

    @UnAuthorized
    @ApiOperation(value = "导出所有部门")
    @GetMapping("depts/export")
    public void exportDeptExcel(HttpServletResponse response,@RequestParam(value = "flags",required = false) List<Integer> flags) {
        List<Dept> depts = iDeptService.findAll();
        List<DeptExcel> deptExcels=new ArrayList<>();
        if (flags==null){
            depts.stream().forEach(item -> deptExcels.add(new DeptExcel().of(item)));
        }else {
            depts.stream().filter(dept->flags.contains(dept.getFlag())).forEach(item -> deptExcels.add(new DeptExcel().of(item)));
        }
        String fileName=String.format("部门结构_%s", DateUtils.getDate("yyyyMMddHHmmss"));
        String sheetName = "部门结构";
        exportExcel(response,DeptExcel.class,fileName,sheetName,deptExcels);
    }

    @UnAuthorized
    @ApiOperation(value = "导入所有部门")
    @PostMapping("depts/import")
    public void importDeptExcel(MultipartFile file) throws IOException {
        ImportMsg importMsg = new ImportMsg().setFileName(file.getOriginalFilename()).setImportStartTime(new Date());
        List<DeptExcel> deptExcels = analysisExcel(file, DeptExcel.class,0);
        deptExcels=deptExcels.stream().sorted(Comparator.comparing(DeptExcel::getFlagValue)).collect(Collectors.toList());
        iDeptService.importExcel(deptExcels);

        long countSuccess = deptExcels.stream().filter(s -> s.getRemark().equals(SUCCESS)).count();
        long countFail = deptExcels.size() - countSuccess;
        Path xls = FileStoreUtils.createEmptyFile("xls", true);
        EasyExcel.write(xls.toFile(), DeptExcel.class).sheet("导入组织架构反馈信息").doWrite(deptExcels);
        importMsg.setImportErrorMsg(String.format("成功【%s】,失败【%s】,总条数【%s】",countSuccess,countFail,deptExcels.size()));
        importMsg.setErrorFileName(StringUtils.replace(xls.toString(),FileStoreUtils.systemParentPath(),""));
        importMsg.setType("导入组织架构信息");
        importMsg.setImportEndTime(new Date());
        iImportMsgService.save(importMsg);
    }

    @UnAuthorized
    @ApiOperation(value = "下载组织建构上传模板")
    @GetMapping("depts/download/template")
    public void downloadDeptsExcel(HttpServletResponse response) throws IOException {
        String fileName="depts_template.xls";
        Resource resource = new ClassPathResource("templates_core/excel/" + fileName);
        Path xls = FileStoreUtils.createEmptyFile("xls", true);
        File file = xls.toFile();
        FileUtils.copyToFile(resource.getInputStream(), file);
        excelDownload(response, file);
    }


    @ApiOperation(value = "获取所有年级")
    @GetMapping("/depts/grade")
    public JSONObject getAllGrade(){
        List<String> grades = iDeptService.getAllGradeGroup();
        return success(grades);
    }

}

