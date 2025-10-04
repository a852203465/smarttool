package cn.darkjrong.core.utils;

import cn.darkjrong.core.exceptions.StlRuntimeException;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.regex.Pattern;

/**
 * 密码工具类
 *
 * @author Rong.Jia
 * @date 2024/07/05
 */
@Slf4j
public class EncipherUtils {

    private static final String BASE64_PATTERN = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
    private static final RSA rsa;

    static {
        ClassPathResource classPathResource = new ClassPathResource("public.key");
        try {
            InputStream inputStream = classPathResource.getInputStream();
            String base64 = IoUtil.read(inputStream, CharsetUtil.CHARSET_UTF_8);
            rsa = SecureUtil.rsa(null, Base64.decode(base64));
        } catch (Exception e) {
            log.error("**********,The encryption key is incorrectly loaded", e);
            throw new StlRuntimeException(e);
        }
    }

    /**
     * 解密
     *
     * @param param 参数
     * @return {@link String}
     */
    public static String decrypt(String param) {
        return rsa.decryptStr(param, KeyType.PublicKey);
    }

    /**
     * 加密
     *
     * @param param 参数
     * @return {@link String}
     */
    public static String encrypt(String param) {
        return rsa.encryptBase64(param, KeyType.PublicKey);
    }

    /**
     * 是 base64
     *
     * @param str 参数
     * @return {@link Boolean}
     */
    public static Boolean isBase64(String str) {
        return Pattern.matches(BASE64_PATTERN, str);
    }



















}










