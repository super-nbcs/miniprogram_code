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
@Table(name = "sys_login_log")
@Where(clause = "del_flag=" + BaseEntity.DEL_FLAG_NORMAL)
/**
 * 登录日志
 */
public class LoginLog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "remote_addr", columnDefinition = "varchar(255) comment '操作用户的IP地址'")
    private String remoteAddr;

    @Column(name = "user_agent", columnDefinition = "text comment '操作用户代理信息'")
    private String userAgent;

    @Column(name = "token", columnDefinition = "varchar(255) comment '用户登录的token'")
    private String token;

    @Column(name = "username", columnDefinition = "varchar(255) comment '用户姓名'")
    private String username;

    @Column(name = "userId", columnDefinition = "int comment '用户id'")
    private Integer userId;
}
