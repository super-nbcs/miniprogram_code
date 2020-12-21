package com.zfw.core.sys.entity;

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
 * @Date:2019/7/13
 * @Content:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name ="sys_token")
@Where(clause="del_flag="+ BaseEntity.DEL_FLAG_NORMAL)
public class Token extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id",columnDefinition = "int comment '用户id'")
    private Integer userId;

    @Column(name = "token",columnDefinition = "varchar(255) comment '用户登录成功后生成token'")
    private String token;

}
