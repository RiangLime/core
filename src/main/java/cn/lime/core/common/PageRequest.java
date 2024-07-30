package cn.lime.core.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: PageRequest
 * @Description: 分页请求
 * @Author: Lime
 * @Date: 2023/9/18 16:08
 */
@Data
public class PageRequest implements Serializable {
    /**
     * 升序
     */
    public static final String SORT_ORDER_ASC = "ascend";

    /**
     * 降序
     */
    public static final String SORT_ORDER_DESC = "descend";

    /**
     * 当前页号
     */
    @Schema(description = "当前页号")
    private Integer current = 1;

    /**
     * 页面大小
     */
    @Schema(description = "页面大小")
    private Integer pageSize = 10;

    /**
     * 排序字段
     */
    @Schema(description = "排序字段")
    private String sortField;

    /**
     * 排序顺序（默认升序）
     */
    @Schema(description = "排序顺序（默认升序）ascend/descend")
    private String sortOrder = SORT_ORDER_ASC;
}
