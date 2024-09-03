package cn.lime.core.module.vo;

import cn.lime.core.module.entity.LocalMediaTag;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName: LocalMediaTagVo
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/9/3 11:54
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocalMediaTagVo implements Serializable {

    @Schema(description = "多媒体文件标签ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long tagId;

    @Schema(description = "多媒体文件标签名称")
    private String mediaTagName;

    public static LocalMediaTagVo fromBean(LocalMediaTag bean){
        return new LocalMediaTagVo(bean.getId(),bean.getTagName());
    }

}
