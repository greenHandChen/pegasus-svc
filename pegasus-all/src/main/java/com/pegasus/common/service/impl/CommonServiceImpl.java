package com.pegasus.common.service.impl;

import com.pegasus.common.dto.CommonDTO;
import com.pegasus.common.service.ICommonService;
import org.hibernate.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * Created by enHui.Chen on 2019/11/11.
 */
public class CommonServiceImpl<T> implements ICommonService<T> {
    @Autowired
    private Mapper<T> mapper;

    @Override
    public List<T> select(T record) {
        return mapper.select(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(T record) {
        return mapper.insert(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public T insertSelective(T record) {
        int count = mapper.insertSelective(record);
        return record;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateByPrimaryKey(T record) {
        return mapper.updateByPrimaryKey(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public T updateByPrimaryKeySelective(T record) {
        int count = mapper.updateByPrimaryKeySelective(record);
        return record;
    }

    // TODO
    @Override
    @Transactional(rollbackFor = Exception.class)
    public T updateByPrimaryKeyOptions(T record, Criteria criteria) {
        return null;
    }

    @Override
    public T selectByPrimaryKey(T record) {
        return mapper.selectByPrimaryKey(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByPrimaryKey(T record) {
        return mapper.deleteByPrimaryKey(record);
    }

    @Override
    public List<T> selectAll() {
        return mapper.selectAll();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<T> batchUpdate(List<T> list) {
        if (CollectionUtils.isEmpty(list)) {
            return list;
        }
        list.forEach(e -> {
            String status = ((CommonDTO) e).get_status();
            switch (status) {
                case "insert":
                    this.insert(e);
                    break;
                case "update":
                    this.updateByPrimaryKey(e);
                    break;
                case "delete":
                    this.deleteByPrimaryKey(e);
                    break;
            }
        });
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(List<T> list) {
        int count = 0;
        if (CollectionUtils.isEmpty(list)) {
            return count;
        }
        for (T t : list) {
            count++;
            this.deleteByPrimaryKey(t);
        }
        return count;
    }

    @Override
    public List<T> selectOptions(T record, Criteria criteria) {
        return null;
    }

    @Override
    public List<T> selectOptions(T record, Criteria criteria, Integer pageNum, Integer pageSize) {
        return null;
    }
}
