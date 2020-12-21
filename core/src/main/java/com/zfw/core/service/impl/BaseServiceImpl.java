package com.zfw.core.service.impl;

import com.zfw.core.constant.Constant;
import com.zfw.core.entity.Pager;
import com.zfw.core.exception.GlobalException;
import com.zfw.core.service.IBaseService;
import com.zfw.utils.JpaFilterUtils;
import com.zfw.utils.SubjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

import static com.zfw.core.constant.Constant.FAIL;

@NoRepositoryBean
public class BaseServiceImpl<T, ID> implements IBaseService<T, ID> {

    @Autowired
    private JpaRepository<T, ID> jpaRepository;

    @Autowired
    private JpaSpecificationExecutor<T> jpaSpecificationExecutor;

    @Autowired
    private SubjectUtils subjectUtils;

    @Override
    public T save(T t) {

        if (JpaFilterUtils.getFieldValueByFieldName("id", t)!=null){
            ID id = (ID)JpaFilterUtils.getFieldValueByFieldName("id", t);
            T to = jpaRepository.getOne(id);
            t = (T) JpaFilterUtils.copyNotNullProperties(t, to,t.getClass());
        }


        Class<?> aClass = t.getClass();
        Class<?> superclass = aClass.getSuperclass();
        if (superclass.getName().equals("com.com.huigou.core.Client.BaseEntity")) {
            try {
                Object id = JpaFilterUtils.getFieldValueByFieldName("id", t);
                if (id == null) {
                    Field createBy = superclass.getDeclaredField("createBy");
                    createBy.setAccessible(true);
                    try{
                        createBy.set(t, subjectUtils.getPrincipal().getId());
                    }catch (Exception e){
                        createBy.set(t, 1);
                    }
                }
                Field updateBy = superclass.getDeclaredField("updateBy");
                updateBy.setAccessible(true);
                try{
                    updateBy.set(t, subjectUtils.getPrincipal().getId());
                }catch (Exception e){
                    updateBy.set(t, 1);
                }
            } catch (NoSuchFieldException e) {
                throw new GlobalException(Constant.FAIL.CODE, "对象没有这个属性：createBy 或 updateBy", e.getMessage());
            } catch (IllegalAccessException e) {
                throw new GlobalException(Constant.FAIL.CODE, "通过反射设置属性：createBy 或 updateBy 的值时失败", e.getMessage());
            }

        }

        return jpaRepository.save(t);
    }

    @Override
    public List<T> save(List<T> ts) {
        return jpaRepository.saveAll(ts);
    }

    @Override
    public T getById(ID id) {
        if(!existsById(id)){
            return null;
        }
        return jpaRepository.getOne(id);
    }

    @Override
    public void deleteById(ID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public void delete(T t) {
        jpaRepository.delete(t);
    }


    @Override
    public boolean existsById(ID id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public List<T> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public List<T> findAllByIDs(List<ID> ids) {
        return jpaRepository.findAllById(ids);
    }

    @Override
    public T findOne(T t) {
        try {
            Optional<T> one = jpaRepository.findOne(Example.of(t));
            return one.get();
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new GlobalException(FAIL.CODE, FAIL.ZH_CODE + "通过查询条件，查询出多条记录", e.getMessage());
        }catch (NoSuchElementException e){
            return null;
        }
    }

    @Override
    public List<T> findAll(T t) {
        return jpaRepository.findAll(Example.of(t));
    }

    @Override
    public List<T> findAllAndSort(T t, Sort.Order... orders) {
        return jpaRepository.findAll(Example.of(t), Sort.by(JpaFilterUtils.filterSortProperty(t.getClass(), orders)));
    }

    @Override
    public Pager<T> findAll(Pageable pageable) {
        if (pageable.getPageNumber() < 1) {
            throw new GlobalException(FAIL.CODE, "操作失败，页码必须大于或等于1", "Page index must not be less than 1!");
        }


        //过虑无效排序参数
        Sort sort = pageable.getSort();
        List<Sort.Order> orders = new ArrayList<>();
        if (sort != null) {
            Iterator<Sort.Order> iterator = sort.iterator();
            while (iterator.hasNext()) {
                orders.add(iterator.next());
            }

            Type type = getClass().getGenericSuperclass();
            String className = ((ParameterizedType) type).getActualTypeArguments()[0].getTypeName();
            try {
                orders = JpaFilterUtils.filterSortProperty(Class.forName(className), orders);
            } catch (ClassNotFoundException e) {
                throw new GlobalException(FAIL.CODE, "类没有找到：" + className, e.getMessage());
            }

        }
        Page<T> all = jpaRepository.findAll(PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(), Sort.by(orders)));
        return Pager.of(all);
    }

    @Override
    public Pager<T> findAll(Pageable pageable, Sort.Order... orders) {
        if (orders == null || orders.length == 0)
            return this.findAll(pageable);
        return this.findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(orders)));
    }

    @Override
    public Pager<T> findAll(T t, Pageable pageable) {
        if (pageable.getPageNumber() < 1) {
            throw new GlobalException(FAIL.CODE, "操作失败，页码必须大于或等于1", "Page index must not be less than 1!");
        }
        Sort sort = pageable.getSort();
        List<Sort.Order> orders = new ArrayList<>();
        if (sort != null) {
            Iterator<Sort.Order> iterator = sort.iterator();
            while (iterator.hasNext()) {
                orders.add(iterator.next());
            }
            orders = JpaFilterUtils.filterSortProperty(t.getClass(), orders);
        }
        return Pager.of(jpaRepository.findAll(Example.of(t), PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(), Sort.by(orders))));
    }

    @Override
    public List<T> findAll(Sort.Order... orders) {
        Type type = getClass().getGenericSuperclass();
        String className = ((ParameterizedType) type).getActualTypeArguments()[0].getTypeName();
        try {
            return jpaRepository.findAll(Sort.by(JpaFilterUtils.filterSortProperty(Class.forName(className), orders)));
        } catch (ClassNotFoundException e) {
            throw new GlobalException(FAIL.CODE, "类没有找到：" + className, e.getMessage());
        }
    }

    @Override
    public Pager<T> findAll(Specification<T> spec, Pageable pageable) {
        if (spec==null){
            return findAll(pageable);
        }
        //过虑无效排序参数
        Sort sort = pageable.getSort();
        List<Sort.Order> orders = new ArrayList<>();
        if (sort != null) {
            Iterator<Sort.Order> iterator = sort.iterator();
            while (iterator.hasNext()) {
                orders.add(iterator.next());
            }

            Type type = getClass().getGenericSuperclass();
            String className = ((ParameterizedType) type).getActualTypeArguments()[0].getTypeName();
            try {
                orders = JpaFilterUtils.filterSortProperty(Class.forName(className), orders);
            } catch (ClassNotFoundException e) {
                throw new GlobalException(FAIL.CODE, "类没有找到：" + className, e.getMessage());
            }

        }
        return Pager.of(jpaSpecificationExecutor.findAll(spec, PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(), Sort.by(orders))));
    }

    @Override
    public List<T> findAll(Specification<T> spec) {
        if (spec==null){
            return findAll();
        }
        return jpaSpecificationExecutor.findAll(spec);
    }


}
