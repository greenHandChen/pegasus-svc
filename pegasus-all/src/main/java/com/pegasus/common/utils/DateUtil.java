package com.pegasus.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by enHui.Chen on 2019/9/17.
 */
@Slf4j
public class DateUtil {
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    /**
     * @Author: enHui.Chen
     * @Description: String转Date时间
     * @Data 2019/9/17
     */
    public static Date StringToDate(String date, String... format) {
        try {
            if (StringUtils.isEmpty(date)) return null;
            String cuxFormat = format == null || format.length == 0 ? DEFAULT_DATE_FORMAT : format[0];
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(cuxFormat);
            return simpleDateFormat.parse(date);
        } catch (Exception e) {
            log.error("日期转换出错", e);
        }
        return null;
    }

    /**
     * @Author: enHui.Chen
     * @Description: 格林尼治时间转String
     * @Data 2019/4/10
     */
    public static String FromGMTString2String(String date, String... format) {
        try {
            if (StringUtils.isEmpty(date)) return null;
            if (!date.contains("GMT")) return date;
            String cuxFormat = format == null || format.length == 0 ? DEFAULT_DATE_FORMAT : format[0];
            SimpleDateFormat format1 = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss z", Locale.ENGLISH);
            SimpleDateFormat format2 = new SimpleDateFormat(cuxFormat);
            return format2.format(format1.parse(date.replace("GMT", "").replaceAll("\\(.*\\)", "")));
        } catch (Exception e) {
            log.error("日期转换出错", e);
        }
        return null;
    }

    /**
     * @Author: enHui.Chen
     * @Description: String日期转String日期
     * @Data 2019/4/10
     */
    public static String DateFormat(String date, String... format) {
        try {
            if (StringUtils.isEmpty(date)) return null;
            String cuxFormat = format == null || format.length == 0 ? DEFAULT_DATE_FORMAT : format[0];
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(cuxFormat);
            return simpleDateFormat.format(simpleDateFormat.parse(date));
        } catch (Exception e) {
            log.error("日期转换出错", e);
        }
        return null;
    }

    /**
     * @Author: enHui.Chen
     * @Description: 正数表示往前any天, 负数表示向后any天
     * @Data 2019/4/10
     */
    public static Date GetAnyDay(Date today, int anyDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + anyDay);
        return calendar.getTime();
    }
}
