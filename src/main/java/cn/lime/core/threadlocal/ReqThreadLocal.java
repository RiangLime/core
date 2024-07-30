package cn.lime.core.threadlocal;


/**
 * @ClassName: ThreadLocalPool
 * @Description: ThreadLocal信息
 * @Author: Lime
 * @Date: 2023/8/28 14:15
 */
public class ReqThreadLocal {

    private static final ThreadLocal<ReqInfo> THREAD_LOCAL = new ThreadLocal<>();


    private ReqThreadLocal() {
    }

    /**
     * 获取线程中的用户
     * @return 用户信息
     */
    public static ReqInfo getInfo() {
        return THREAD_LOCAL.get();
    }

    /**
     * 设置当前线程中的用户
     * @param info 用户信息
     */
    public static void setInfo(ReqInfo info) {
        THREAD_LOCAL.set(info);
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }


}
