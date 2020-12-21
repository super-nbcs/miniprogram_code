package com.zfw.core.sys.service;

import com.zfw.core.service.ICommonService;
import com.zfw.core.sys.entity.Progress;
import com.zfw.core.sys.entity.User;
import com.zfw.dto.excel.PhotoExcel;
import com.zfw.utils.StringUtilsEx;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;

public interface IUserService extends ICommonService<User,Integer> {
    /**
     * 查询出所有上传照片的人员
     * @return
     */
    List<User> findByPhotoIsNotNull();
    List<User> findByPhotoFlag(Integer photoFlag);
    User findTop1ByMiniOpenId(String miniOpenId);
    /**
     * 获取用户所具有的角色，以及权限
     * @param userId
     * @return
     */
    User getUserRole(Integer userId);

    String generateToken();

    /**
     * 获取用户所具有的角色，以及权限
     * @param user
     * @return
     */
    User getUserRole(User user);


    /**
     * 获取用户的具有的所有菜单列表，并以tree封入user.setMenu中
     * @param user
     * @return
     */
    User getUserTreeMenu(User user);
    /**
     * 通过用户获取盐值密码
     * @param user
     * @return
     */
    String getSaltPassword(User user);

    /**
     * 注册用户
     * @param user
     * @return 返回注册成功的用户
     */
    User registerUser(User user);


    /**
     * 修改密码
     * @param userId
     * @param password
     * @return 返回修改成功的user实体
     */
    User changePassword(Integer userId, String password);

    User login(String userName, String password);

    void logout(User currentUser);

    /**
     * 通过id删除用户，同时删除user_role表中绑定的userId
     * @param id
     */
    void deleteUser(Integer id);

    /**
     * 用于批量退宿接口
     * @param id
     * @return
     */
    boolean deleteUserAndBack(Integer id,Progress progress);

    /**
     * 通过user对象和request对象来获取登录日志
     * @param user
     * @param request
     */
    void saveLoginLog(User user, HttpServletRequest request);


    boolean existsByUserName(String userName);

    /**
     * 通过用户id重置email
     * @param userId
     * @param email
     */
    void resetEmail(Integer userId, String email);

    /**
     * 通过用户名获取用户对象
     * @param userName
     * @return
     */
    User findByUserName(String userName);

    /**
     * 创建用户信息
     * @param user
     * @return
     */
    User createUser(User user);

    /**
     * 初始化密码
     * @param user
     * @return
     */
    default String initPassword(User user){
        String password= StringUtilsEx.getMD5("000000") ;
        String idCard = user.getIdCard();
        if (StringUtils.isNotBlank(idCard)){
            password=StringUtilsEx.getMD5(StringUtils.substring(idCard,idCard.length()-6));
        }
        return password;
    }

    /**
     * 修改用户信息
     * @param user
     * @return
     */
    User updateUser(User user);

    /**
     * 根据部门id判断数据是否存在
     * @param deptId
     * @return
     */
    boolean existsByDeptId(Integer deptId);

    List<User> findByDeptCodeIsStartingWith(String deptCode);


    /**
     * 导入照片
     * @param files
     */
    void importPhotos(List<File> files, List<PhotoExcel> excels,boolean isCheckFace) throws IOException;

    /**
     * 通过userName更改deptId,deptPath，deptCode
     * @param userName
     * @param deptId
     * @param deptPath
     * @param deptCode
     */
    void updateDeptCode(String userName,Integer deptId,String deptPath,String deptCode) ;




}

