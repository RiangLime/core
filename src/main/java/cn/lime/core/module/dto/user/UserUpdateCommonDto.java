package cn.lime.core.module.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: UserUpdateDto
 * @Description: 用户更新通用信息
 * @Author: Lime
 * @Date: 2023/10/16 14:17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateCommonDto implements Serializable {

    @Schema(description = "账号")
    private String account;

    @Schema(description = "昵称")
    private String nickName;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "性别")
    private Integer sex;

    @Schema(description = "出生日期 yyyy-mm-dd")
    private Date birthday;

    @Schema(description = "出生籍贯")
    private String birthplace;

    @Schema(description = "学历")
    private String degree;

    @Schema(description = "毕业院校")
    private String graduateSchool;

    @Schema(description = "年级")
    private String grade;
}
