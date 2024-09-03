package cn.lime.core.module.dto.media;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @ClassName: MediaUrlDeleteDto
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/9/3 12:39
 */
@Data
public class MediaUrlDeleteDto {
    @Schema(description = "URL ID String")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @NotNull(message = "多媒体ID不可为空")
    private Long urlId;
}
