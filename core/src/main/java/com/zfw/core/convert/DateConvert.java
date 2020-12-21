package com.zfw.core.convert;

import com.zfw.core.constant.Constant;
import com.zfw.core.exception.GlobalException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * @Author:zfw
 * @Date:2019/8/6
 * @Content:
 */
@Component
public class DateConvert implements Converter<String, Date> {

    public static String[] pattern = {"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd" ,"yyyy-MM-dd HH", "yyyy-MM-dd HH:mm",
                                "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm",
                                "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm",
                                "yyyyMMdd", "yyyyMMdd HHmmss", "yyyyMMdd HHmm","yyyyMMdd HH",
                                "yyyy年MM月dd日", "yyyy年MM月dd日 HH时mm分ss秒", "yyyy年MM月dd日 HH时mm分","yyyy年MM月dd日 HH时"
                                };

    @Override
    public Date convert(String source) {
        if (StringUtils.isBlank(source)){
            return null;
        }
        Date date = get(source, 0);
        return date;
    }

    public Date get(String source,int begin){
        if (begin==pattern.length){
            throw new GlobalException(Constant.FAIL.CODE,Constant.FAIL.ZH_CODE+":日期格式为"+ Arrays.toString(pattern)+"中的一个，推荐【yyyy-MM-dd HH:mm:ss】",Constant.FAIL.EN_CODE);
        }
        String pattern = this.pattern[begin];
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            Date date = sdf.parse(source);
            return date;
        } catch (ParseException e) {
            return get(source,begin+1);
        }
    }
}
