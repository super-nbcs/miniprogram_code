package com.zfw.core.sys.service;

import com.zfw.core.service.ICommonService;
import com.zfw.core.sys.entity.Dept;
import com.zfw.dto.excel.DeptExcel;

import java.util.List;


public interface IDeptService extends ICommonService<Dept,Integer> {
    void deleteDept(Integer id);
    boolean existsByName(String name);
    boolean existsByPath(String path);
    Dept findByName(String name);
    Dept findTopByCode(String code);
    Dept addDept(Dept dept);
    Dept findTop1ByPath(String path);

    /**
     * 获取所有顶级树形结构
     * @return
     */
    List<Dept> findAllTopTreeDept();

    /**
     * 传入部门id，返回所有的子元素，树形
     * @param deptId
     * @return
     */
    Dept findAllTreeDeptByDeptId(Integer deptId);

    List<Dept> findByFlag(Integer flag);

    List<Dept> findByFlagOrderBySort(Integer flag);

    /**
     * 修改部门信息
     * @return
     */
    Dept updateDepartment(Dept dept);

    List<Dept> findByParentIdOrderBySort(Integer parentId);

    void updateAllChild();

    void updateAllPath();
    Dept getDeptParentByFlag(Dept dept,int flag);

    List<Integer> getParentIds(Dept dept,List<Integer> parentIds);

    void importExcel(List<DeptExcel> deptExcels);


    List<String> getAllGradeGroup();
}
