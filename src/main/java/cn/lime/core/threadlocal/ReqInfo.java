package cn.lime.core.threadlocal;

import lombok.Data;

/**
 * @ClassName: UserThreadLocalBean
 * @Description: 用户ThreadLocal信息实体
 * @Author: Lime
 * @Date: 2023/10/16 10:31
 */
@Data
public class ReqInfo {

    private String uuid;

    private Long reqTime;

    private Long userId;

    private Integer role;

    private String token;

    private Integer platform;

    private String ip;

    private int authLevel;

    private int vipLevel;

}
