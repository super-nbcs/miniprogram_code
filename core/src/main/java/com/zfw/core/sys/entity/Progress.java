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

/**
 * @Author:zfw
 * @Date:2020/6/23
 * @Content: 进度条
 */
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name ="t_progress")
@Where(clause="del_flag="+ BaseEntity.DEL_FLAG_NORMAL)
public class Progress extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "progress_flag", nullable = false,columnDefinition = "varchar(64) comment '进度标识，不能为空，存储uuid'")
    private String progressFlag;

    @Column(name = "name",columnDefinition = "varchar(64) comment '处理的进度的名字'")
    private String name;

    @Column(name="size",columnDefinition = "int comment '处理信息的总条数'")
    private Integer size;

    @Column(name = "handler_size",columnDefinition = "int comment '处理到第几条了'")
    private Integer currentSize;

    @Column(name="success_size",columnDefinition = "int comment '成功了几条'")
    private Integer successSize;

    @Column(name="fail_size",columnDefinition = "int comment '失败了几条'")
    private Integer failSize;

}
