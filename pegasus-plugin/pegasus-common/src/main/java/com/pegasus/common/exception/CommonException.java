package com.pegasus.common.exception;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by enHui.Chen on 2019/10/21.
 */
@Data
public class CommonException extends RuntimeException {
    private String message;
    // 不进行持久化
    private transient List<Object> objects;

    public CommonException() {
        super();
    }

    public CommonException(Throwable throwable) {
        super(throwable);
    }

    public CommonException(String message) {
        super(message);
        this.message = message;
    }

    public CommonException(String message, List<Object> objects) {
        super(message);
        this.message = message;
        this.objects = objects;
    }

    public CommonException(String message, Object objects) {
        super(message);
        this.message = message;
        if (objects != null) {
            this.objects = new ArrayList<>();
            this.objects.add(objects);
        }
    }
}