package com.zfw.core.sys.controller;

import com.zfw.core.constant.Constant;
import com.zfw.core.exception.GlobalException;
import com.zfw.core.sys.entity.RolePermission;
import com.zfw.core.sys.entity.User;
import com.zfw.core.sys.entity.UserRole;
import com.zfw.core.sys.service.IRolePermissionService;
import com.zfw.core.sys.service.IUserRoleService;
import com.zfw.utils.StringUtilsEx;
import com.zfw.utils.SubjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @Author:zfw
 * @Date:2019/7/11
 * @Content:
 */
@RequestMapping("${ops.api-version}")
@ResponseBody
public class BaseController implements AnalysisExcel,AnalysisData{

    private static final String CURRENT_PAGE_PARAM_NAME = "currentPage";
    private static final String PAGE_SIZE_PARAM_NAME = "pageSize";
    private static final String SORT_BY_PARAM_NAME = "sortBy";
    private static final String ORDER_PARAM_NAME = "order";


    @Autowired
    private SubjectUtils subjectUtils;

    @Autowired
    private IRolePermissionService rolePermissionService;

    @Autowired
    private IUserRoleService userRoleService;

    @ModelAttribute("permissionList")
    public Map<String, List<String>> getPermissionList() {
        boolean allPermission = false;
        boolean personalPermission = false;
        User user = null;
        try {
            user = currentUser();
            if (user==null){
                return null;
            }
        } catch (Exception e) {
            return null;
        }
        Set<UserRole> userRoleList = userRoleService.findByUserId(user.getId());
        List<String> list = new ArrayList<>();
        for (UserRole ur : userRoleList) {
//            TODO  新建角色后要想RolePermission表中放数据
            RolePermission rp = rolePermissionService.findByRoleId(ur.getRoleId());
            if (rp == null) {
                break;
            }
            String deptIds = rp.getDeptIds();
            if (deptIds.equals("00000000")) {
                personalPermission = true;
                break;
            }else if (deptIds.equals("11111111")) {
                allPermission = true;
                break;
            } else {
                list.addAll(Arrays.asList(deptIds.split(",")));
            }
        }
        Map<String, List<String>> map = new HashMap<>();
        if (allPermission) {
            return null;
        } else if (personalPermission) {
            List<String> l = new ArrayList<>();
            l.add(user.getId().toString());
            map.put("id", l);
            return map;
        } else {
            map.put("deptId", list);
            return map;
        }
    }


    protected Logger logger = LoggerFactory.getLogger(this.getClass());


    protected User currentUser() {
        return subjectUtils.getPrincipal();
    }

    protected User resetUser(Integer userId) {
        return subjectUtils.resetPrincipal(userId);
    }

    protected Boolean isAdmin() {
        //TODO 此处改
        User user = currentUser();
        if (user.getId() == 1 && user.getUserName().toLowerCase().equals("admin"))
            return true;
        return false;
    }

    protected PageRequest dynamicAnalysisRequest(HttpServletRequest request) {
        int DEFAULT_CURRENT_PAGE = 1;
        int DEFAULT_PAGE_SIZE = 20;
        String pageNo = request.getParameter(CURRENT_PAGE_PARAM_NAME);
        String pageSize = request.getParameter(PAGE_SIZE_PARAM_NAME);
        String sortBy = request.getParameter(SORT_BY_PARAM_NAME);
        String order = request.getParameter(ORDER_PARAM_NAME);

        Sort sort = null;
        if (!StringUtilsEx.isBlank(pageNo) && !StringUtilsEx.isBlank(pageSize)) {
            DEFAULT_CURRENT_PAGE = Integer.valueOf(pageNo);
            DEFAULT_PAGE_SIZE = Integer.valueOf(pageSize);
        }
        if (StringUtilsEx.isBlank(sortBy) && StringUtilsEx.isBlank(order)) {
            sort = Sort.by(Sort.Order.desc("id"));
        }
        if (StringUtilsEx.isNotBlank(sortBy) && StringUtilsEx.isBlank(order)) {
            sort = Sort.by(Sort.Order.asc(sortBy));
        }
        if (StringUtilsEx.isNotBlank(sortBy) && StringUtilsEx.isNotBlank(order)) {
            if (order.toUpperCase().equals(Sort.Direction.ASC.name())) {
                sort = Sort.by(Sort.Order.asc(sortBy));
            } else if (order.toUpperCase().equals(Sort.Direction.DESC.name())) {
                sort = Sort.by(Sort.Order.desc(sortBy));
            } else {
                throw new GlobalException(Constant.CODE_10006);
            }
        }
        return PageRequest.of(DEFAULT_CURRENT_PAGE, DEFAULT_PAGE_SIZE, sort);
    }




}
