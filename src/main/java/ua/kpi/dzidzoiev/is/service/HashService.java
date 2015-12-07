package ua.kpi.dzidzoiev.is.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static ua.kpi.dzidzoiev.is.service.Holder.UTF_8;

/**
 * Created by dzidzoiev on 12/7/15.
 */
public class HashService {
    public static byte[] get16(String message) {
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
        byte[] key = sha.digest(message.getBytes(UTF_8));
        return Arrays.copyOf(key, 16);
    }

    public static byte[] get(String message, int bits) {
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
        byte[] key = sha.digest(message.getBytes(UTF_8));
        return Arrays.copyOf(key, bits);
    }
}
