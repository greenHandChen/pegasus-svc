package com.pegasus.common.pagehelper.custom;

import java.util.List;


/**
 * Created by enHui.Chen on 2019/11/11.
 */
@FunctionalInterface
public interface Select<T> {
    List<T> doPageQuery();
}
