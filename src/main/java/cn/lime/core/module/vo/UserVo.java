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
    @Schema(description = "用户ID")
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
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long birthday;
    @Schema(description = "籍贯")
    private String birthplace;
    @Schema(description = "注册时间 秒级时间戳")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long registerTime;
    @Schema(description = "最后一次登录时间 秒级时间戳")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long lastLoginTime;
    @Schema(description = "用户状态")
    private Integer status;
    @Schema(description = "用户角色 1用户2管理员")
    private Integer role;

    public UserVo(User personnel) {
        userId = personnel.getUserId();
        account = personnel.getAccount();
        nickName = personnel.getName();
        avatar = personnel.getAvatar();
        phone = personnel.getPhone();
        email = personnel.getEmail();
        sex = personnel.getSex();
        birthday = personnel.getBirthday().getTime()/1000;
        birthplace = personnel.getBirthplace();
        status = personnel.getStatus();
    }
}
