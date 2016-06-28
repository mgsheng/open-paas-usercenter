package cn.com.open.openpaas.userservice.app.infrastructure.model;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

/**
 * 
 */
public abstract class PasswordHandler {


    /**
     * Private constructor
     */
    private PasswordHandler() {
    }

    /**
     * Encrypt  password ,if original password is empty,
     *
     * @param originalPassword Original password
     * @return Encrypted password
     */
    public static String encryptPassword(String originalPassword) {
        Md5PasswordEncoder md5PasswordEncoder = new Md5PasswordEncoder();
        return md5PasswordEncoder.encodePassword(originalPassword, null);
    }


}