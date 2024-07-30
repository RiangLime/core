package cn.lime.core.aes;


import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @ClassName: AesUtils
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/4/7 18:08
 */
public class AesUtils {
    /**
     * AES 对称加密（RSA非对称加密）
     * CBC 有向量 （ECB 无向量）
     * PKCS5Padding 填充模式（NoPadding 无填充）
     */
    private static final String ALG_AES_CBC_PKCS5 = "AES/CBC/PKCS5Padding";

    private static final String ALGORITHM = "AES";

    private static final Charset UTF8 = StandardCharsets.UTF_8;

    private String aesKey;  // 指定好的秘钥，非Base64和16进制

    private String aesIv = "2e119e58a526bc64";   // 偏移量

    private SecretKeySpec skeySpec;

    private IvParameterSpec iv;

    public AesUtils(String key,String iv){
        this.aesKey = key;
        aesIv = iv;
    }

    /**
     * 解密方法
     * @param cipherStr Base64编码的加密字符串
     * @return 解密后的字符串(UTF8编码)
     * @throws Exception 异常
     */
    public String decrypt(String cipherStr) throws Exception{
        // step 1 获得一个密码器
        Cipher cipher = Cipher.getInstance(ALG_AES_CBC_PKCS5);
        // step 2 初始化密码器，指定是加密还是解密(Cipher.DECRYPT_MODE 解密; Cipher.ENCRYPT_MODE 加密)
        // 加密时使用的盐来够造秘钥对象
        skeySpec = new SecretKeySpec(aesKey.getBytes(),ALGORITHM);
        // 加密时使用的向量，16位字符串
        iv = new IvParameterSpec(aesIv.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        // 对加密报文进行base64解码
        byte[] encrypted1 = Base64.decodeBase64(cipherStr);
        // 解密后的报文数组
        byte[] original = cipher.doFinal(encrypted1);
        // 输出utf8编码的字符串，输出字符串需要指定编码格式
        return new String(original, UTF8);
    }

    /**
     * 加密
     * @param plainText 明文
     * @return Base64编码的密文
     * @throws Exception  加密异常
     */
    public String encrypt(String plainText) throws Exception{
        Cipher cipher = Cipher.getInstance(ALG_AES_CBC_PKCS5);
        skeySpec = new SecretKeySpec(aesKey.getBytes(),ALGORITHM);
        iv = new IvParameterSpec(aesIv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec,iv);
        // 这里的编码格式需要与解密编码一致
        byte [] encryptText = cipher.doFinal(plainText.getBytes(UTF8));
        return Base64.encodeBase64String(encryptText);
    }
}
