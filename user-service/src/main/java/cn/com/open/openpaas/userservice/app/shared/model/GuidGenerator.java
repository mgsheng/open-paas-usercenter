package cn.com.open.openpaas.userservice.app.shared.model;

import java.util.UUID;

/**
 * 
 */
public abstract class GuidGenerator {


    /**
     * private constructor
     */
    private GuidGenerator() {
    }

    public static String generate() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}