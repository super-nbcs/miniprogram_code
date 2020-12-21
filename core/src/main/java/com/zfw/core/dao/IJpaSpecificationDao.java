package com.zfw.core.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 包含分页+排序查询，统计查询
 * @author zfw
 *
 * @param <T>
 */
@NoRepositoryBean
public interface IJpaSpecificationDao<T> extends JpaSpecificationExecutor<T>{

}
