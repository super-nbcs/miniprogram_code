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
 * 职位表
 */
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name ="sys_duty")
@Where(clause="del_flag="+ BaseEntity.DEL_FLAG_NORMAL)
public class Duty extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ParamValidate
    @Column(name = "name", columnDefinition = "varchar(32) comment '职位名称'")
    private String name;

    @ParamValidate
    @Column(name = "code", columnDefinition = "varchar(64) comment '职位编号'")
    private String code;

    @Column(name="dtl",columnDefinition = "varchar(128) comment '职位描述'")
    private String dtl;

}
