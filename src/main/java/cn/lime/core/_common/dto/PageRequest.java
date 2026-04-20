package cn.lime.core._common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @ClassName: PageRequest
 * @Description: 分页请求
 * @Author: Lime
 * @Date: 2023/9/18 16:08
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class PageRequest extends BaseCheckDto implements Serializable {
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
    @Min(value = 1,message = "current最小值为1")
    @NotNull(message = "current不可为空")
    private Integer current = 1;

    /**
     * 页面大小
     */
    @Schema(description = "页面大小")
    @Min(value = 1,message = "pageSize最小值为1")
    @NotNull(message = "pageSize不可为空")
    private Integer pageSize = 10;

    /**
     * 排序字段
     */
    @Schema(description = "排序字段 非数据库字段")
    private String sortField;

    /**
     * 排序顺序（默认升序）
     */
    @Schema(description = "排序顺序（默认升序）ascend/descend")
    private String sortOrder = SORT_ORDER_ASC;

    /**
     * 分页参数校验
     */
    public abstract void checkPageRequest();

    /**
     * 获取SQL真实排序字段
     * @return
     */
    public abstract String getRealSortField();
}
