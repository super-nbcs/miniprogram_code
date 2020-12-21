package com.zfw.core.sys.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zfw.core.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * @Author:zfw
 * @Date:2019/7/15
 * @Content: 字典类型
 */
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name ="sys_dict_type", indexes = {@Index(columnList = "type_name")})
@Where(clause="del_flag="+ BaseEntity.DEL_FLAG_NORMAL)
public class DictType extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 字典类型名称 */
    @Column(name = "type_name",unique = true,columnDefinition = "varchar(64) comment '字典类型名称'")
    private String typeName;

    @Column(name = "status", columnDefinition = "int comment '字典状态，0：正常，1禁用'")
    private Integer status;

    @Column(name = "description", columnDefinition = "varchar(255) comment '字典类型描述'")
    private String description;// 描述

}
