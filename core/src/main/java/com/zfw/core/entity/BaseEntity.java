package com.zfw.core.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 基础Entity,新建实体类时继承此类
 */

@Data
@MappedSuperclass
public class BaseEntity implements Serializable {
    // protected Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     *
     */

    private static final long serialVersionUID = 1L;

    public final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final int DEL_FLAG_NORMAL = 0;
    public static final int DEL_FLAG_DELETE = 1;

    @Column(name = "create_by", updatable = false,columnDefinition = "int comment '创建用户的id'")
    private Integer createBy;

    @Column(name = "update_by", columnDefinition = "int comment '更新用户的id'")
    private Integer updateBy;

    @JsonIgnore
    @Column(name = "del_flag", nullable = false, insertable = false, columnDefinition = "int default 0 comment '删除标记，默认0'")
    private int delFlag;

    @Column(name = "sort", columnDefinition = "int comment '排序'")
    private Integer sort;

    @JsonFormat(pattern = DATE_FORMAT, timezone = "GMT+8")
    @Column(name = "create_date", nullable = false, insertable = false, updatable = false, columnDefinition = "timestamp default now() comment '创建时间'")
    private Date createDate;

    @JsonFormat(pattern = DATE_FORMAT, timezone = "GMT+8")
    @Column(name = "update_date", nullable = false, insertable = false, columnDefinition = "timestamp default now() comment '修改时间'")
    private Date updateDate;
    @Column(name = "ops_server", columnDefinition = "varchar(64) comment '哪个服务操作的'")
    private String opsSever;

    @Column(name = "remarks", columnDefinition = "varchar(255) comment '备注'")
    private String remarks;

    @PrePersist
    protected void onPersist() {
        createDate=new Date();
        updateDate = new Date();
    }

    @PreUpdate
    protected void onUpDate() {
        this.updateDate = new Date();
    }

    @PreRemove
    protected void onRemove() {
        // logger.debug("+++++++++++++++++++++++++++++");
    }

}
