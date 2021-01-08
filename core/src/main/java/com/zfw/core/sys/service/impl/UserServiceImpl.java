package com.zfw.core.sys.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.zfw.core.annotation.cache.CacheAble;
import com.zfw.core.annotation.cache.CacheDisable;
import com.zfw.core.constant.CacheName;
import com.zfw.core.constant.IDSConstant;
import com.zfw.core.exception.GlobalException;
import com.zfw.core.service.impl.CommonServiceImpl;
import com.zfw.core.sys.dao.IDutyDao;
import com.zfw.core.sys.dao.IUserDao;
import com.zfw.core.sys.dao.IUserRoleDao;
import com.zfw.core.sys.dao.mapper.IUserMapperDao;
import com.zfw.core.sys.entity.*;
import com.zfw.core.sys.service.*;
import com.zfw.dto.excel.ExcelContext;
import com.zfw.dto.excel.PhotoExcel;
import com.zfw.utils.FileStore.FileStoreUtils;
import com.zfw.utils.RedisUtils;
import com.zfw.utils.StringUtilsEx;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.zfw.core.constant.Constant.*;

@Service
public class UserServiceImpl extends CommonServiceImpl<User, Integer> implements IUserService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IFileService iFileService;

    @Autowired
    private IUserDao iUserDao;

    @Autowired
    private IUserRoleDao iUserRoleDao;

    @Autowired
    private IRoleService iRoleService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private IUserRoleService iUserRoleService;

    @Autowired
    private ILoginLogService loginLogService;

    @Autowired
    private IDutyDao dutyDao;

    @Autowired
    private IDeptService deptService;

    @Autowired
    private IUserMapperDao iUserMapperDao;

    @Autowired
    private IProgressService iProgressService;
    @Autowired
    private IUserBackService iUserBackService;

    @Override
    public User save(User user) {
        if (user.getCanLogin() == null) {
            user.setCanLogin(0);
        }
        return super.save(user);
    }


    @Override
    public List<User> findByPhotoIsNotNull() {
        return iUserDao.findByPhotoIsNotNull();
    }

    @Override
    public List<User> findByPhotoFlag(Integer photoFlag) {
        return iUserMapperDao.findMapperByPhotoFlag(photoFlag);
    }

    @Override
    public User findTop1ByMiniOpenId(String miniOpenId) {
        return iUserDao.findTop1ByMiniOpenId(miniOpenId);
    }

    @Override
    public boolean existsByMiniOpenId(String miniOpenId) {
        return iUserDao.existsByMiniOpenId(miniOpenId);
    }

    @Override
    public User getUserRole(Integer userId) {
        if (!this.existsById(userId)) {
            throw new GlobalException(NOT_FOUND_ID);
        }
        User user = this.getById(userId);
        Set<UserRole> userRoles = iUserRoleDao.findByUserId(userId);
        Set<Role> roles = new HashSet<>();
        userRoles.forEach(userRole -> {
            Integer roleId = userRole.getRoleId();
            Role role = iRoleService.getRoleMenus(roleId);
            roles.add(role);
        });
        user.setRoles(roles);
        return user;
    }

    @Override
    public String generateToken() {
        return StringUtilsEx.getUUID2();
    }

    @Override
    public User getUserRole(User user) {
        if (user == null) {
            throw new GlobalException(FAIL_NULL);
        }
        return getUserRole(user.getId());
    }

    @Override
    public User getUserTreeMenu(User user) {
        Set<Role> roles = user.getRoles();

        Set<Menu> menus = new HashSet<>();

        roles.forEach(role -> menus.addAll(role.getMenus()));

        ArrayList<Menu> rootMenus = new ArrayList<>(menusTree(menus));
        user.setMenus(rootMenus.stream().sorted(Comparator.comparing(Menu::getSort)).collect(Collectors.toList()));
        return user;
    }

    /**
     * 将所有菜单重新以树形结构排列并返回，只处理两级
     *
     * @param menus
     * @return
     */
    public Set<Menu> menusTree(Set<Menu> menus) {
        Set<Menu> rootMenus = menus.stream().filter(menu -> menu.isRoot()).collect(Collectors.toSet());

        for (Menu rootMenu : rootMenus) {
            Set<Menu> childrenMenu = menus.stream().filter(menu -> menu.getParentId() == rootMenu.getId()).collect(Collectors.toSet());
            ArrayList<Menu> children = new ArrayList<>(childrenMenu);
            rootMenu.setChildren(children.stream().sorted(Comparator.comparing(Menu::getSort)).collect(Collectors.toList()));
        }
        return rootMenus;
    }


    @Override
    public String getSaltPassword(User user) {
        userNotNull(user);
        if (user.getId() == null) {
            throw new GlobalException(NOT_NULL_ID);
        }
        String str = user.getPassword() + user.getId();
        String md5 = StringUtilsEx.getMD5(str, user.getId());
        return md5;
    }

    @Override
    public User registerUser(User user) {
        userNotNull(user);
        boolean b = iUserDao.existsByUserName(user.getUserName());
        if (b) {
            throw new GlobalException(CODE_10011);
        }

        user = save(user);
        user.setSaltPassword(getSaltPassword(user));
        return save(user);
    }

    @Override
    @Transactional(rollbackFor = GlobalException.class)
    public User changePassword(Integer userId, String password) {
        if (this.existsById(userId)) {
            User user = getById(userId);
            user.setPassword(password);
            userNotNull(user);
            user.setSaltPassword(getSaltPassword(user));
            return save(user);
        } else {
            throw new GlobalException(NOT_FOUND_ID);
        }
    }

    @Override
    public User login(String userName, String password) {
        if (!iUserDao.existsByUserName(userName)) {
            throw new GlobalException(CODE_10012);
        }

        User user = iUserDao.findByUserName(userName);
        if (user.getCanLogin() == null || user.getCanLogin() == 1) {
            throw new GlobalException(CODE_10004);
        }

        String saltPassword = getSaltPassword(new User().setId(user.getId()).setPassword(password).setUserName(userName));
        if (saltPassword.equals(user.getSaltPassword())) {
            user = getUserRole(user);
            user = getUserTreeMenu(user);
            String token = this.generateToken();
            user.setToken(token);


            redisUtils.sSet(CacheName.USER_ID + user.getId(), CacheName.AUTHORIZED_NAME + token);
            redisUtils.set(CacheName.AUTHORIZED_NAME + token, user, CacheName.AUTHORIZED_NAME_TIMEOUT);

            User finalUser = user;
            new Thread(() -> clearInvalidValue(CacheName.USER_ID + finalUser.getId())).start();
            return user;
        } else {
            throw new GlobalException(CODE_10013);
        }
    }

    @Override
    public void logout(User currentUser) {
        String token = currentUser.getToken();
        redisUtils.setRemove(CacheName.USER_ID + currentUser.getId(), CacheName.AUTHORIZED_NAME + token);
        redisUtils.del(CacheName.AUTHORIZED_NAME + token);
    }

    @Override
    @Transactional
    public void deleteUser(Integer id) {
        if (id == 1) {
            throw new GlobalException(FAIL.CODE, FAIL.ZH_CODE + "超级用户不能删除", FAIL.EN_CODE);
        }
        iUserRoleService.deleteAllByUserId(id);
        deleteById(id);
    }

    @Override
    @Transactional
    public boolean deleteUserAndBack(Integer id, Progress progress) {
        String remarks = StringUtils.isBlank(progress.getRemarks()) ? "\n" : progress.getRemarks()+"\n";

        if (!existsById(id)) {
            progress.setRemarks(String.format("%s失败:原因：不存在此用户id【%s】,可能在其它地方删除了", remarks,id));
            iProgressService.save(progress);
            return false;
        }
        User user = getById(id);
        //保存到退宿记录表中
        UserBack userBack = new UserBack();
        BeanUtils.copyProperties(user, userBack);
        userBack.setRemarks("退宿");
        iUserBackService.save(userBack.setId(null));

        try {
            deleteUser(id);
            progress.setRemarks(String.format("%s成功：【%s】",remarks,user.getUserName()));
            iProgressService.save(progress);
            return true;
        } catch (Exception e) {
            progress.setRemarks(String.format("%s失败:【%s】,原因：【%s】", remarks,user.getUserName(),e.getMessage()));
            iProgressService.save(progress);
            return false;
        }
    }

    @Override
    public void saveLoginLog(User user, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        if (!StringUtils.isNotBlank(userAgent)) {
            throw new GlobalException(CODE_10008);
        }
        LoginLog loginLog = new LoginLog();
        loginLog.setRemoteAddr(ip).setUserId(user.getId()).setUsername(user.getUserName()).setUserAgent(userAgent).setToken(user.getToken());
        loginLogService.save(loginLog);
    }

    public void userNotNull(User user) {
        if (user.getPassword() == null) {
            throw new GlobalException(CODE_10014);
        }
        if (user.getUserName() == null) {
            throw new GlobalException(CODE_10015);
        }
    }


    /**
     * 清理当前用户中无用的token
     *
     * @param setKeyName
     */
    protected void clearInvalidValue(String setKeyName) {
        logger.info("---------------开始清理缓存：｛" + setKeyName + "｝中的无效token------------");
        Set<Object> tokens = redisUtils.sGet(setKeyName);
        tokens.forEach(token -> {
            Object o = redisUtils.get((String) token);
            if (o == null) {
                logger.info("---------------清理缓存：｛" + setKeyName + "｝中无效的token：｛" + token + "｝------------");
                redisUtils.setRemove(setKeyName, token);
            }
        });
    }


    @Override
    public boolean existsByUserName(String userName) {
        return iUserDao.existsByUserName(userName);
    }

    @Override
    @Transactional
    public void resetEmail(Integer userId, String email) {
        if (userId == null) {
            throw new GlobalException(FAIL.CODE, FAIL.ZH_CODE + ":userId不能为null", FAIL.ZH_CODE);
        }
        User user = this.getById(userId).setEmail(email);
        this.save(user);
    }

    @Override
    @CacheAble
    public User findByUserName(String userName) {
        return iUserDao.findByUserName(userName);
    }

    @Override
    @Transactional
    @CacheDisable
    public User createUser(User user) {

        if (!deptService.existsById(user.getDeptId())) {
            throw new GlobalException(CODE_10028);
        }
        if (StringUtils.isNotBlank(user.getPhone()) && iUserDao.existsByPhone(user.getPhone())) {
            throw new GlobalException(CODE_10033);
        }
        updateUserAttr(user);
        user.setPassword(initPassword(user));
        user = registerUser(user);
        //绑定角色
        iUserRoleService.bindRole(user.getId(), Arrays.asList(user.getRoleId()));
        return user;
    }


    @Override
    @Transactional
    @CacheDisable
    public User updateUser(User user) {
        User oldUser = this.getById(user.getId());
        if (ObjectUtils.isEmpty(oldUser)) {
            throw new GlobalException(NOT_FOUND_ID);
        }
        if (!deptService.existsById(user.getDeptId())) {
            throw new GlobalException(CODE_10028);
        }
        if (!oldUser.getUserName().equals(user.getUserName())) {
            throw new GlobalException(CODE_10031);
        }
        updateUserAttr(user);

//        绑定角色
        iUserRoleService.bindRole(user.getId(), Arrays.asList(user.getRoleId()));
        //如果没有更改床位就不执行更改床位了
        User byId = this.getById(user.getId());
        return this.save(user);
    }

    private void updateUserAttr(User user) {
        Dept dept = deptService.getById(user.getDeptId());
        user.setDeptCode(dept.getCode()).setDeptPath(dept.getPath());
    }

    @Override
    public boolean existsByDeptId(Integer deptId) {
        return iUserDao.existsByDeptId(deptId);
    }

    @Override
    public List<User> findByDeptCodeIsStartingWith(String deptCode) {
        return iUserDao.findByDeptCodeIsStartingWith(deptCode);
    }


    @Override
    public void importPhotos(List<File> files, List<PhotoExcel> excels,boolean isCheckFace) throws IOException {

        for (File file : files) {
            PhotoExcel photoExcel = new PhotoExcel();
            String name = file.getName();
            String userName = FileStoreUtils.getFileNameWithoutExtension(name);
            photoExcel.setFileName(name);
            if (!FileStoreUtils.isPhoto(file)) {
                photoExcel.setRemark("请上传图片文件");
                excels.add(photoExcel);
                continue;
            }
            if (!this.existsByUserName(userName)) {
                photoExcel.setRemark("根据文件名找不到对应的学工号");
                excels.add(photoExcel);
                continue;
            }
            User user = this.findByUserName(userName);
            User newUser = new User();
            BeanUtils.copyProperties(user, newUser);
            if (StringUtils.isNotBlank(user.getPhoto())) {
                photoExcel.setRemark("此学号已经上传过照片了，如修改，请在系统中操作");
                excels.add(photoExcel);
                continue;
            }
            if (isCheckFace){
                JSONObject check = iFileService.check(file);
                if (check.getInteger("code")!=0){
                    photoExcel.setRemark(check.getString("desc"));
                    excels.add(photoExcel);
                    continue;
                }
            }

            String newPhoto = FileStoreUtils.createFile(file);
            newUser.setPhoto(newPhoto);
            try {
                this.updateUser(newUser);
                photoExcel.setRemark(ExcelContext.SUCCESS);
                excels.add(photoExcel);
            } catch (GlobalException e) {
                photoExcel.setRemark(e.getZhCode());
                excels.add(photoExcel);
            }
        }
    }

    @Override
    public void updateDeptCode(String userName, Integer deptId, String deptPath, String deptCode) {
        iUserDao.updateDeptCode(userName, deptId, deptPath, deptCode);
    }

}

