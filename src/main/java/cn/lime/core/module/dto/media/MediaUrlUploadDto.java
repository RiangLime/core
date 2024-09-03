package cn.lime.core.module.dto.media;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: MediaUrlUploadDto
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/9/3 12:37
 */
@Data
public class MediaUrlUploadDto implements Serializable {
    @Schema(description = "URL")
    private String url;
    @Schema(description = "URL标签ID 未分组为1 string")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @NotNull(message = "多媒体标签ID不可为空 如未分组为1")
    private Long urlTagId;
}
