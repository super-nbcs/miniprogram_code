package com.zfw.utils;
public class ValidatorUtils implements Regular {

    /**
     * 验证是否邮箱
     * @param email
     * @return
     */
    public static boolean isEmail(String email){
        if (StringUtilsEx.isBlank(email))
            return false;
        return emailer.matcher(email).matches();
    }

    /**
     *  验证是否为纯数字
     * @param numberStr
     * @return
     */
    public static boolean isNumber(String numberStr) {
        if (StringUtilsEx.isBlank(numberStr)){
            return false;
        }
        return number.matcher(numberStr).matches();
    }

    /**
     *
     * 判断是否包含中文
     * @param str
     * @return
     */
    public static boolean isContainsChinese(String str) {
        if (StringUtilsEx.isBlank(str)){
            return false;
        }
        return chineser.matcher(str).find();
    }

    /**
     * 判断是否为日期
     * @param str
     * @return
     */
    public static boolean isDatetime(String str) {
        if (StringUtilsEx.isBlank(str)){
            return false;
        }
        return datetimeer.matcher(str).matches();
    }

    /**
     *
     * 判断是否为电话号码
     * @param phone
     * @return
     */
    public static boolean isPhone(String phone) {
        if (StringUtilsEx.isBlank(phone)){
            return false;
        }
        return phoner.matcher(phone).matches();
    }

    /**
     * 判断是否为IP地址
     * @param ip
     * @return
     */
    public static Boolean isIpAddress(String ip){
        if (StringUtilsEx.isBlank(ip)){
            return false;
        }
        return iper.matcher(ip).matches();
    }

    /**
     * 判断是否为IP地址 ,需要带http://
     * @param ip
     * @return
     */
    public static Boolean isIpAddressAndUrl(String ip){
        if (StringUtilsEx.isBlank(ip)){
            return false;
        }
        return ipURL.matcher(ip).matches();
    }

    /**
     *
     * 判断是否为url
     * @param url
     * @return
     */
    public static Boolean isUrl(String url){
        if (StringUtilsEx.isBlank(url)){
            return false;
        }
        return urler.matcher(url).matches();
    }

    /**
     *
     * 判断是否为身份证号码
     * @param idCard
     * @return
     */
    public static Boolean isIdCard(String idCard){
        if (StringUtilsEx.isBlank(idCard)){
            return false;
        }
        return idCarder.matcher(idCard).matches();
    }

    public static Boolean isUsername(String userName){
        if (StringUtilsEx.isBlank(userName)){
            return false;
        }
        return usernameer.matcher(userName).matches();
    }

}
