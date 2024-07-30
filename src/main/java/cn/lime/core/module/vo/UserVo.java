package cn.lime.core.module.vo;

import cn.lime.core.module.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: UserVo
 * @Description: 用户详情vo
 * @Author: Lime
 * @Date: 2023/10/16 14:25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVo implements Serializable {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;
    @Schema(description = "账号")
    private String account;
    @Schema(description = "昵称")
    private String nickName;
    @Schema(description = "头像链接")
    private String avatar;
    @Schema(description = "手机号")
    private String phone;
    @Schema(description = "邮箱")
    private String email;
    @Schema(description = "性别")
    private Integer sex;
    @Schema(description = "生日")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date birthday;
    @Schema(description = "籍贯")
    private String birthplace;

    @Schema(description = "注册时间")
    @JsonFormat(pattern="yyyy-MM-dd:hh-mm-ss")
    private Date gmtCreated;
    @Schema(description = "用户状态")
    private Integer state;

    public UserVo(User personnel) {
        userId = personnel.getUserId();
        account = personnel.getAccount();
        nickName = personnel.getName();
        avatar = personnel.getAvatar();
        phone = personnel.getPhone();
        email = personnel.getEmail();
        sex = personnel.getSex();
        birthday = personnel.getBirthday();
        birthplace = personnel.getBirthplace();
        state = personnel.getStatus();
    }
}
