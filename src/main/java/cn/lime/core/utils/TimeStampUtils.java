package cn.lime.core.utils;

/**
 * @ClassName: TimeStampUtils
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/23 16:12
 */
public class TimeStampUtils {
    public static Long getUnixTimestamp(){
        return System.currentTimeMillis()/1000;
    }
}
