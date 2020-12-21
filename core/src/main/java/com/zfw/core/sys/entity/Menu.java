package com.zfw.core.sys.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zfw.core.annotation.ParamValidate;
import com.zfw.core.constant.Constant;
import com.zfw.core.entity.BaseEntity;
import com.zfw.core.entity.TreeEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Entity
@Proxy(lazy = false)
@Table(name = "sys_menu")
@Where(clause = "del_flag=" + BaseEntity.DEL_FLAG_NORMAL)
public class Menu extends BaseEntity implements TreeEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", columnDefinition = "varchar(255) comment '菜单名'")
    private String name;

    @ParamValidate
    @Column(name = "parent_id", nullable = false, columnDefinition = "int default 0 comment '父Id'")
    private Integer parentId;

    @Column(name = "path", columnDefinition = "varchar(255) comment '菜单路径，路由地址'")
    private String path;

    @Column(name = "icon", columnDefinition = "varchar(255) comment '图标'")
    private String icon;

    @Column(name = "is_show", nullable = false, columnDefinition = "varchar(16) comment '是否展示,0展示，1隐藏'")
    @Convert(converter = Constant.Convert.class)
    private Constant isShow = Constant.SHOW;

    @Formula(value = "( exists(select 1 from sys_menu model where model.parent_id = id) )")
    private boolean hasChildren;

    @Column(name = "level" ,columnDefinition = "int comment '菜单级别，如一级菜单'")
    private Integer level;

    @Column(name = "component_path",columnDefinition = "varchar(255) comment '组件路径，views下的全路径'")
    private String componentPath;

    @Column(name="meta",columnDefinition = "varchar(255) comment '组件名的meta内容'")
    private String meta;

    @Column(name="title",columnDefinition = "varchar(255) comment '组件标题'")
    private String title;

    @Override
    public boolean isHasChildren() {
        return hasChildren;
    }

    public boolean isLeaf(){
        return !hasChildren;
    }
    @Override
    public boolean isRoot() {
        int parentId = this.getParentId();
        if (parentId == 0) {
            return true;
        }
        return false;
    }
    @Transient
    private List<Menu> children;

}
