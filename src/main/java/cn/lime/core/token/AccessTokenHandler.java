package cn.lime.core.token;

import cn.lime.core.common.BusinessException;
import cn.lime.core.common.ErrorCode;
import cn.lime.core.common.ThrowUtils;
import cn.lime.core.constant.AuthLevel;
import cn.lime.core.module.entity.User;
import cn.lime.core.module.vo.TokenCheckVo;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: AccessTokenUtil
 * @Description: Token生成工具类 使用Jwt
 * @Author: Lime
 * @Date: 2023/12/21 13:29
 */
public class AccessTokenHandler {

    //获取jwt生成器
    private static final JWTCreator.Builder JWT_BUILDER = JWT.create();

    private final String encodeKey;
    private final long accessTokenExpire;
    private final int refreshTokenExpire;

    public static final Long ACCESS_TOKEN_EXPIRE_MILLS = 1000 * 60 * 60 * 3L;

    public AccessTokenHandler(String key, Long accessTokenExpire, int refreshTokenExpire) {
        encodeKey = key;
        this.accessTokenExpire = accessTokenExpire;
        this.refreshTokenExpire = refreshTokenExpire;
    }

    public Long getAccessTokenExpire() {
        return accessTokenExpire;
    }

    public int getRefreshTokenExpire() {
        return refreshTokenExpire;
    }

    /**
     * 后端自己本地token
     *
     * @return
     */
    public String getToken(User user, Integer authLevel, Integer vipLevel) {

        //由于该生成器设置Header的参数为一个<String, Object>的Map,
        //所以我们提前准备好
        Map<String, Object> headers = new HashMap<>();

        headers.put("typ", "jwt");   //设置token的type为jwt
        headers.put("alg", "hs256");  //表明加密的算法为HS256


        //开始生成token
        //我们将之前准备好的header设置进去
        return JWT_BUILDER.withHeader(headers)
                //设置用户id
                .withClaim("userId", user.getUserId())
//                .withClaim("auth", JSON.toJSONString(authInfoArr))
                .withClaim("authLevel",authLevel)
                .withClaim("vipLevel",vipLevel)
                //token失效时间，3小时失效
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenExpire))
                //设置该jwt的发行时间，一般为当前系统时间
                .withIssuedAt(new Date(System.currentTimeMillis()))
                //进行签名，选择加密算法，以一个字符串密钥为参数
                .sign(Algorithm.HMAC256(encodeKey));
    }

    public TokenCheckVo checkJwtAccessToken(String accessToken) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(encodeKey)).build();
        DecodedJWT decode;
        try {
            /*开始进行验证，该函数会验证此token是否遭到修改，
                以及是否过期，验证成功会生成一个解码对象
                ，如果token遭到修改或已过期就会
                抛出异常，我们用try-catch抓一下*/
            decode = verifier.verify(accessToken);

        } catch (Exception e) {
            throw new BusinessException(ErrorCode.INVALID_ACCESS_TOKEN, "解析管理员ACCESS_TOKEN异常" + e.getMessage());
        }

        //可以一次性获取所有的自定义参数，返回Map集合
        Map<String, Claim> claims = decode.getClaims();
        if (claims == null) {
            throw new BusinessException(ErrorCode.INVALID_ACCESS_TOKEN, "解析管理员ACCESS_TOKEN异常,该Token参数值不合法");
        }

        //也可以根据自定义参数的键值来获取
        TokenCheckVo vo = new TokenCheckVo();
        // 解析userId
        Long userId = decode.getClaim("userId").asLong();
        Integer authLevel = decode.getClaim("authLevel").asInt();
        Integer vipLevel = decode.getClaim("vipLevel").asInt();

        // 解析过期时间
        Date expireDate = decode.getExpiresAt();
        // 校验参数值
        ThrowUtils.throwIf(ObjectUtils.isEmpty(userId) || ObjectUtils.isEmpty(expireDate),
                ErrorCode.INVALID_ACCESS_TOKEN, "解析管理员ACCESS_TOKEN异常,该Token参数值不合法");
        ThrowUtils.throwIf(expireDate.getTime() <= System.currentTimeMillis(),
                ErrorCode.INVALID_ACCESS_TOKEN, "token已经过期");
        vo.setUserId(userId);
        vo.setAuthLevel(authLevel);
        vo.setVipLevel(vipLevel);

        return vo;
    }

}
