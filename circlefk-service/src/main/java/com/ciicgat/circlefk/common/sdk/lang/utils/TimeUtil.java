package com.ciicgat.circlefk.common.sdk.lang.utils;

import com.ciicgat.circlefk.common.sdk.lang.exception.BusinessRuntimeException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
    public static Date StringToDayDate(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            throw new BusinessRuntimeException(-1, "时间转换失败");
        }
    }

    public static String DateToDayString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }
}
