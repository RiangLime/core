package cn.lime.core.module.dto.media;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName: MediaTagAddDto
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/9/3 12:30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MediaTagAddDto implements Serializable {
    @Schema(description = "URL标签名")
    @NotNull(message = "URL标签名不可为空")
    private String tagName;
}
