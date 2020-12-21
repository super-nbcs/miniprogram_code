package com.zfw.core.sys.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zfw.core.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * 角色-数据权限
 */
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Proxy(lazy = false)
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name ="sys_role_permission")
@Where(clause="del_flag="+ BaseEntity.DEL_FLAG_NORMAL)
public class RolePermission extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "role_id", nullable = false, columnDefinition = "int comment '角色Id'")
    private Integer roleId;

    @Column(name="dept_ids", nullable = false, columnDefinition = "text comment '部门id集合'")
    private String deptIds;
}
