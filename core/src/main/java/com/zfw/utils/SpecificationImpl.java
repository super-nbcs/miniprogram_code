package com.zfw.utils;


import com.zfw.core.constant.Constant;
import com.zfw.core.convert.DateConvert;
import com.zfw.core.exception.GlobalException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.text.ParseException;
import java.util.*;

/**
 * @Author:zfw
 * @Date:2020-12-22
 * @Content:
 */
public class SpecificationImpl<T> implements Specification<T> {
    public static String LIKE = "like";
    public static String BETWEEN = "至";
    Map<String, List<String>> params;

    public SpecificationImpl(Map<String, List<String>> params) {
        this.params = params;
    }
    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> list = new ArrayList<>();
        List<Predicate> listOr = new ArrayList<>();
        Set<Map.Entry<String, List<String>>> entries = params.entrySet();
        for (Map.Entry<String, List<String>> entry : entries) {
            String k = entry.getKey();
            List<String> v = entry.getValue();
            Path<String> objectPath = root.get(k);
            Class<? extends Class<? extends String>> javaType = objectPath.type().getJavaType();


            //处理日期类型
            if (javaType.equals(Date.class)) {
                if (v.get(0).contains(BETWEEN)) {
                    String[] between = v.get(0).split(BETWEEN);
                    try {
                        Date startDate = DateUtils.parseDateStrictly(between[0], DateConvert.pattern);
                        Date endDate = DateUtils.parseDateStrictly(between[1], DateConvert.pattern);
                        list.add(criteriaBuilder.between(objectPath.as(Date.class), startDate, endDate));
                    } catch (ParseException e) {
                        throw new GlobalException(Constant.FAIL.CODE, Constant.FAIL.ZH_CODE + ":日期格式为" + Arrays.toString(DateConvert.pattern) + "中的一个，推荐【yyyy-MM-dd HH:mm:ss】", Constant.FAIL.EN_CODE);
                    }
                } else {
                    throw new GlobalException(Constant.FAIL.CODE, Constant.FAIL.ZH_CODE + ":请遵守规定时间查询格式", Constant.FAIL.EN_CODE);
                }
            }
            //处理枚举类型
            if (javaType.isEnum()) {
                Constant[] values = Constant.values();
                for (Constant constant : values) {
                    String name = constant.name();
                    if (name.equals(v.get(0))) {
                        list.add(criteriaBuilder.equal(objectPath.as(String.class), constant.CODE));
                    }
                }
            }
            //处理字符串类型
            if (javaType.equals(String.class)) {
                if (v.size() == 1) {
                    if (StringUtils.endsWith(v.get(0), LIKE)) {
                        listOr.add(criteriaBuilder.like(root.get(k).as(String.class), StringUtils.removeEnd(v.get(0), LIKE) + "%"));
                    } else {
                        list.add(criteriaBuilder.equal(root.get(k).as(String.class), v.get(0)));
                    }
                } else {
                    for (String value : v) {
                        if (StringUtils.endsWith(value, LIKE)) {
                            Predicate like = criteriaBuilder.like(root.get(k).as(String.class), StringUtils.removeEnd(value, LIKE) + "%");
                            listOr.add(like);
                        } else {
                            CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get(k).as(String.class));
                            in.value(value);
                            list.add(in);
                        }

                    }
                }
            }
            //处理integer类型
            if (javaType.equals(Integer.class)) {
                if (StringUtils.endsWith(v.get(0), LIKE)) {
                    list.add(criteriaBuilder.like(root.get(k).as(String.class), "%" + StringUtils.removeEnd(v.get(0), LIKE) + "%"));
                } else {
                    if (v.size() == 1) {
                        list.add(criteriaBuilder.equal(root.get(k).as(Integer.class), v.get(0)));
                    } else if (v.size() > 1) {
                        CriteriaBuilder.In<Integer> in = criteriaBuilder.in(root.get(k).as(Integer.class));
                        v.forEach(value -> in.value(Integer.valueOf(value)));
                        list.add(in);
                    }
                }
            }
        }
        Predicate[] p = new Predicate[list.size()];
        Predicate and = criteriaBuilder.and(list.toArray(p));
        Predicate[] orp = new Predicate[listOr.size()];
        Predicate or = criteriaBuilder.or(listOr.toArray(orp));
        if (list.size() != 0 && listOr.size() != 0) {
            return query.where(and, or).getRestriction();
        }
        if (list.size() == 0 && listOr.size() != 0) {
            return query.where(or).getRestriction();
        }
        if (list.size() != 0 && listOr.size() == 0) {
            return query.where(and).getRestriction();
        }
        return criteriaBuilder.and(list.toArray(p));
    }
}
