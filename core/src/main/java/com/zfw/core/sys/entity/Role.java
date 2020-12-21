package com.zfw.core.sys.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zfw.core.annotation.ParamValidate;
import com.zfw.core.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Set;

@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Entity
@Proxy(lazy = false)
@Table(name ="sys_role")
@Where(clause="del_flag="+ BaseEntity.DEL_FLAG_NORMAL)
public class Role extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="code")
    @ParamValidate
    private String code;

    @Column(name="code_zh",length=124,columnDefinition = "varchar(255) comment '中文简称'")
    private String codeZH;

    @Column(name="code_en",columnDefinition="varchar(255) comment '英文简称'")
    private String codeEN;

    @ParamValidate
    @Column(name="name",columnDefinition="varchar(255) comment '角色名'")
    private String name;

    @ParamValidate
    @Column(name="dtl",columnDefinition="varchar(255) comment '角色详情'")
    private String dtl;

    //获取当前角色所有的菜单权限
    @Transient
    private Set<Menu> menus;
}
