package cn.lime.core._common.dto;

/**
 * @ClassName: BaseCheckDto
 * @Description: 基础类 用于进行参数校验
 * @Author: riang
 * @Date: 2026/1/13 01:48
 */
public abstract class BaseCheckDto {

    /**
     * 自定义入参校验
     */
    public abstract void checkRequest();

}