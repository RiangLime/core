package cn.lime.core.module.dto.media;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @ClassName: MediaTagDeleteDto
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/9/3 12:31
 */
@Data
public class MediaTagDeleteDto {
    @Schema(description = "URL ID String")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @NotNull(message = "多媒体标签ID不可为空")
    private Long id;
}
