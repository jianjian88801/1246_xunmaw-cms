package com.xunmaw.cms.common.util;


import com.xunmaw.cms.module.admin.model.User;
import lombok.experimental.UtilityClass;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * 密码加密工具类
 *
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
@UtilityClass
public class PasswordHelper {
    private static final RandomNumberGenerator RANDOM_NUMBER_GENERATOR = new SecureRandomNumberGenerator();
    private static final String ALGORITHM_NAME = "md5";
    private static final int HASH_ITERATIONS = 2;

    public static void encryptPassword(User user) {
        user.setSalt(RANDOM_NUMBER_GENERATOR.nextBytes().toHex());
        String newPassword = new SimpleHash(ALGORITHM_NAME, user.getPassword(), ByteSource.Util.bytes(user.getCredentialsSalt()), HASH_ITERATIONS).toHex();
        user.setPassword(newPassword);
    }

    public static String getPassword(User user) {
        return new SimpleHash(ALGORITHM_NAME, user.getPassword(), ByteSource.Util.bytes(user.getCredentialsSalt()), HASH_ITERATIONS).toHex();
    }

    public static void main(String[] args) {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("123456");
        encryptPassword(user);
        System.out.println(user);
    }
}
