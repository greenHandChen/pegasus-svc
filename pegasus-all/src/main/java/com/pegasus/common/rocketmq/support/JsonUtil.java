package com.pegasus.common.rocketmq.support;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * Created by enHui.Chen on 2019/2/25.
 */
@Slf4j
public class JsonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    }

    public static String toString(Object object) {
        if (object == null) return null;
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            log.error("json数据转换错误:{}", e);
            return null;
        }
    }

    public static <T> T toObject(String str, Class<T> clazz) {
        try {
            return objectMapper.readValue(str.getBytes(Charset.forName("utf8")), clazz);
        } catch (Exception e) {
            log.error("json数据转换错误:{}", e);
            return null;
        }
    }

    public static <T> T toObject(String str, Class<?> collection, Class<?> clazz) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collection, clazz);
        try {
            return objectMapper.readValue(str, javaType);
        } catch (Exception e) {
            log.error("json数据转换错误:{}", e);
            return null;
        }
    }

    public static <T> T toObject(String str, TypeReference typeReference) {
        try {
            return objectMapper.readValue(str, typeReference);
        } catch (Exception e) {
            log.error("json数据转换错误:{}", e);
            return null;
        }
    }
}
