package com.pegasus.common.pagehelper.custom;

import com.pegasus.common.pagehelper.Page;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: enHui.Chen
 * @Description:
 * @Data 2019/11/11
 */
@Data
@AllArgsConstructor
public class PageResponse<T> implements Serializable {
    private final int page;
    private final int size;
    private final long total;
    private final List<T> content;

    public PageResponse(List<T> list) {
        Page<T> page = (Page<T>) list;
        this.page = page.getPageNum();
        this.size = page.getPageSize();
        this.total = page.getTotal();
        this.content = page.getResult();
    }
}
