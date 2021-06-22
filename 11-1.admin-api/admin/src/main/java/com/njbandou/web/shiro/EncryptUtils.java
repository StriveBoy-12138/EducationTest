package com.njbandou.web.shiro;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;

/**
 * The type Encrypt utils.
 */
public class EncryptUtils {
    private EncryptUtils() {

    }

    /**
     * Encrypt string.
     *
     * @param password the password
     * @param salt     the salt
     * @return the string
     */
    public static String encrypt(String password, String salt) {
        String algorithmName = "SHA-512";
        int hashIterations = 2;
        SimpleHash hash = new SimpleHash(algorithmName, password, salt, hashIterations);
        return hash.toHex();
    }

    /**
     * Create salt string.
     *
     * @return the string
     */
    public static String createSalt() {
        return new SecureRandomNumberGenerator().nextBytes().toHex();
    }
}
