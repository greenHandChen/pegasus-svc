package com.pegasus.common.pagehelper.custom.utils;

import com.pegasus.common.pagehelper.PageHelper;
import com.pegasus.common.pagehelper.custom.PageResponse;
import com.pegasus.common.pagehelper.custom.Select;
import org.springframework.data.domain.Pageable;

/**
 * Created by enHui.Chen on 2019/11/11.
 */
public class PageHelperUtil {

    /**
     * @Author: enHui.Chen
     * @Description: 分页查询
     * @Data 2019/11/11
     */
    public static <T> PageResponse<T> doPageQuery(Pageable pageRequest, Select<T> select) {
        PageHelper.startPage(pageRequest.getPageNumber() + 1, pageRequest.getPageSize());
        return new PageResponse<T>(select.doPageQuery());
    }
}
