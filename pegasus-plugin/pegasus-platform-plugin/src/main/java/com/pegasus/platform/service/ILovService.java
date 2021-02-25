package com.pegasus.platform.service;

import com.pegasus.common.service.ICommonService;
import org.springframework.data.domain.Pageable;

import java.util.Map;

/**
 * t
 * Created by enHui.Chen on 2019/11/11.
 */
public interface ILovService<T> extends ICommonService<T> {
    Map<String, Object> lovQuery(Pageable pageable, String lovCode, Boolean init);
}
