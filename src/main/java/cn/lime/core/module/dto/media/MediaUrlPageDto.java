package cn.lime.core.module.dto.media;

import cn.lime.core.common.ErrorCode;
import cn.lime.core.common.ThrowUtils;
import cn.lime.core.common.dto.PageRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @ClassName: MediaUrlPageDto
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/9/3 12:43
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MediaUrlPageDto extends PageRequest {
    @Schema(description = "URL标签ID 默认标签未分组为1 String ")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long tagId;

    @Override
    public void checkRequest() {

    }

    private static final List<String> SORT_LIST = List.of("create_time");

    @Override
    public void checkPageRequest() {
        ThrowUtils.throwIf(!SORT_LIST.contains(getSortField()), ErrorCode.PARAMS_ERROR,
                "可用排序字段:" + String.join(",", SORT_LIST));
    }
}
