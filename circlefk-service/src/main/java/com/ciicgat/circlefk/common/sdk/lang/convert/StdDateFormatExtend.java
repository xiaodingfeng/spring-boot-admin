package com.ciicgat.circlefk.common.sdk.lang.convert;

import com.fasterxml.jackson.databind.util.StdDateFormat;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StdDateFormatExtend extends StdDateFormat {

    @Override
    public Date _parseDate(String source, ParsePosition pos) throws ParseException {
        // 解析Date.toString, 直接使用dateUtils会有时区转换问题
        if (source.length() >= 1 && !Character.isDigit(source.charAt(0))) {
            try {
                SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", new Locale("us"));
                return format.parse(source);
            } catch (Exception e) {
                return super._parseDate(source, pos);
            }
        } else {
            return super._parseDate(source, pos);
        }
    }

    @Override
    public StdDateFormat clone() {
        return new StdDateFormatExtend();
    }
}
