package com.zfw.core.dao;

import org.springframework.data.repository.NoRepositoryBean;

/**
 * 公用Dao包含基础查询,条件查询,分页查询,排序查询,分页+排序,统计查询
 * 业务层Dao接口直接继承此接口
 * @author zfw
 *
 * @param <T>	实体类
 * @param <ID>	id类型
 */
@NoRepositoryBean
public interface ICommonDao<T,ID> extends IJpaDao<T, ID>, IJpaSpecificationDao<T> {

}
