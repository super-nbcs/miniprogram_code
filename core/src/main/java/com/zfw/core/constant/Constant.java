package com.zfw.core.constant;

import org.springframework.http.HttpStatus;

import javax.persistence.AttributeConverter;

/**
 * 常量
 */
public enum Constant {
    SHOW(0, "展示", "show"),
    HIDDEN(1, "隐藏", "hidden"),


    ENABLED(2, "可用", "enabled"),
    DISABLED(3, "不可用", "disabled"),


    NOT_FOUND(HttpStatus.NOT_FOUND.value(), "页面没找到", HttpStatus.NOT_FOUND.getReasonPhrase()),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), "无权限访问", HttpStatus.UNAUTHORIZED.getReasonPhrase()),


    MAN(4, "男", "man"),
    WOMAN(5, "女", "woman"),

    CLIENT(6, "客户", "client"),
    EMPLOYEE(7, "员工", "employee"),
    VIP(8, "会员", "vip"),



    SUPPLIER(9, "供应商", "supplier"),
    PROJECT(10, "项目", "project"),

    IN(11, "进", "in"),
    OUT(12, "出", "out"),

    adopt(13, "通过", "adopt"),
    Not_pass(14, "不通过", "Not pass"),


    IMPORT_UNTREATED(20,"导入反馈信息未处理","Untreated"),
    IMPORT_PROCESSED(21,"导入反馈信息待处理","Processed"),


    /**
     * -----------------自定义状态码-------------------
     **/
    SUCCESS(HttpStatus.OK.value(), "操作成功", HttpStatus.OK.getReasonPhrase()),
    FAIL(90000, "操作失败", "fail"),
    FAIL_NULL(90001, "对象不能为null", "not null object"),


    /**
     * 此异常的错误码用于自定义反馈信息
     */
    FAIL_MSG(90002,"","Custom exception information"),



    NOT_NULL_ID(40003, "id不能为空", "not null id"),
    NOT_FOUND_ID(40004, "id不存在:", "not found id:"),


    /**
     * -----------------角色相关异常状态码-------------------200开头
     **/
    CODE_20011(20011, "角色名已存在", "The role name already exists"),
    CODE_20012(20012, "角色编码已存在", "The student did not upload the photo"),
    CODE_20013(20013, "根据角色code没有找到该角色", "The role was not found according to the role code"),
    CODE_20014(20014, "根据角色id没有找到该角色", "The role was not found according to the role code"),


    /**
     * -----------------用户相关异常状态码-------------------100开头
     **/
    CODE_10004(10004, "此用户禁止登录", "user no login authorized"),
    CODE_10005(10005, "缓存服务器出现异常", "Cache java.com.com.huigou.server exception"),
    CODE_10006(10006, "排序规则参数（sort）的值不正确：正确值：desc或asc", "The value of the sort parameter is incorrect: the correct value: desc or asc"),
    CODE_10007(10007, "原始密码不正确", "error old password"),
    CODE_10008(10008, "登录已过期，请重新登录", "Logon has expired"),
    CODE_10009(10009, "非法请求验证", "Illegal authentication request"),
    CODE_10010(10010, "未登录或登录已过期", "not login"),
    CODE_10011(10011, "用户名已存在", "already exists userName"),
    CODE_10012(10012, "用户名不存在", "not found userName"),
    CODE_10013(10013, "用户名或密码错误", "error:userName or password"),
    CODE_10014(10014, "密码不能为空", "not null user password"),
    CODE_10015(10015, "用户名不能为空", "not null user name"),
    CODE_10016(10016, "验证码不存在或失效", "The verification code does not exist or is invalid"),
    CODE_10017(10017, "验证码输入不正确", "Verification code check is incorrect"),
    CODE_10018(10018, "根据id没有找到对应的字典", "No dictionary was found based on id"),
    CODE_10019(10019, "已经有该Code的字典，请修正后插入", "The dictionary of this Code already exists, please correct it and insert it"),
    CODE_10020(10020, "照片地址不能为空", "The photo address cannot be empty"),
    CODE_10021(10021, "时间转换异常", "Time transition anomaly"),
    CODE_10022(10022, "性别不能为空", "Gender cannot be null"),
    CODE_10023(10023, "设备中存在人员，无法删除，请先清空设备人员信息", "There are people in the device and cannot be deleted"),
    CODE_10024(10024, "该职位下存在人员，无法删除，请先删除该职位下的人员信息", "There are people under this position and cannot be deleted"),
    CODE_10025(10025, "该部门下存在人员，无法删除，请先删除该部门下的人员信息", "There are people under this department and cannot be deleted"),
    CODE_10026(10026, "工号已存在", "The number already exists"),
    CODE_10027(10027, "职位不存在", "The position doesn't exist"),
    CODE_10028(10028, "部门不存在", "Department does not exist"),
    CODE_10029(10029, "根据设备id没有找到对应的设备", "com.com.huigou.device.Device does not exist"),
    CODE_10030(10030, "图片文件转base64字符失败", "The image file failed to transfer base64 characters"),
    CODE_10031(10031, "工号不可修改", "The work number cannot be modified"),
    CODE_10032(10032, "根据部门id没有找到对应的部门", "Department does not exist"),
    CODE_10033(10033, "手机号不能重复", "The phone number must not be repeated"),
    CODE_10034(10034, "时段名称已存在", "The period name already exists"),
    CODE_10035(10035, "时段开始时间不能为空", "The start time of the session cannot be empty"),
    CODE_10036(10036, "时段结束时间不能为空", "The end time of the session cannot be empty"),
    CODE_10037(10037, "音频文件地址不能为空", "The audio address cannot be empty"),
    CODE_10038(10038, "时段时间格式不正确", "The time format is incorrect"),


    ;





    public int CODE;
    public String ZH_CODE;
    public String EN_CODE;

    Constant(int CODE, String ZH_CODE, String EN_CODE) {
        this.CODE = CODE;
        this.ZH_CODE = ZH_CODE;
        this.EN_CODE = EN_CODE;
    }

    public static class Convert implements AttributeConverter<Constant, Integer> {

        @Override
        public Integer convertToDatabaseColumn(Constant attribute) {
            return attribute == null ? null : attribute.CODE;
        }

        @Override
        public Constant convertToEntityAttribute(Integer dbData) {
            if (dbData == null)
                return null;
            for (Constant isShow : Constant.values()) {
                if (dbData.equals(isShow.CODE))
                    return isShow;
            }
            throw new RuntimeException("没有定义：" + dbData);
        }
    }
}
