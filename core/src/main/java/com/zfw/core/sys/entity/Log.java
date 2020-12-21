package com.zfw.core.sys.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zfw.core.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "sys_log")
@Where(clause = "del_flag=" + BaseEntity.DEL_FLAG_NORMAL)
public class Log  extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "type", columnDefinition = "varchar(255) comment '日志类型（1：接入日志；2：错误日志）'")
    private String type;

    @Column(name = "title", columnDefinition = "varchar(255) comment '日志标题'")
    private String title;

    @Column(name = "remote_addr", columnDefinition = "varchar(255) comment '操作用户的IP地址'")
    private String remoteAddr;

    @Column(name = "request_uri", columnDefinition = "varchar(255) comment '操作的URI'")
    private String requestUri;

    @Column(name = "method", columnDefinition = "varchar(255) comment '操作的方式'")
    private String method;

    @Column(name = "params", columnDefinition = "text comment '操作提交的数据'")
    private String params;

    @Column(name = "user_agent", columnDefinition = "varchar(255) comment '操作用户代理信息'")
    private String userAgent;

    @Column(name = "ops_time", columnDefinition = "int comment '操作执行时长'")
    private int opsTime;

    @Column(name = "exception", columnDefinition = "text comment '异常信息'")
    private String exception;
}
