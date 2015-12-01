package ua.kpi.dzidzoiev.is.service;

import ua.kpi.dzidzoiev.is.service.symmetric.CipherAlgsEnum;
import ua.kpi.dzidzoiev.is.service.symmetric.CipherInstance;
import ua.kpi.dzidzoiev.is.service.symmetric.CipherModeEnum;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.util.HashSet;
import java.util.Set;

import static ua.kpi.dzidzoiev.is.service.symmetric.CipherInstance.parseCipher;

/**
 * Created by dzidzoiev on 12/1/15.
 */
public class SymmetricService {

    private final Set<CipherInstance> availableCiphers;
    {
        availableCiphers = new HashSet<>();
        availableCiphers.add(parseCipher("AES/CBC/NoPadding (128)"));
        availableCiphers.add(parseCipher("AES/CBC/PKCS5Padding (128)"));
        availableCiphers.add(parseCipher("AES/ECB/NoPadding (128)"));
        availableCiphers.add(parseCipher("AES/ECB/PKCS5Padding (128)"));
        availableCiphers.add(parseCipher("DES/CBC/NoPadding (56)"));
        availableCiphers.add(parseCipher("DES/CBC/PKCS5Padding (56)"));
        availableCiphers.add(parseCipher("DES/ECB/NoPadding (56)"));
        availableCiphers.add(parseCipher("DES/ECB/PKCS5Padding (56)"));
        availableCiphers.add(parseCipher("DESede/CBC/NoPadding (168)"));
        availableCiphers.add(parseCipher("DESede/CBC/PKCS5Padding (168)"));
        availableCiphers.add(parseCipher("DESede/ECB/NoPadding (168)"));
        availableCiphers.add(parseCipher("DESede/ECB/PKCS5Padding (168)"));
        availableCiphers.add(parseCipher("RSA/ECB/PKCS1Padding (1024)"));
        availableCiphers.add(parseCipher("RSA/ECB/OAEPWithSHA-1AndMGF1Padding (1024)"));
        availableCiphers.add(parseCipher("RSA/ECB/OAEPWithSHA-256AndMGF1Padding (1024)"));
    }

    public SymmetricService() {
        for (Provider provider: Security.getProviders()) {
            System.out.println(provider.getName());
            for (String key: provider.stringPropertyNames())
                System.out.println("\t" + key + "\t" + provider.getProperty(key));
        }
    }

    public File encryptFile(File f, byte[] key, CipherAlgsEnum alg, CipherModeEnum mode) {
        throw new IllegalStateException();
    }

    public byte[] enctypt(byte[] message, byte[] key, CipherAlgsEnum alg, CipherModeEnum mode) {
        Cipher desCipher;
        // Create the cipher
        try {
            desCipher = Cipher.getInstance(alg.name() +"/" + mode.name());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
        return null;
    }

    public Set<CipherInstance> getAvailableCiphers() {
        return availableCiphers;
    }
}
