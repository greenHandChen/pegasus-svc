package com.pegasus.common.advice;

import com.pegasus.common.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by enHui.Chen on 2019/10/21.
 */
@Slf4j
@ControllerAdvice
public class CuxControllerAdvice {
    @ResponseBody
    @ExceptionHandler(CommonException.class)
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public Map<String, Object> handleCommonException(CommonException e) {
        HashMap<String, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("status", "501");
        objectObjectHashMap.put("message", e.getMessage());
        objectObjectHashMap.put("timeStamp", new Date());
        return objectObjectHashMap;
    }
}
