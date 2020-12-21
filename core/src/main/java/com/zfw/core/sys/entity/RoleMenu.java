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
@Proxy(lazy = false)
@Table(name ="sys_role_menu")
@Where(clause="del_flag="+ BaseEntity.DEL_FLAG_NORMAL)
public class RoleMenu extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "role_id")
    private Integer roleId;

    @Column(name="menu_id")
    private Integer menuId;

}
