package com.zfw.core.service;

import com.zfw.core.entity.Pager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface IBaseService<T, ID> {
    /**
     * 插入实体
     *
     * @param t
     */
    T save(T t);


    /**
     * 批量插入
     *
     * @param ts
     * @return
     */
    List<T> save(List<T> ts);

    /**
     * 通过ID进行查询
     *
     * @param id
     * @return
     */
    T getById(ID id);

    /**
     * 通过id删除
     *
     * @param id
     */
    void deleteById(ID id);

    void delete(T t);

    /**
     * 判断id是否存在
     *
     * @param id
     * @return
     */
    boolean existsById(ID id);

    /**
     * 查询全部
     *
     * @return
     */
    List<T> findAll();

    /**
     * 通过Ids 查询所有
     *
     * @param ids 没有的id 自动过滤，不查询，不抛异常
     * @return
     */
    List<T> findAllByIDs(List<ID> ids);

    /**
     * 通过实体类查询出一个结果
     *
     * @param t 如：t.setName("aa").setSex(0)
     * @return t
     */
    T findOne(T t);

    /**
     * 通过实体类查询出所有匹配结果
     *
     * @param t 如：t.setName("aa").setSex(0)
     * @return
     */
    List<T> findAll(T t);

    /**
     * 通过实体类查询出所有匹配结果,并排序
     *
     * @param t      如：t.setName("aa").setSex(0)
     * @param orders Sort.Order.desc("id")   Sort.Order.asc("path")
     * @return
     */
    List<T> findAllAndSort(T t, Sort.Order... orders);

    /**
     * 分页查询 页码是从1开始，原生的是从0开始
     *
     * @param pageable PageRequest.of(1, 10)
     * @return
     */
    Pager<T> findAll(Pageable pageable);

    /**
     * 分页排序查询 页码是从1开始，原生的是从0开始
     *
     * @param pageable PageRequest.of(1, 10)
     * @param orders   Sort.Order.desc("id")
     * @return
     */
    Pager<T> findAll(Pageable pageable, Sort.Order... orders);


    /**
     * 条件分页排序查询 ,自动过虑无效排序参数， 页码是从1开始，原生的是从0开始
     * @param t   如：t.setName("aa").setSex(0)
     * @param pageable PageRequest.of(1, 10)  或 PageRequest.of(1, 10,Sort.by(Sort.Order.asc("id"),Sort.Order.asc("name")));
     * @return
     */
    Pager<T> findAll(T t, Pageable pageable);
    /**
     * 排序查询
     *
     * @param orders Sort.Order.desc("id")   Sort.Order.asc("path")
     * @return
     */
    List<T> findAll(Sort.Order... orders);

    /**
     * 模糊搜索，分页
     * @param spec
     * @param pageable
     * @return
     */
    Pager<T> findAll(Specification<T> spec, Pageable pageable);

    /**
     * 模糊搜索，不分页
     * @param spec
     * @return
     */
    List<T> findAll(Specification<T> spec);
}
