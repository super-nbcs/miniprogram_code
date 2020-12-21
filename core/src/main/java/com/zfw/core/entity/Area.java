package com.zfw.core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Author:zfw
 * @Date:2020/02/12
 * @Content: 全国地区实体类
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Area {
    private Integer id;
}
