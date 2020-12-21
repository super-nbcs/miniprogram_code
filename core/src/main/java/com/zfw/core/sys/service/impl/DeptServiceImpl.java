package com.zfw.core.sys.service.impl;

import com.zfw.core.annotation.cache.CacheAble;
import com.zfw.core.annotation.cache.CacheDisable;
import com.zfw.core.exception.GlobalException;
import com.zfw.core.service.impl.CommonServiceImpl;
import com.zfw.core.sys.dao.IDeptDao;
import com.zfw.core.sys.dao.mapper.IDeptMapperDao;
import com.zfw.core.sys.entity.Dept;
import com.zfw.core.sys.entity.DictData;
import com.zfw.core.sys.service.IDeptService;
import com.zfw.core.sys.service.IDictDataService;
import com.zfw.core.sys.service.IUserService;
import com.zfw.dto.excel.DeptExcel;
import com.zfw.dto.excel.ExcelContext;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
public class DeptServiceImpl extends CommonServiceImpl<Dept, Integer> implements IDeptService {
    @Autowired
    private IDeptDao iDeptDao;
    @Autowired
    private IDeptMapperDao iDeptMapperDao;
    @Autowired
    private IUserService iUserService;

    @Autowired
    private IDictDataService iDictDataService;

    @Override
    @Transactional
    @CacheDisable
    public void deleteDept(Integer id) {
        boolean b = iUserService.existsByDeptId(id);
        if (b){
            throw new GlobalException("此组织下有人员绑定，禁止删除");
        }
        this.deleteById(id);
    }

    @Override
    public Dept save(Dept dept) {
        String path = dept.getPath().trim();
        StringUtils.replace(path, "(", "（");
        StringUtils.replace(path, ")", "）");
        dept.setPath(path);
        return super.save(dept);
    }

    @Override
    @CacheAble
    public Dept getById(Integer integer) {
        return super.getById(integer);
    }

    @Override
    public boolean existsByName(String name) {
        return iDeptDao.existsByName(name);
    }

    @Override
    public boolean existsByPath(String path) {
        return iDeptDao.existsByPath(path);
    }

    @Override
    @CacheAble
    public Dept findByName(String name) {
        return iDeptDao.findByName(name);
    }

    @Override
    @CacheAble
    public Dept findTopByCode(String code) {
        return iDeptDao.findTopByCode(code);
    }

    /**
     * 判断当前部门下，是否有重复的名字
     *
     * @return 有true  无false
     */
    private boolean existsByNameCurrentLeaf(Dept dept) {
        String code = dept.getCode();
        String parentPath = null;
        if (dept.getParentId()!=null&&dept.getParentId()!=0){
            Dept parentDept = this.getById(dept.getParentId());
            parentPath = parentDept.getPath();
        }
        String parentCode = StringUtils.substring(code, 0, -2);
        boolean b = iDeptMapperDao.existsByNameAndParentCode(dept.getName(), parentPath, parentCode, dept.getId());
        return b;
    }

    public String generateCode(Dept dept) {
        int codeLength = 2;
        if (dept.getParentId() == null || dept.getParentId() == 0) {
            dept.setPath(String.format("/%s",dept.getName()));
            Dept dept1 = iDeptMapperDao.findLastByCodeLength(codeLength);
            if (dept1 == null) {
                return String.format("1%s", 0);
            } else {
                String code = dept1.getCode();
                return String.valueOf(Long.valueOf(code) + 1);
            }
        }
        Dept parentDept = this.getById(dept.getParentId());
        dept.setPath(parentDept.getPath()+"/"+dept.getName());
        int length = parentDept.getCode().length();
        Dept dept2 = iDeptMapperDao.findLastByCodeLengthAndLikeParentName(codeLength + length, parentDept.getPath()+"/");
        if (dept2 == null) {
            return String.format("%s1%s", parentDept.getCode(), 0);
        } else {
            String code = dept2.getCode();
            return String.valueOf(Long.valueOf(code) + 1);
        }
    }

    @Override
    @Transactional
    @CacheDisable
    public Dept addDept(Dept dept) {
        if (StringUtils.isAnyBlank(dept.getName())) {
            throw new GlobalException("结构名称不能为空");
        }
        String code = generateCode(dept);
        dept.setCode(code);
        if (existsByNameCurrentLeaf(dept)) {
            throw new GlobalException("同级结构中，名称已存在");
        }
        synchronized (dept) {
            dept = this.save(dept);
        }
        return dept;
    }

    @Override
    public Dept findTop1ByPath(String path) {
        return iDeptDao.findTop1ByPath(path);
    }

    @Override
    @CacheAble
    public List<Dept> findAllTopTreeDept() {
        List<Dept> depts = iDeptDao.findByParentIdOrderBySort(0);
        for (Dept dept : depts) {
            getChildren(dept);
        }
        return depts;
    }


    @Override
    @CacheAble
    public Dept findAllTreeDeptByDeptId(Integer deptId) {
        return getChildren(this.getById(deptId));
    }

    @Override
    @CacheAble
    public List<Dept> findByFlag(Integer flag) {
        return iDeptDao.findByFlag(flag);
    }

    @Override
    @CacheAble
    public List<Dept> findByFlagOrderBySort(Integer flag) {
        return iDeptDao.findByFlagOrderBySort(flag);
    }

    /**
     * 递归查询，判断dept是否有子节点，无返回本身，dept.children中
     *
     * @param dept
     * @return
     */
    public Dept getChildren(Dept dept) {
        if (dept.isHasChildren()) {
            List<Dept> childrenMenus = iDeptDao.findByParentIdOrderBySort(dept.getId());
            childrenMenus.forEach(item -> {
                getChildren(item);
            });
            dept.setChildren(childrenMenus);
        }
        return dept;
    }

    @Override
    @CacheDisable
    public Dept updateDepartment(Dept dept) {
        if (existsByNameCurrentLeaf(dept)) {
            throw new GlobalException("同级结构中，名称已存在");
        }
        Dept oldDept = iDeptDao.getOne(dept.getId());
        BeanUtils.copyProperties(dept, oldDept);
        return iDeptDao.save(oldDept);
    }

    @Override
    @CacheAble
    public List<Dept> findByParentIdOrderBySort(Integer parentId) {
        return iDeptDao.findByParentIdOrderBySort(parentId);
    }

    @Override
    public void updateAllChild() {
        List<Dept> departments = iDeptDao.findAll();
        for (Dept department : departments) {
            StringBuilder ids = new StringBuilder();
            ids.append(department.getId());
            ArrayList<Dept> allChilds = new ArrayList<>();
            getAllChild(department, allChilds);
            if (allChilds != null) {
                for (Dept dept : allChilds) {
                    ids.append(",");
                    ids.append(dept.getId());
                }
            }
            department.setChildIds(ids.toString());
            this.save(department);
        }
    }


    public void getAllChild(Dept department, List<Dept> allChilds) {
        List<Dept> departments = iDeptDao.findByParentIdOrderBySort(department.getId());
        if (departments == null || departments.size() == 0) {
            return;
        } else {
            allChilds.addAll(departments);
            for (Dept department1 : departments) {
                getAllChild(department1, allChilds);
            }
        }
    }

    @Override
    public void updateAllPath() {
        List<Dept> depts = iDeptDao.findAll();
        for (Dept dept : depts) {
            updatePath(dept);
            this.save(dept);
        }
    }

    /**
     * 递归查找dept的父级为flag的数据
     * @param dept
     * @param flag
     */
    @Override
    public Dept getDeptParentByFlag(Dept dept, int flag) {
        if (dept.getFlag().equals(flag)||dept.getParentId()==0){
            return dept;
        }
        Dept parentDept = this.getById(dept.getParentId());
        return getDeptParentByFlag(parentDept,flag);
    }

    /**
     * 获取所有父ids
     * @param dept
     * @return
     */
    @Override
    public List<Integer> getParentIds(Dept dept,List<Integer> parentIds) {
        if (parentIds==null){
            parentIds=new ArrayList<>();
        }
        parentIds.add(dept.getId());
        if (dept.getParentId().equals(0)){
            return parentIds;
        }else {
            return getParentIds(dept,parentIds);
        }
    }


    private void updatePath(Dept dept) {
        String code = dept.getCode();
        int i1 = code.length() / 2;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < i1; i++) {
            String code1 = StringUtils.substring(code, 0, (i + 1) * 2);
            Dept byCode = iDeptDao.findByCode(code1);
            sb.append(String.format("/%s", byCode.getName()));
        }
        dept.setPath(sb.toString());
    }

    /**
     * 导入组织架构
     *
     * @param deptExcels
     */
    @Override
    @CacheDisable
    @Transient
    public void importExcel(List<DeptExcel> deptExcels) {
        for (DeptExcel deptExcel : deptExcels) {
            if (StringUtils.isAnyBlank(deptExcel.getPath(),deptExcel.getFlag())){
                deptExcel.setRemark("全路径，标识，标识值必填");
                continue;
            }
            if (deptExcel.getFlagValue()==null){
                deptExcel.setRemark("标识值必填");
                continue;
            }
            if (!StringUtils.startsWith(deptExcel.getPath(),"/")){
                deptExcel.setPath(String.format("/%s",deptExcel.getPath()));
            }

            Dept top1ByPath = this.findTop1ByPath(deptExcel.getPath().trim());
            if (top1ByPath!=null){
                deptExcel.setRemark("此部门已存在");
                continue;
            }



            String path = deptExcel.getPath().trim();
            StringUtils.replace(path, "(", "（");
            StringUtils.replace(path, ")", "）");

            int i = StringUtils.lastIndexOf(deptExcel.getPath(), "/");
            String parentPath = StringUtils.substring(deptExcel.getPath(),0, i);
            Dept parentDept = getParentDept(path);
            if (parentDept==null){
                deptExcel.setRemark(String.format("找不到父级结构【%s】",parentPath));
                continue;
            }
            String flag = deptExcel.getFlag();
            DictData dictData = iDictDataService.findByTypeNameAndLabel("sys_dict_dept_flag", flag);
            if (dictData == null) {
                deptExcel.setRemark(String.format("组织架构标识【%s】没找到", flag));
                continue;
            }
            Dept dept = new Dept().setPath(path).setName(getNameByPath(path)).setFlag(Integer.valueOf(dictData.getValue()));
            if (parentDept == null) {
                dept.setParentId(0);
            } else {
                dept.setParentId(parentDept.getId());
            }
            try {
                this.addDept(dept);
                deptExcel.setRemark(ExcelContext.SUCCESS);
            } catch (GlobalException e) {
                deptExcel.setRemark(e.getZhCode());
            }
        }
    }

    /**
     * 获取所有年级
     * @return
     */
    @Override
    @CacheAble
    public List<String> getAllGradeGroup() {
        return iDeptMapperDao.getAllGradeGroup();
    }

    private Dept getParentDept(String path) {
        int i = StringUtils.lastIndexOf(path, "/");
        String substring = StringUtils.substring(path, 0, i);
        Dept dept = this.findTop1ByPath(substring);
        return dept;
    }

    private String getNameByPath(String path) {
        String[] split = path.split("/");
        String s = split[split.length - 1];
        return s;
    }

}
