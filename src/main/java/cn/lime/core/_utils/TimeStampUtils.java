package cn.lime.core._utils;

/**
 * @ClassName: TimeStampUtils
 * @Description: 时间戳工具类
 * @Author: Lime
 * @Date: 2024/8/23 16:12
 */
public class TimeStampUtils {
    public static Long getUnixTimestamp(){
        return System.currentTimeMillis()/1000;
    }
}
