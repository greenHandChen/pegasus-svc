package com.pegasus.common.service;

import org.hibernate.Criteria;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by enHui.Chen on 2019/11/11.
 */
public interface ICommonService<T> {
    List<T> select(T condition);

    int insert(T record);

    T insertSelective(T record);

    int updateByPrimaryKey(T record);

    @Transactional(rollbackFor = Exception.class)
    T updateByPrimaryKeySelective(T record);

    @Transactional(rollbackFor = Exception.class)
    T updateByPrimaryKeyOptions(T record, Criteria criteria);

    T selectByPrimaryKey(T record);

    int deleteByPrimaryKey(T record);

    List<T> selectAll();

    List<T> batchUpdate(List<T> list);

    int batchDelete(List<T> list);


    List<T> selectOptions(T record, Criteria criteria);

    List<T> selectOptions(T record, Criteria criteria, Integer pageNum, Integer pageSize);
}
