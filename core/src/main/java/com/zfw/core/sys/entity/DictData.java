package com.zfw.core.sys.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zfw.core.annotation.ParamValidate;
import com.zfw.core.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * @Author:zfw
 * @Date:2019/7/15
 * @Content:
 */
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name ="sys_dict_data", indexes = {@Index(columnList = "type_name")})
@Where(clause="del_flag="+ BaseEntity.DEL_FLAG_NORMAL)
public class DictData extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "type_name", columnDefinition = "varchar(64) comment '字典类型名'")
    private String typeName;	// 字典类型名

    @ParamValidate
    @Column(name = "value", columnDefinition = "varchar(255) comment '数据值'")
    private String value;	// 数据值

    @Column(name = "value_type", columnDefinition = "varchar(255) comment '值类型，比如数字类型，日期类型'")
    private String valueType;	// 值类型

    @ParamValidate
    @Column(name = "label", columnDefinition = "varchar(255) comment '标签名'")
    private String label;	// 标签名

    @Column(name = "status", columnDefinition = "int comment '0=正常,1=停用'")
    private int status;

    @Column(name = "is_sys", columnDefinition = "int comment '是否系统默认，0=系统级，1=用户级'")
    private int isSys;

    @Column(name = "description", columnDefinition = "varchar(255) comment '字典数据描述'")
    private String description;// 描述

}
