package com.zfw.core.sys.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.zfw.core.annotation.LogAnnotation;
import com.zfw.core.annotation.UnAuthorized;
import com.zfw.core.annotation.Validate;
import com.zfw.core.constant.CacheName;
import com.zfw.core.constant.Constant;
import com.zfw.core.exception.GlobalException;
import com.zfw.core.sys.entity.*;
import com.zfw.core.sys.service.*;
import com.zfw.dto.excel.PhotoExcel;
import com.zfw.dto.excel.UserDeptExcel;
import com.zfw.dto.excel.UserRemoveExcel;
import com.zfw.utils.FileStore.FileStoreUtils;
import com.zfw.utils.FileStore.ZipUtils;
import com.zfw.utils.JpaFilterUtils;
import com.zfw.utils.RedisUtils;
import com.zfw.utils.StringUtilsEx;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.zfw.core.constant.Constant.*;
import static com.zfw.dto.excel.ExcelContext.SUCCESS;

/**
 * @Author:zfw
 * @Date:2019/7/13
 * @Content:
 */
@Api(tags = "用户相关接口")
@Controller
public class UserController extends BaseController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IUserRoleService iUserRoleService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private IRoleService iRoleService;

    @Autowired
    private IImportMsgService iImportMsgService;
    @Autowired
    private IProgressService iProgressService;

    @UnAuthorized
    @ApiOperation(value = "账号密码登录接口，无验证码登录", notes = "返回token，放在Header中")
    @PostMapping("loginWx")
    public JSONObject wxLogin(@RequestParam("userName") String userName, @RequestParam("password") String password) {
        User user = new User().setUserName(userName).setPassword(password);
        user = iUserService.login(user.getUserName(), user.getPassword());
        return success(user);
    }

    @ApiOperation(value = "账号密码登录接口，不需要授权验证", notes = "返回token，放在Header中")
    @UnAuthorized
    @PostMapping("login")
    public JSONObject login(@RequestParam("userName") String userName, @RequestParam("password") String password,
                            @RequestParam("key") String key, @RequestParam("validateCode") String validateCode) {
        if (validateCode == null) {
            throw new GlobalException(CODE_10017);
        }
        key = String.format("%s%s", CacheName.CAPTCHA_PREFIX, key);
        User user = new User().setUserName(userName).setPassword(password);
        if (redisUtils.hasKey(key)) {
            if (redisUtils.get(key).equals(validateCode.toUpperCase())) {
                user = iUserService.login(user.getUserName(), user.getPassword());
                iUserService.saveLoginLog(user, request);
            } else {
                //验证码不正确
                throw new GlobalException(CODE_10017);
            }
        } else {
            //验证码不存在
            throw new GlobalException(Constant.CODE_10016);
        }
        return success(user);
    }


    @ApiOperation(value = "退出当前用户")
    @PostMapping("logout")
    public JSONObject logout() {
        iUserService.logout(currentUser());
        return success();
    }

    @ApiOperation(value = "获取当前登录用户，从缓存中得到，数据不及时，需重新登录")
    @PostMapping("currentUser")
    public JSONObject getCurrentUser() {
        User user = currentUser();
        return success(user);
    }

    @ApiOperation(value = "重置用户信息缓存信息")
    @PostMapping("resetUser")
    public JSONObject resetUserPassword(Integer id) {
        return success(resetUser(id));
    }

    @ApiOperation(value = "通过id查找用户", notes = "包含用户角色列表")
    @GetMapping("/user/{id}")
    public JSONObject getUserById(@PathVariable("id") Integer id) {
        if (iUserService.existsById(id)) {
            return success(iUserService.getUserRole(id));
        } else {
            throw new GlobalException(NOT_FOUND_ID.CODE, NOT_FOUND_ID.ZH_CODE + id, NOT_FOUND_ID.EN_CODE + id);
        }
    }

    @LogAnnotation("创建用户信息")
    @ApiOperation(value = "创建用户信息")
    @PostMapping("/user")
    @Validate
    public JSONObject createUser(User user) {
        if (iUserService.existsByUserName(user.getUserName())) {
            throw new GlobalException(CODE_10026);
        }

        user = iUserService.createUser(user);
        return success(user);
    }

    @LogAnnotation("修改用户基本信息")
    @ApiOperation(value = "修改用户基本信息", notes = "通过id修改user内容,id必填")
    @PutMapping("user")
    @Validate
    public JSONObject updateUser(User user) {
        if (user.getId() == null) {
            throw new GlobalException(NOT_NULL_ID);
        }
        User save = iUserService.updateUser(user);
        return success(save);
    }

    @ApiOperation(value = "获取所有用户", notes = "不包含用户角色列表")
    @GetMapping("users")
    public JSONObject getUsers() {
        return success(iUserService.findAll(JpaFilterUtils.dynamicSpecificationManyValues(request, new HashMap<>(), User.class), dynamicAnalysisRequest(request)));
    }

    @ApiOperation(value = "用户绑定角色")
    @PostMapping("user/bind")
    public JSONObject bindRole(Integer userId, @RequestParam("roleIds") List<Integer> roleIds) {
        if (!isAdmin()) {
            throw new GlobalException(FAIL.CODE, FAIL.ZH_CODE + "超级管理员才具有此权限", FAIL.EN_CODE);
        }
        iUserRoleService.bindRole(userId, roleIds);
        return success();
    }

    @LogAnnotation("通过id删除用户")
    @ApiOperation(value = "通过id删除用户", notes = "id不能为null")
    @DeleteMapping("user/{id}")
    public JSONObject deleteUserById(@PathVariable("id") Integer id) {
        if (!isAdmin()) {
            throw new GlobalException(FAIL.CODE, FAIL.ZH_CODE + "超级管理员才具有此权限", FAIL.EN_CODE);
        }
        if (iUserService.existsById(id)) {
            iUserService.deleteUser(id);
            return success();
        } else {
            throw new GlobalException(NOT_FOUND_ID.CODE, NOT_FOUND_ID.ZH_CODE + id, NOT_FOUND_ID.EN_CODE + id);
        }
    }


    @ApiOperation(value = "重置密码,通过用户Id")
    @PostMapping("resetPassword/{userId}")
    public JSONObject resetPassword(@PathVariable("userId") Integer userId) {
        if (!isAdmin()) {
            throw new GlobalException(FAIL.CODE, FAIL.ZH_CODE + "超级管理员才具有此权限", FAIL.EN_CODE);
        }
        if (!iUserService.existsById(userId)) {
            throw new GlobalException("系统找不到此用户，可能已删除");
        }
        User user = iUserService.getById(userId);
        iUserService.changePassword(userId, iUserService.initPassword(user));
        return success();
    }


    @ApiOperation("重置所有用户密码")
    @GetMapping("/user/resetAllPassword")
    public void resetPassword() {
        if (!isAdmin()) {
            throw new GlobalException(FAIL.CODE, FAIL.ZH_CODE + "超级管理员才具有此权限", FAIL.EN_CODE);
        }
        List<User> all = iUserService.findAll();
        all.forEach(user -> {
            if (user.getId() != 1) {
                iUserService.changePassword(user.getId(), iUserService.initPassword(user));
                logger.error("重置成功:" + user.getId());
            }
        });
    }

    @ApiOperation(value = "修改自己密码")
    @PostMapping("changePassword")
    public JSONObject changePassword(String oldPassword, String newPassword) {
        User user = currentUser();
        String validateSaltPassword = iUserService.getSaltPassword(new User().setId(user.getId()).setPassword(oldPassword).setUserName(user.getUserName()));
        if (validateSaltPassword.equals(user.getSaltPassword())) {
            iUserService.changePassword(user.getId(), newPassword);
        } else {
            throw new GlobalException(Constant.CODE_10007);
        }
        return success();
    }

    @ApiOperation("通过角色code获取所有用户")
    @PostMapping("/user/role/{code}")
    public JSONObject getUserByRoleCode(@PathVariable("code") String code) {
        if (iRoleService.existsByCode(code)) {
            List<User> users = new ArrayList<>();
            Role role = iRoleService.findByCode(code);
            List<UserRole> userRoles = iUserRoleService.findAll(new UserRole().setRoleId(role.getId()));
            userRoles.forEach(userRole -> {
                Integer userId = userRole.getUserId();
                users.add(iUserService.getById(userId));
            });
            return success(users);
        } else {
            return fail("通过角色code没找到角色");
        }
    }

    @UnAuthorized
    @ApiOperation(value = "下载人员上传模板")
    @GetMapping("users/download/template")
    public void downloadDeptsExcel(HttpServletResponse response) throws IOException {
        String fileName = "users_template.xls";
        Resource resource = new ClassPathResource("templates_core/excel/" + fileName);
        Path xls = FileStoreUtils.createEmptyFile("xls", true);
        FileUtils.copyToFile(resource.getInputStream(), xls.toFile());
        excelDownload(response, xls.toFile());
    }

    @UnAuthorized
    @ApiOperation(value = "导入图片zip压缩包")
    @PostMapping("users/import/photos")
    public void importPhotos(MultipartFile file) {
        if (!StringUtils.endsWith(file.getOriginalFilename(), "zip")) {
            throw new GlobalException("请上传图片zip压缩包");
        }
        try {
            ImportMsg importMsg = new ImportMsg().setFileName(file.getOriginalFilename()).setImportStartTime(new Date());
            List<File> files = ZipUtils.uploadZip(file);
            List<PhotoExcel> photoExcels = new ArrayList<>();
            //此参数为是否对上传图片 进行人脸检测  可让前台传过来
            boolean isCheckFace=true;
            iUserService.importPhotos(files, photoExcels,isCheckFace);


            int size = photoExcels.size();
            long countSuccess = photoExcels.stream().filter(s -> s.getRemark().equals(SUCCESS)).count();
            long countFail = size - countSuccess;
            Path xls = FileStoreUtils.createEmptyFile("xls", true);
            EasyExcel.write(xls.toFile(), PhotoExcel.class).sheet("导入图片").doWrite(photoExcels);
            importMsg.setErrorFileName(StringUtils.replace(xls.toString(), FileStoreUtils.systemParentPath(), ""));
            importMsg.setImportErrorMsg(String.format("成功【%s】,失败【%s】,总条数【%s】", countSuccess, countFail, size));
            importMsg.setType("导入图片反馈信息");
            importMsg.setImportEndTime(new Date());
            iImportMsgService.save(importMsg);

        } catch (IOException e) {
            e.printStackTrace();
            throw new GlobalException("压缩文件读取异常");
        }


    }

    @ApiOperation("微信端更新人脸照片")
    @PostMapping("wx/update/face")
    public JSONObject wxUpdateUser(@RequestParam(value = "userId") Integer userId, @RequestParam(value = "photoPath",required = false) String photoPath) {
        if (!iUserService.existsById(userId)) {
            throw new GlobalException("此用户不存在了");
        }
        if (StringUtils.isBlank(photoPath)){
            throw new GlobalException("图片地址不能为空");
        }
        User user = iUserService.getById(userId);
        User newUser=new User();
        BeanUtils.copyProperties(user,newUser);
        newUser.setPhoto(photoPath);
        iUserService.updateUser(newUser);
        return success();
    }



    @UnAuthorized
    @ApiOperation(value = "导入修改人员组织架构")
    @PostMapping("users/import/dept")
    public void importUsersExcelUpdateDept(MultipartFile file) throws IOException {
        ImportMsg importMsg = new ImportMsg().setFileName(file.getOriginalFilename()).setImportStartTime(new Date());
        List<UserDeptExcel> userDeptExcels = analysisExcel(file, UserDeptExcel.class, 0);
        new UserDeptExcel().updateDeptCodes(userDeptExcels);
        long countSuccess = userDeptExcels.stream().filter(item -> item.getRemark().contains(SUCCESS)).count();
        long countFail = userDeptExcels.size() - countSuccess;
        Path xls = FileStoreUtils.createEmptyFile("xls", true);
        EasyExcel.write(xls.toFile(), UserDeptExcel.class).sheet("导入人员反馈信息").doWrite(userDeptExcels);
        importMsg.setImportErrorMsg(String.format("成功【%s】,失败【%s】,总条数【%s】", countSuccess, countFail, userDeptExcels.size())).setErrorFileName(StringUtils.replace(xls.toString(), FileStoreUtils.systemParentPath(), "")).setType("导入人员反馈信息");
        importMsg.setImportEndTime(new Date());
        iImportMsgService.save(importMsg);
    }
    @UnAuthorized
    @ApiOperation(value = "下载修改组织架构上传模板")
    @GetMapping("users/download/dept/template")
    public void downloadUserDeptExcel(HttpServletResponse response) throws IOException {
        String fileName = "user_dept_template.xls";
        Resource resource = new ClassPathResource("templates_core/excel/" + fileName);
        File xls = FileStoreUtils.createEmptyFile("xls", true).toFile();
        FileUtils.copyToFile(resource.getInputStream(), xls);
        excelDownload(response, xls);
    }

    //图片备份,此方法可以用，只不过用到定时任务里面了
//    @UnAuthorized
//    @GetMapping("back/photo")
//    public JSONObject backPhoto() throws IOException {
//        String backDir = FileStoreUtils.isWindows()?YamlConfigurerUtils.getStr("fileStore.back.windows"):YamlConfigurerUtils.getStr("fileStore.back.linux");
//        Files.createDirectories(Paths.get(backDir));
//        List<User> users = iUserService.findByPhotoFlag(1);
//        for (User user : users) {
//            String photo = user.getPhoto();
//            Path target = Paths.get(backDir + File.separator + user.getUserName() + ".jpg");
//            if (Files.exists(target)) {
//                Files.deleteIfExists(target);
//            }
//            Files.copy(Paths.get(FileStoreUtils.systemParentPath()+File.separator+photo), target);
//        }
//        return success();
//    }

    @UnAuthorized
    @ApiOperation(value = "退宿接口")
    @PostMapping("users/remove/{id}")
    public JSONObject removeUser(@PathVariable("id") Integer id){
        boolean b = iUserService.deleteUserAndBack(id, new Progress().setProgressFlag(StringUtilsEx.getUUID2()));
        return success(b);
    }


    @ApiOperation(value = "批量退宿接口")
    @PostMapping("users/removes")
    public JSONObject removeUsers(@RequestParam(value="ids",required=false)List<Integer> ids){
        String uuid2 = StringUtilsEx.getUUID2();
        Progress progress = new Progress();
        progress.setProgressFlag(uuid2);
        progress.setSize(ids.size());
        Progress progress1= iProgressService.save(progress);
        new Thread(()->removeStudents(progress1,ids)).start();
        return success(uuid2);
    }

    public void removeStudents(Progress progress,List<Integer> ids){
        int success=1;
        int fail=1;
        for (int i = 0; i < ids.size(); i++) {
            progress.setCurrentSize(i+1);
            boolean b = iUserService.deleteUserAndBack(ids.get(i),progress);
            if (b){
                progress.setSuccessSize(success);
                success++;
            }else {
                progress.setFailSize(fail);
                fail++;
            }
            iProgressService.save(progress);
        }
    }

    @UnAuthorized
    @ApiOperation(value = "导入人员，批量退宿")
    @PostMapping("users/import/removes")
    public void importUsersRemovesExcel(MultipartFile file) throws IOException {
        ImportMsg importMsg = new ImportMsg().setFileName(file.getOriginalFilename()).setImportStartTime(new Date());
        List<UserRemoveExcel> userRemoveExcels = analysisExcel(file, UserRemoveExcel.class, 0);
        new UserRemoveExcel().userRemoves(userRemoveExcels);
        long countSuccess = userRemoveExcels.stream().filter(item -> item.getRemark().contains(SUCCESS)).count();
        long countFail = userRemoveExcels.size() - countSuccess;
        Path path = FileStoreUtils.createEmptyFile("xls", true);
        EasyExcel.write(path.toFile(), UserRemoveExcel.class).sheet("导入学生，批量退宿舍反馈").doWrite(userRemoveExcels);
        importMsg.setImportErrorMsg(String.format("成功【%s】,失败【%s】,总条数【%s】", countSuccess, countFail, userRemoveExcels.size())).setErrorFileName(StringUtils.replace(path.toString(), FileStoreUtils.systemParentPath(), "")).setType("导入学生，批量退宿舍反馈").setImportEndTime(new Date());
        iImportMsgService.save(importMsg);
    }


    @UnAuthorized
    @ApiOperation(value = "下载批量退宿上传模板")
    @GetMapping("users/removes/download/template")
    public void downloadUserRemoveExcel(HttpServletResponse response) throws IOException {
        String fileName = "user_removes_template.xls";
        Resource resource = new ClassPathResource("templates_core/excel/" + fileName);
        Path xls = FileStoreUtils.createEmptyFile("xls", true);
        FileUtils.copyToFile(resource.getInputStream(), xls.toFile());
        excelDownload(response, xls.toFile());
    }
}
