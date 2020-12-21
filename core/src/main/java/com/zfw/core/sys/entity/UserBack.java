package com.zfw.core.sys.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zfw.core.annotation.ParamValidate;
import com.zfw.core.entity.BaseEntity;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author:zfw
 * @Date:2019/7/13
 * @Content: 退宿记录表
 */
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "sys_user_back", indexes = {@Index(columnList = "dept_code"),@Index(columnList = "user_name")})
@Where(clause = "del_flag=" + BaseEntity.DEL_FLAG_NORMAL)
public class UserBack extends BaseEntity {

    @ApiParam("用户id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ParamValidate
    @ApiParam("用户名")
    @Column(name = "user_name", columnDefinition = "varchar(64) comment '用户名'")
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

    @ApiParam("用户职位")
    @Column(name="duty_name",columnDefinition = "varchar(32) comment '用户职位'")
    private String dutyName;


    @Column(name = "building_code", columnDefinition = "varchar(128) comment '用户所住的宿舍楼的code'")
    private String buildingCode;
    @Column(name = "building_path", columnDefinition = "varchar(255) comment '用户所住的宿舍楼的path'")
    private String buildingPath;

    @Column(name = "can_login", columnDefinition = "int default 0 comment '0能，1不能。默认能登录'")
    private Integer canLogin;

    @JsonFormat(pattern = BaseEntity.DATE_FORMAT, timezone = "GMT+8")
    @Column(name = "last_login_date", columnDefinition = "timestamp default now() comment '上次登录时间'")
    private Date lastLoginDate;

    private Integer roleId;
    private String roleName;
    private String buildingCodesStr;

    @Column(name = "grade",columnDefinition = "varchar(8) comment '冗余一个年级字段，方便前端查询'")
    private String grade;

}
