package com.zfw.utils;

import com.zfw.core.constant.Constant;
import com.zfw.core.convert.DateConvert;
import com.zfw.core.entity.BaseEntity;
import com.zfw.core.exception.GlobalException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.Assert;

import javax.persistence.criteria.*;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.zfw.core.constant.Constant.FAIL;

/**
 * @Author:zfw
 * @Date:2019/7/20
 * @Content:
 */
public class JpaFilterUtils {

    public static String LIKE = "like";
    public static String BETWEEN="至";


    /**
     * 过滤无效排序参数
     * @param zClass
     * @param orders
     */
    public static List<Sort.Order> filterSortProperty(Class<?> zClass, Sort.Order... orders){

        Field[] fields = zClass.getDeclaredFields();
        List<String> names=new ArrayList<>();
        Arrays.asList(fields).forEach(item->{
            String name = item.getName();
            names.add(name);
        });

        Class<?> superclass = zClass.getSuperclass();
        Field[] superclassDeclaredFields = superclass.getDeclaredFields();
        Arrays.asList(superclassDeclaredFields).forEach(item->{
            String name=item.getName();
            names.add(name);
        });

        Set<Sort.Order> objects = new HashSet<>();
        Arrays.asList(orders).forEach(item->{
            boolean contains = names.contains(item.getProperty());
            if (contains){
                objects.add(item);
            }else {
                //throw new GlobalException(Constant.FAIL.CODE,"无效排序参数："+item.getProperty(),"");
            }
        });
        return new ArrayList<>(objects);
    }

    public static List<Sort.Order> filterSortProperty(Class<?> zClass, List<Sort.Order> orders){
        Sort.Order[] ordersArray=new Sort.Order[orders.size()];
        for (int i = 0; i <orders.size() ; i++) {
            ordersArray[i]=orders.get(i);
        }
        return filterSortProperty(zClass,ordersArray);
    }


    /**
     * 获取 对象（o）上 指定属性（fieldName）的值
     * @param fieldName 如：id
     * @param o         如：t
     * @return          t对象上的id值
     */
    public static Object getFieldValueByFieldName(String fieldName,Object o){
        if ( o instanceof HibernateProxy) {
            o=Hibernate.unproxy(o);
        }
        Class<?> aClass = o.getClass();
        try {
            Field declaredField = aClass.getDeclaredField(fieldName);
            declaredField.setAccessible(true);
            return declaredField.get(o);
        } catch (NoSuchFieldException e) {
            throw new GlobalException(FAIL.CODE,"对象没有这个属性："+fieldName,e.getMessage());
        } catch (IllegalAccessException e) {
            throw new GlobalException(FAIL.CODE,"通过反射获取属性："+fieldName+"的值时失败",e.getMessage());
        }
    }

    /**
     *  对象（o）设置属性（fieldName）的值（fieldValue）
     * @param fieldName         如：id
     * @param fieldValue        如：1
     * @param o                 如：t
     *                          t.setId(1)
     */
    public static void setFieldValueByFieldName(String fieldName,Object fieldValue,Object o){
        try {
            if ( o instanceof HibernateProxy) {
                o=Hibernate.unproxy(o);
            }
            Field declaredField = o.getClass().getDeclaredField(fieldName);
            declaredField.setAccessible(true);
            declaredField.set(o,fieldValue);
        } catch (NoSuchFieldException e) {
            throw new GlobalException(FAIL.CODE,"对象没有这个属性："+fieldName,e.getMessage());
        } catch (IllegalAccessException e) {
            throw new GlobalException(FAIL.CODE,"通过反射设置属性："+fieldName+"的值时失败",e.getMessage());
        }
    }


    /**
     * 将from中不为null的属性值，复制到to相对应的属性中, TODO 只考虑了父类的remarks
     * @param from      如：new User().setId(1).setName("from")
     * @param to        如：new User().setId(1).setName("to").setPassword("123456")
     * @param zClass    如：User.class
     * @param <T>       T  就是  User这个对象
     * @return          new User().setId(1).setName("from").setPassword("123456")
     */
    public static <T> T  copyNotNullProperties(Object from, Object to, Class<T> zClass){
        Assert.notNull(from,"from 对象不能为null");
        Assert.notNull(to,"to 对象不能为null");
        if ( from instanceof HibernateProxy) {
            from=Hibernate.unproxy(from);
        }
        if ( to instanceof HibernateProxy) {
            to = Hibernate.unproxy(to);
        }
        Class<?> toClass = to.getClass();
        Class<?> fromClass = from.getClass();
        if (!toClass.isInstance(from)){
            throw new  IllegalArgumentException("对象： [" + fromClass.getName() +
                    "] 不能转换成 [" + toClass.getName() + "]");
        }
        Field[] fromDeclaredFields = fromClass.getDeclaredFields();
        for (Field fromDeclaredField :fromDeclaredFields) {
            String fromFieldName = fromDeclaredField.getName();
            Object fromFieldValue = getFieldValueByFieldName(fromFieldName, from);
            if (fromFieldValue!=null){
                setFieldValueByFieldName(fromFieldName,fromFieldValue,to);
            }
        }

        //此处只对父类的remarks做了验证，如有特别需要，另加
        Class<?> fromSuperclass = from.getClass().getSuperclass();
        Class<?> toSuperclass = to.getClass().getSuperclass();
        try {
            Field fromRemarks = fromSuperclass.getDeclaredField("remarks");
            fromRemarks.setAccessible(true);

            Object o = fromRemarks.get(from);
            Field toRemarks = toSuperclass.getDeclaredField("remarks");
            toRemarks.setAccessible(true);
            toRemarks.set(to,o);


            Field fromSort= fromSuperclass.getDeclaredField("sort");
            fromSort.setAccessible(true);

            Object oSort = fromSort.get(from);
            Field toSort = toSuperclass.getDeclaredField("sort");
            toSort.setAccessible(true);
            toSort.set(to,oSort);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (T)to;
    }


    /**
     * 解析参数
     * @param request
     * @return
     */
    public static Map<String, Object> bySearchFilter(HttpServletRequest request) {
        Assert.notNull(request, "request不能为null");
        Enumeration<String> paramNames = request.getParameterNames();
        Map<String, Object> params = new TreeMap<>();

        while (paramNames != null && paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String[] values = request.getParameterValues(paramName);
            if (values == null || values.length == 0) {
                // Do nothing, no values found at all.
            } else if (values.length > 1) {
                params.put(paramName, values);
            } else {
                params.put(paramName, values[0]);
            }
        }
        return params;
    }



    /**
     * 过滤无效参数
     * @param request
     * @param t
     */
    public static Map<String, Object> filterParams(HttpServletRequest request,Class<?> t){
        Class<?> superclass = t.getSuperclass();
        Map<String, Object> params = bySearchFilter(request);
        Map<String, Object> tempParams=new TreeMap<>();
        params.forEach((k,v)->{
            try {
                t.getDeclaredField(k);
                tempParams.put(k,v);
            } catch (NoSuchFieldException e) {
                try {
                    superclass.getDeclaredField(k);
                    tempParams.put(k,v);
                } catch (NoSuchFieldException e1) {
                    //throw new GlobalException(FAIL.CODE,FAIL.ZH_CODE+"：不存在["+k+"]查询参数",FAIL.EN_CODE);
                }
            }
        });
        return tempParams;
    }


    /**
     * 过滤参数空值,只处理第一个参数值，其余的全部扔了
     * @param request
     * @param t
     * @return
     */
    public static Map<String,String> filterValue(HttpServletRequest request,Class<?> t){
        Map<String, Object> params = filterParams(request, t);
        Map<String, String> tempParams=new TreeMap<>();
        params.forEach((k,v)->{
            //只处理第一个参数值,其余全部仍了
            String value=v.getClass().isArray()?((String [])v)[0]:((String) v);
            if (!StringUtils.isBlank(value)){
                tempParams.put(k,value);
            }else {
                throw new GlobalException(FAIL.CODE,FAIL.ZH_CODE+":参数【"+k+"】传值有误，不能为空或没遵守传值规定",FAIL.EN_CODE);
            }
        });
        return tempParams;
    }


    /**
     *  过滤参数空值，处理一条件多值
     * @param request
     * @param t
     * @return
     */
    public static Map<String,List<String>> filterManyValue(HttpServletRequest request,Class<?> t){
        Map<String, Object> params = filterParams(request, t);
        Map<String, List<String>> tempParams=new TreeMap<>();
        params.forEach((k,v)->{
            List<String> values;
            if (v.getClass().isArray()){
                values=Arrays.asList((String[]) v);
            }else {
                values= Arrays.asList((String) v);
            }
            values = values.stream().filter(item -> StringUtilsEx.isNotBlank(item)).collect(Collectors.toList());
            if (values.size()!=0){
                tempParams.put(k,values);
            }
        });
        return tempParams;
    }


    /**
     * 只处理一个参数
     * @param request
     * @param zClass
     * @param <T>
     * @return
     */
    public static <T> Specification<T> dynamicSpecifications(HttpServletRequest request, Class<T> zClass){
        Map<String, String> params = filterValue(request,zClass);
        if (params.size()==0){
            return null;
        }
        Specification<T> tSpecification = new Specification<T>() {
            List<Predicate> list = new ArrayList<>();

            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                params.forEach((k,v)->{
                    Path<String> objectPath = root.get(k);
                    Class<? extends Class<? extends String>> javaType = objectPath.type().getJavaType();


                    //处理日期类型
                    if (javaType.equals(Date.class)){
                        if (v.contains(BETWEEN)){
                            String[] between = v.split(BETWEEN);
                            try {
                                Date startDate = DateUtils.parseDateStrictly(between[0], DateConvert.pattern);
                                Date endDate = DateUtils.parseDateStrictly(between[1], DateConvert.pattern);
                                SimpleDateFormat sdf = new SimpleDateFormat(BaseEntity.DATE_FORMAT);
                                list.add(criteriaBuilder.between(objectPath.as(String.class),sdf.format(startDate),sdf.format(endDate)));
                            } catch (ParseException e) {
                                throw new GlobalException(Constant.FAIL.CODE,Constant.FAIL.ZH_CODE+":日期格式为"+ Arrays.toString(DateConvert.pattern)+"中的一个，推荐【yyyy-MM-dd HH:mm:ss】",Constant.FAIL.EN_CODE);
                            }
                        }else {
                            throw new GlobalException(Constant.FAIL.CODE,Constant.FAIL.ZH_CODE+":请遵守规定时间查询格式",Constant.FAIL.EN_CODE);
                        }
                    }
                    //处理枚举类型
                    if (javaType.isEnum()){
                        Constant[] values = Constant.values();
                        for (Constant constant : values) {
                            String name = constant.name();
                            if (name.equals(v)){
                                list.add(criteriaBuilder.equal(objectPath.as(String.class),constant.CODE));
                            }
                        }
                    }
                    //处理字符串类型
                    if (javaType.equals(String.class)){
                        if (StringUtils.endsWith(v, LIKE)){
//                            list.add(criteriaBuilder.like(root.get(k).as(String.class),"%"+StringUtils.removeEnd(v, LIKE)+"%"));
                            list.add(criteriaBuilder.like(root.get(k).as(String.class),StringUtils.removeEnd(v, LIKE)+"%"));
                        }else {
                            list.add(criteriaBuilder.equal(root.get(k).as(String.class),v));
                        }
                    }
                    //处理integer类型
                    if (javaType.equals(Integer.class)){
                        if (StringUtils.endsWith(v, LIKE)){
//                            list.add(criteriaBuilder.like(root.get(k).as(String.class),"%"+StringUtils.removeEnd(v, LIKE)+"%"));
                            list.add(criteriaBuilder.like(root.get(k).as(String.class),StringUtils.removeEnd(v, LIKE)+"%"));
                        }else {
                            list.add(criteriaBuilder.equal(root.get(k).as(String.class),v));
                        }
                    }

                });
                Predicate[] p = new Predicate[list.size()];
                return criteriaBuilder.and(list.toArray(p));
            }
        };

        return tSpecification;
    }

    public static Map<String, List<String>> addEquals2Map(Map<String,List<String>> equals, Map<String, List<String>> params){
        if (equals.isEmpty()){
            return params;
        }else {
            equals.forEach((k,v)->params.put(k,v));
        }
        return params;
    }

    /**
     * 处理一个请求参数，多条件，多条件时like以or查询
     * @param request
     * @param zClass
     * @param <T>
     * @return
     */
    public static <T> Specification<T> dynamicSpecificationManyValues(HttpServletRequest request,Map<String,List<String>> equals, Class<T> zClass){
        Map<String, List<String>> params = filterManyValue(request, zClass);
        Map<String, List<String>> params1= addEquals2Map(equals,params);
        if (params1.size()==0){
            return null;
        }
        Specification<T> tSpecification = new Specification<T>() {
            List<Predicate> list = new ArrayList<>();
            List<Predicate> listOr = new ArrayList<>();

            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                params1.forEach((k,v)->{
                    Path<String> objectPath = root.get(k);
                    Class<? extends Class<? extends String>> javaType = objectPath.type().getJavaType();


                    //处理日期类型
                    if (javaType.equals(Date.class)){
                        if (v.get(0).contains(BETWEEN)){
                            String[] between = v.get(0).split(BETWEEN);
                            try {
                                Date startDate = DateUtils.parseDateStrictly(between[0], DateConvert.pattern);
                                Date endDate = DateUtils.parseDateStrictly(between[1], DateConvert.pattern);
                                SimpleDateFormat sdf = new SimpleDateFormat(BaseEntity.DATE_FORMAT);
                                list.add(criteriaBuilder.between(objectPath.as(String.class),sdf.format(startDate),sdf.format(endDate)));
                            } catch (ParseException e) {
                                throw new GlobalException(Constant.FAIL.CODE,Constant.FAIL.ZH_CODE+":日期格式为"+ Arrays.toString(DateConvert.pattern)+"中的一个，推荐【yyyy-MM-dd HH:mm:ss】",Constant.FAIL.EN_CODE);
                            }
                        }else {
                            throw new GlobalException(Constant.FAIL.CODE,Constant.FAIL.ZH_CODE+":请遵守规定时间查询格式",Constant.FAIL.EN_CODE);
                        }
                    }
                    //处理枚举类型
                    if (javaType.isEnum()){
                        Constant[] values = Constant.values();
                        for (Constant constant : values) {
                            String name = constant.name();
                            if (name.equals(v.get(0))){
                                list.add(criteriaBuilder.equal(objectPath.as(String.class),constant.CODE));
                            }
                        }
                    }
                    //处理字符串类型
                    if (javaType.equals(String.class)){
                        if (v.size()==1){
                            if (StringUtils.endsWith(v.get(0),LIKE)){
                                listOr.add(criteriaBuilder.like(root.get(k).as(String.class),StringUtils.removeEnd(v.get(0),LIKE)+"%"));
                            }else {
                                list.add(criteriaBuilder.equal(root.get(k).as(String.class),v.get(0)));
                            }
                        }else {
                            for (String value : v) {
                                if (StringUtils.endsWith(value,LIKE)){
                                    Predicate like = criteriaBuilder.like(root.get(k).as(String.class), StringUtils.removeEnd(value, LIKE) + "%");
                                    listOr.add(like);
                                }else {
                                    CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get(k).as(String.class));
                                    in.value(value);
                                    list.add(in);
                                }

                            }
                        }
                    }
                    //处理integer类型
                    if (javaType.equals(Integer.class)){
                        if (StringUtils.endsWith(v.get(0), LIKE)){
                            list.add(criteriaBuilder.like(root.get(k).as(String.class),"%"+StringUtils.removeEnd(v.get(0), LIKE)+"%"));
                        }else {
                            if (v.size()==1){
                                list.add(criteriaBuilder.equal(root.get(k).as(String.class),v.get(0)));
                            }else if (v.size()>=1){
                                CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get(k).as(String.class));
                                v.forEach(value ->in.value(value));
                                list.add(in);
                            }
                        }
                    }

                });
                Predicate[] p = new Predicate[list.size()];
                Predicate and = criteriaBuilder.and(list.toArray(p));
                Predicate[] orp = new Predicate[listOr.size()];
                Predicate or = criteriaBuilder.or(listOr.toArray(orp));
                if (list.size()!=0&&listOr.size()!=0){
                    return query.where(and,or).getRestriction();
                }
                if (list.size()==0&&listOr.size()!=0){
                    return query.where(or).getRestriction();
                }
                if (list.size()!=0&&listOr.size()==0){
                    return query.where(and).getRestriction();
                }
                return criteriaBuilder.and(list.toArray(p));
            }
        };

        return tSpecification;
    }
}
