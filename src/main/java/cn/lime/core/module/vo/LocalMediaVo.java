package cn.lime.core.module.vo;

import cn.lime.core.module.entity.LocalMedia;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName: LocalMediaVo
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/9/3 12:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocalMediaVo implements Serializable {
    @Schema(description = "多媒体URL ID 序列化为String")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long urlId;
    @Schema(description = "多媒体URL ")
    private String url;
    private String tagName;
    @Schema(description = "创建时间 序列化为String")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long createTime;

    public static LocalMediaVo fromBean(LocalMedia bean){
        LocalMediaVo vo = new LocalMediaVo();
        vo.setUrlId(bean.getId());
        vo.setUrl(bean.getUrl());
        return vo;
    }
}
