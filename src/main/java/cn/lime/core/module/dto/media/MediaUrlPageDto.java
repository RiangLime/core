package cn.lime.core.module.dto.media;

import cn.lime.core.common.PageRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
    @NotNull(message = "多媒体标签ID不可为空")
    private Long tagId = 1L;
}
