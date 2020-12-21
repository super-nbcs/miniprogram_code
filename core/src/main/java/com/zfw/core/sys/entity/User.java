package com.zfw.core.sys.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zfw.core.annotation.ParamValidate;
import com.zfw.core.entity.BaseEntity;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @Author:zfw
 * @Date:2019/7/13
 * @Content:
 */
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Entity
@Proxy(lazy = false)
@Table(name = "sys_user", indexes = {@Index(columnList = "dept_code"),@Index(columnList = "user_name"),@Index(columnList = "photo_flag"),@Index(columnList = "mini_open_id")})
@Where(clause = "del_flag=" + BaseEntity.DEL_FLAG_NORMAL)
public class User extends BaseEntity {

    @ApiParam("用户id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ParamValidate
    @ApiParam("用户名")
    @Column(name = "user_name", unique = true, columnDefinition = "varchar(64) unique comment '用户名'")
    private String userName;

    @ParamValidate
    @ApiParam("姓名")
    @Column(name = "name", columnDefinition = "varchar(32) comment '姓名'")
    private String name;

    @ApiParam("身份证号码")
    @Column(name = "id_card", columnDefinition = "varchar(32) comment '身份证号码'")
    private String idCard;

    @ParamValidate
    @ApiParam("性别")
    @Column(name = "gender", columnDefinition = "int comment '性别'")
    private Integer gender;

    @ApiParam("手机号")
    @Column(name = "phone", columnDefinition = "varchar(11) comment '手机号'")
    private String phone;

    @ApiParam("邮箱")
    @Column(name = "email", columnDefinition = "varchar(100) comment '邮箱'")
    private String email;

    @ApiParam("人员照片地址")
    @Column(name = "photo", columnDefinition = "varchar(255) comment '人员照片地址'")
    private String photo;

    @ApiParam("部门id")
    @ParamValidate
    @Column(name = "dept_id", columnDefinition = "int comment '用户所属部门id，关联dept表'")
    private Integer deptId;

    @ApiParam("部门全路径")
    @Column(name = "dept_path", columnDefinition = "varchar(255) comment '部门全路径'")
    private String deptPath;

    @ApiParam("部门code")
    @Column(name = "dept_code", nullable = false, columnDefinition = "varchar(64) comment '部门code'")
    private String deptCode;

    @Column(name="mini_open_id",unique = true,columnDefinition = "varchar(64) comment '小程序openId'")
    private String miniOpenId;

    @JsonIgnore
    @Column(name = "salt_password", columnDefinition = "varchar(255) comment '盐值密码'")
    private String saltPassword;

    @Column(name = "can_login", columnDefinition = "int default 0 comment '0能，1不能。默认能登录'")
    private Integer canLogin;

    @JsonFormat(pattern = BaseEntity.DATE_FORMAT, timezone = "GMT+8")
    @Column(name = "last_login_date", columnDefinition = "timestamp default now() comment '上次登录时间'")
    private Date lastLoginDate;


    //    密码，此属性只用作参数接收，数据库中不存储用户的明文密码
    @JsonIgnore
    @Transient
    private String password;

    @Transient
    private String token;

    //    获取当前用户所有对应的角色
    @Transient
    private Set<Role> roles;

    //    获取用户所有对应的菜单权限
    @Transient
    private List<Menu> menus;

    //所有角色id，接收前端参数
    @Formula("(select model.roleId from sys_user_role model where model.userId=id)")
    private Integer roleId;

    @Formula("(select model.name from sys_role model,sys_user_role model1 where model.id=model1.roleId and model1.userId=id)")
    private String roleName;

    @ApiParam("是否有照片")
    @Column(name = "photo_flag", columnDefinition = "int comment '是否有照片，0：无，1：有。加这个字段就是为了查询快'")
    private Integer photoFlag;

    @PrePersist
    protected void onPersist() {
        if (StringUtils.isNotBlank(photo))
            photoFlag=1;
        else
            photoFlag=0;
    }
    @PreUpdate
    protected void onUpDate() {
        if (StringUtils.isNotBlank(photo))
            photoFlag=1;
        else
            photoFlag=0;
    }

}
