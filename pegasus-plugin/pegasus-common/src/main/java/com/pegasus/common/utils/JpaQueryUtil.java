package com.pegasus.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.CollectionUtils;

import javax.persistence.Parameter;
import javax.persistence.Query;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;


/**
 * @Author: enHui.Chen
 * @Description: jpa通用查询工具类
 * @Data 2019/11/8
 */
@Slf4j
public class JpaQueryUtil {
    private static final String COUNT_SQL_PREFIX = " SELECT count(1) from ( ";
    private static final String COUNT_SQL_SUFFIX = " ) tmp ";


    public static <T> T getSingleResult(Query query, Class<T> clazz) {
        List<T> resultList = getResultList(query, clazz);
        if (!CollectionUtils.isEmpty(resultList)) {
            return resultList.get(0);
        }
        return null;
    }

    public static <T> List<T> getResultList(Query query, Class<T> clazz) {
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> resultList = query.getResultList();

        if (resultList == null) {
            return null;
        }

        List<T> rstList = new ArrayList<>();
        resultList.forEach(result -> {
            try {
                T rst = clazz.newInstance();
                rstList.add(rst);
                mapCopyProperties(result, rst);
            } catch (Exception e) {
                log.error("对象创建失败!");
                throw new RuntimeException(e);
            }
        });
        return rstList;
    }

    public static <T> Page<T> getResultListByPage(Query query, Pageable pageable, Class<T> clazz) {
        // count查询
        StringBuilder sb = new StringBuilder();
        sb.append(COUNT_SQL_PREFIX).append(((NativeQuery) query).getQueryString()).append(COUNT_SQL_SUFFIX);
        Map<String, Object> parameterMap = new HashMap<>();
        Set<Parameter<?>> parameters = query.getParameters();
        parameters.forEach(parameter -> {
            String key = parameter.getName();
            Object val = query.getParameterValue(key);
            parameterMap.put(key, val);
        });
        NamedParameterJdbcTemplate jdbcTemplate = ApplicationContextUtil.getApplicationContext()
                .getBean("namedParameterJdbcTemplate", NamedParameterJdbcTemplate.class);
        if (log.isDebugEnabled()) {
            log.debug("used by namedParameterJdbcTemplate to page sql : {}", sb.toString());
        }
        Long countRst = jdbcTemplate.queryForObject(sb.toString(), parameterMap, Long.class);

        // 分页查询
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<T> resultList = getResultList(query, clazz);
        return new PageImpl<>(resultList, pageable, countRst == null ? 0 : countRst);
    }


    private static void mapCopyProperties(Map source, Object target) {
        try {
            Class<?> targetClass = target.getClass();
            Field[] fields = targetClass.getDeclaredFields();
            Arrays.asList(fields).forEach(field -> {
                Object val = source.get(field.getName());
                if (val == null) {
                    return;
                }
                try {
                    Method method = targetClass.getMethod(getMethodName(field), field.getType());
                    method.invoke(target, val);
                } catch (Exception e) {
                    log.error("字段映射出错:{}", e.getMessage());
                    log.error("字段映射出错:", e);
                }
            });
        } catch (Exception e) {
            log.error("字段映射出错:{}", e.getMessage());
            log.error("字段映射出错:", e);
        }
    }

    private static String getMethodName(Field field) {
        String fieldName = field.getName();
        StringBuilder sb = new StringBuilder();
        return sb.append("set")
                .append(fieldName.substring(0, 1).toUpperCase())
                .append(fieldName.substring(1))
                .toString();
    }
}
