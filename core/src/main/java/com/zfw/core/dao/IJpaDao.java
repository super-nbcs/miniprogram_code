package com.zfw.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 此接口包含基础查询，分页查询，排序查询，集合查询
 * @author zfw
 *
 * @param <T>	实体类
 * @param <ID>	id 类型
 */
@NoRepositoryBean
public interface IJpaDao<T,ID> extends JpaRepository<T, ID> {

}
