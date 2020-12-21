package com.zfw.core.constant;

public enum RoleConstant {
    Role_1("employee","员工"),
    Role_2("manager","部门管理员"),


    ;



    public String  NAME;
    public String DESCRIBE;

    RoleConstant(String NAME, String DESCRIBE) {
        this.NAME = NAME;
        this.DESCRIBE = DESCRIBE;
    }
}
