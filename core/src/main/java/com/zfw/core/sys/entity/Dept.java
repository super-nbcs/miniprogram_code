package com.zfw.core.sys.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zfw.core.entity.BaseEntity;
import com.zfw.core.entity.TreeEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

/**
 * 组织架构表
 */
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "sys_dept",indexes = {@Index(columnList = "code"),@Index(columnList = "level"),@Index(columnList = "path")})
@Where(clause = "del_flag=" + BaseEntity.DEL_FLAG_NORMAL)
public class Dept extends BaseEntity implements TreeEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", columnDefinition = "varchar(64) comment '部门(组织结构)名称'")
    private String name;

    @Column(name = "code", unique = true,columnDefinition = "varchar(128) comment '部门编号'")
    private String code;

    @Column(name = "parent_id", nullable = false, columnDefinition = "int default 0 comment '父Id'")
    private Integer parentId;

    @Formula(value = "(select model.name from sys_dept model where model.id = parent_id)")
    private String parentName;

    @Column(name = "child_ids", columnDefinition = "varchar(10000) comment '所有孩子节点的id'")
    private String childIds;

    @Column(name = "leader", columnDefinition = "varchar(32) comment '部门(组织结构)负责人'")
    private String leader;

    @Column(name = "phone", columnDefinition = "varchar(32) comment '部门(组织结构)电话'")
    private String phone;

    @Column(name = "email", columnDefinition = "varchar(64) comment '部门(组织结构)邮箱'")
    private String email;

    @Column(name = "dtl", columnDefinition = "varchar(255) comment '部门(组织结构)描述'")
    private String dtl;

    @Column(name = "loc", columnDefinition = "varchar(255) comment '部门(组织结构)位置'")
    private String loc;

    @Column(name = "path",columnDefinition = "varchar(128) comment '部门名全路径'")
    private String path;

    @Column(name = "flag", columnDefinition = "int comment '组织标志，值在字典表中'")
    private Integer flag;

    @Column(name = "level", columnDefinition = "int comment '组织层级'")
    private Integer level;

    @Column(name = "color", columnDefinition = "varchar(64) DEFAULT '#000000'  comment '颜色'")
    private String color="#000000";

    public Integer getLevel() {
        return this.getCode().length()/2;
    }
    @PrePersist
    protected void onPersist() {
        level=this.getCode().length()/2;
    }
    @PreUpdate
    protected void onUpDate() {
        level=this.getCode().length()/2;
    }

    @Formula(value = "( exists(select 1 from sys_dept model where model.parent_id = id) )")
    private boolean hasChildren;

    @Override
    public boolean isHasChildren() {
        return hasChildren;
    }

    public boolean isLeaf() {
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
    private List<Dept> children;

}
