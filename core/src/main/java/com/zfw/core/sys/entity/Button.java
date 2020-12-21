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
 * @Author:zfw
 * @Date:2020/1/6
 * @Content:
 */
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "sys_button")
@Where(clause = "del_flag=" + BaseEntity.DEL_FLAG_NORMAL)
public class Button extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ParamValidate
    @Column(name = "btn_name", columnDefinition = "varchar(32) comment '按钮名'")
    private String btnName;

    @ParamValidate
    @Column(name = "menu_id",columnDefinition = "int default 0 comment '此按钮所属菜单Id'")
    private Integer menuId;


    @Column(name = "btn_role",columnDefinition = "varchar(255) comment '按钮所具的角色'")
    private String btnRole;

    @ParamValidate
    @Column(name="btn_sign",nullable = false,unique=true,columnDefinition = "varchar(16) comment '标记按钮，全库唯一'")
    private String btnSign;

}
