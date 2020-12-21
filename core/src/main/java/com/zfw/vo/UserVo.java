package com.zfw.vo;

import com.zfw.core.sys.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author snow
 * @Date 2020/7/22 11:32
 * @Description: 人员视图数据vo
 */
@Data
public class UserVo extends User {
    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getSaltPassword() {
        return null;
    }
}
