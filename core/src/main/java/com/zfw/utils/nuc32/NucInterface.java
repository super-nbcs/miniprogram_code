package com.zfw.utils.nuc32;

public enum NucInterface {

    /**-----------------旷视路径-------------------**/

    KOALA_1_1("POST","/auth/login","登录"),
    KOALA_2_1("GET","/subjects","获取所有用户列表"),
    KOALA_2_2("GET","/mobile-admin/subjects/list","获取用户列表"),
    KOALA_2_3("POST","/subject","创建用户"),
    KOALA_2_4("GET","/subject/","获取用户信息"),
    KOALA_2_5("PUT","/subject/","更新用户信息"),
    KOALA_2_6("DELETE","/subject/","删除用户"),
    KOALA_2_7("POST","/subject/photo","上传识别照片"),
    KOALA_2_8("DELETE","/subject/photo"," 删除用户底库接口"),
    KOALA_2_9("POST","/subject/photo/check","入库图片质量判断接口"),
    KOALA_2_10("POST","/subject/file","创建用户，支持直接上传图片"),
    KOALA_3_1("POST","/subject/avatar","上传显示头像"),
    KOALA_4_1("GET","/system/screen","获取门禁列表"),
    KOALA_4_2("GET","/system/screen/","获取门禁信息"),
    KOALA_4_3("GET","/system/boxes","获取所有可用本地主机"),
    KOALA_4_4("POST","/system/screen","创建一个门禁"),
    KOALA_4_5("PUT","/system/screen/","更新门禁信息"),
    KOALA_4_6("DELETE","/system/screen/","删除门禁"),
    KOALA_4_7("PUT","/subject/bind_camera","用户绑定门禁"),
    KOALA_4_8("DELETE","/subject/bind_camera","用户解绑门禁"),
    KOALA_5_1("GET","/event/events","历史识别记录"),
    KOALA_5_2("POST","/event/scan","历史记录遍历接口"),
    KOALA_6_1("GET","/attendance/records","考勤记录"),
    KOALA_8_1("POST","/pad/login","登录"),
    KOALA_8_2("POST","/pad/add-visitor","添加访客"),
    KOALA_8_3("PUT","/pad/set-info","设置Pad信息"),
    KOALA_10_1("POST","/recognize","1比N识别"),
    KOALA_10_2("POST","/checkin","1比1认证"),
    KOALA_11_1("GET","/c4r/screens/group/list","获取门禁分组列表"),
    KOALA_11_2("GET","/c4r/screens/group/","获取门禁组以及所属门禁"),
    KOALA_11_3("POST","/c4r/screens/group","创建门禁分组"),
    KOALA_11_4("PUT","/c4r/screens/group/","更新门禁分组"),
    KOALA_11_5("DELETE","/c4r/screens/group/","删除门禁分组"),
    KOALA_11_6("POST","/c4r/screens/group/","添加门禁到门禁分组(需加路径后缀/insert)"),
    KOALA_11_7("POST","/c4r/screens/group/","从门禁分组中删除门禁(需加路径后缀/delete)"),
    KOALA_12_1("GET","/subjects/group/list","获取人员分组列表"),
    KOALA_12_2("GET","/subjects/group/","获取人员组以及所属人员"),
    KOALA_12_3("PUT","/subjects/group","创建人员分组"),
    KOALA_12_4("POST","/subjects/group/","更新人员分组"),
    KOALA_12_5("POST","/subjects/group/","添加人员到人员分组(需加路径后缀/insert)"),
    KOALA_12_6("POST","/subjects/group/","从人员分组中删除人员/delete)"),
    KOALA_12_7("DELETE","/access/subjects/list","删除人员分组"),
    KOALA_13_1("PUT","/access/setting","创建门禁设置"),
    KOALA_13_2("DELETE","/access/setting/","删除门禁设置"),
    KOALA_13_3("POST","/access/setting/","更新门禁设置"),
    KOALA_13_4("GET","/access/setting/list","获取门禁设置列表"),
    KOALA_14_1("GET","/access/schedule/list","获取时段列表"),
    KOALA_14_2("PUT","/access/schedule","创建时段规则"),
    KOALA_14_3("POST","/access/schedule/","更新时段规则"),
    KOALA_14_4("DELETE","/access/schedule/","删除时段规则"),
    KOALA_15_1("GET","/access/calendar/list","获取假日列表"),
    KOALA_15_2("PUT","/access/calendar","创建假日规则"),
    KOALA_15_3("POST","/access/calendar/","更新假日规则"),
    KOALA_15_4("DELETE","/access/calendar/","删除假日规则"),
    KOALA_15_5("POST","/recognize","静态1比N识别"),



    ;

    public String TYPE;
    public String PATH;
    public String DESCRIBE;

    NucInterface(String TYPE, String PATH, String DESCRIBE) {
        this.TYPE = TYPE;
        this.PATH = PATH;
        this.DESCRIBE = DESCRIBE;
    }
}
