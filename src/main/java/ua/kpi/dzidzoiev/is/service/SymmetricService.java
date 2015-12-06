package ua.kpi.dzidzoiev.is.service;

import org.apache.commons.codec.binary.Base64;
import ua.kpi.dzidzoiev.is.service.symmetric.CipherDescription;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dzidzoiev on 12/1/15.
 */
public class SymmetricService {

    private static final Set<CipherDescription> availableCiphers;
    public static final String UTF_8 = "UTF-8";

    static {
        availableCiphers = new HashSet<>();
        availableCiphers.add(new CipherDescription("AES", "CBC", "NoPadding", 128));
        availableCiphers.add(new CipherDescription("AES", "CBC", "PKCS5Padding", 128));
        availableCiphers.add(new CipherDescription("AES", "ECB", "NoPadding", 128));
        availableCiphers.add(new CipherDescription("AES", "ECB", "PKCS5Padding", 128));
        availableCiphers.add(new CipherDescription("DES", "CBC", "NoPadding", 56));
        availableCiphers.add(new CipherDescription("DES", "CBC", "PKCS5Padding", 56));
        availableCiphers.add(new CipherDescription("DES", "ECB", "NoPadding", 56));
        availableCiphers.add(new CipherDescription("DES", "ECB", "PKCS5Padding", 56));
        availableCiphers.add(new CipherDescription("DESede", "CBC", "NoPadding", 168));
        availableCiphers.add(new CipherDescription("DESede", "CBC", "PKCS5Padding", 168));
        availableCiphers.add(new CipherDescription("DESede", "ECB", "NoPadding", 168));
        availableCiphers.add(new CipherDescription("DESede", "ECB", "PKCS5Padding", 168));
        availableCiphers.add(new CipherDescription("RSA", "ECB", "PKCS1Padding", 1024));
        availableCiphers.add(new CipherDescription("RSA", "ECB", "OAEPWithSHA-1AndMGF1Padding", 1024));
        availableCiphers.add(new CipherDescription("RSA", "ECB", "OAEPWithSHA-256AndMGF1Padding", 1024));
    }

    public SymmetricService() {
//        for (Provider provider : Security.getProviders()) {
//            System.out.println(provider.getName());
//            for (String key : provider.stringPropertyNames())
//                System.out.println("\t" + key + "\t" + provider.getProperty(key));
//        }
    }

    public byte[] encrypt(byte[] key, byte[] value, byte[] initVector) throws Exception {
        IvParameterSpec iv = new IvParameterSpec(initVector);
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

        byte[] encrypted = cipher.doFinal(value);
        System.out.println("encrypted string: "
                + Base64.encodeBase64String(encrypted));

        return Base64.encodeBase64(encrypted);
    }

    public byte[] decrypt(byte[] key, byte[] encrypted, byte[] initVector) throws Exception{
        IvParameterSpec iv = new IvParameterSpec(initVector);
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

        return cipher.doFinal(Base64.decodeBase64(encrypted));

    }

    public SecretKey generateKey(CipherDescription cipherDescription) throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance(cipherDescription.getAlg());
        keyGen.init(cipherDescription.getBlockSize());
        return keyGen.generateKey();
    }

    public IvParameterSpec generateIV(int blockSize) throws NoSuchAlgorithmException {
        final byte[] ivData = new byte[blockSize];
        final SecureRandom rnd = SecureRandom.getInstance("SHA1PRNG");
        rnd.nextBytes(ivData);
        return new IvParameterSpec(ivData);
    }

    public Set<CipherDescription> getAvailableCiphers() {
        return availableCiphers;
    }

    public static void main(String[] args) throws Exception {
        SymmetricService service = new SymmetricService();
        CipherDescription cipherDescription = new CipherDescription("AES", "CBC", "PKCS5Padding");
        IvParameterSpec iv = service.generateIV(cipherDescription.getBlockSize());
        String initVector = "RandomInitVector";
        String key = "Bar12345Bar12345";
        String message = "ацуп34н5рук 634цр 45 4цукіен ghrt yttr en";
        String encrypted = new String(service.encrypt(key.getBytes(UTF_8), message.getBytes(UTF_8), initVector.getBytes(UTF_8)), UTF_8);
        String decrypted = new String(service.decrypt(key.getBytes(UTF_8), encrypted.getBytes(UTF_8), initVector.getBytes(UTF_8)), UTF_8);
        System.out.println(encrypted);
        System.out.println(decrypted);
        System.out.println(message.equals(decrypted));
    }
}
