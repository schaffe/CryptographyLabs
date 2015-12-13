package ua.kpi.dzidzoiev.is.service;

import sun.misc.BASE64Encoder;
import ua.kpi.dzidzoiev.is.service.symmetric.CipherDescription;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

import static ua.kpi.dzidzoiev.is.service.Holder.UTF_8;

/**
 * Created by dzidzoiev on 12/1/15.
 */
public class SymmetricService {

    private static final Set<CipherDescription> availableCiphers;

    static {
        availableCiphers = new HashSet<>();
        availableCiphers.add(new CipherDescription("AES", "CBC", "NoPadding"));
        availableCiphers.add(new CipherDescription("AES", "CBC", "PKCS5Padding"));
        availableCiphers.add(new CipherDescription("AES", "ECB", "NoPadding"));
        availableCiphers.add(new CipherDescription("AES", "ECB", "PKCS5Padding"));
        availableCiphers.add(new CipherDescription("DES", "CBC", "NoPadding"));
        availableCiphers.add(new CipherDescription("DES", "CBC", "PKCS5Padding"));
        availableCiphers.add(new CipherDescription("DES", "ECB", "NoPadding"));
        availableCiphers.add(new CipherDescription("DES", "ECB", "PKCS5Padding"));
        availableCiphers.add(new CipherDescription("DESede", "CBC", "NoPadding"));
        availableCiphers.add(new CipherDescription("DESede", "CBC", "PKCS5Padding"));
        availableCiphers.add(new CipherDescription("DESede", "ECB", "NoPadding"));
        availableCiphers.add(new CipherDescription("DESede", "ECB", "PKCS5Padding"));
        availableCiphers.add(new CipherDescription("RSA", "ECB", "PKCS1Padding"));
        availableCiphers.add(new CipherDescription("RSA", "ECB", "OAEPWithSHA-1AndMGF1Padding"));
        availableCiphers.add(new CipherDescription("RSA", "ECB", "OAEPWithSHA-256AndMGF1Padding"));
    }

    public SymmetricService() {}

    public byte[] generateKey(int blockSize) {
        final byte[] keyData = new byte[blockSize];
        final SecureRandom rnd;
        try {
            rnd = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
        rnd.nextBytes(keyData);
        return keyData;
    }

    public String generateKey(CipherDescription cipherDescription) {
        return new BASE64Encoder().encode(generateKey(cipherDescription.getBlockSize()));
//        return new String(key, UTF_8);
    }

    public static Set<CipherDescription> getAvailableCiphers() {
        return availableCiphers;
    }

    public String encrypt(final String plainMessage,
                          final CipherDescription cipherDescription) {
        final byte[] symKeyData = generateKey(cipherDescription.getBlockSize());
        final byte[] encodedMessage = plainMessage.getBytes(UTF_8);
        return encrypt(encodedMessage, symKeyData, cipherDescription);
    }

    public String encrypt(final String plainMessage,
                          final String key,
                          final CipherDescription cipherDescription) {
        final byte[] symKeyData = HashService.get(key, cipherDescription.getBlockSize());
        final byte[] encodedMessage = plainMessage.getBytes(UTF_8);
        return encrypt(encodedMessage, symKeyData, cipherDescription);
    }

    public String encrypt(final byte[] encodedMessage,
                          final byte[] symKeyData,
                          final CipherDescription cipherDescription) {
        try {

            final Cipher cipher = Cipher.getInstance(cipherDescription.toInitString());
            final int blockSize = cipher.getBlockSize();

            // create the key
            final SecretKeySpec symKey = new SecretKeySpec(symKeyData, cipherDescription.getAlg());

            // generate random IV using block size (possibly create a method for
            // this)
            final byte[] ivData = new byte[blockSize];
            final SecureRandom rnd = SecureRandom.getInstance("SHA1PRNG");
            rnd.nextBytes(ivData);
            final IvParameterSpec iv = new IvParameterSpec(ivData);

            cipher.init(Cipher.ENCRYPT_MODE, symKey, iv);

            final byte[] encryptedMessage = cipher.doFinal(encodedMessage);

            // concatenate IV and encrypted message
            final byte[] ivAndEncryptedMessage = new byte[ivData.length
                    + encryptedMessage.length];
            System.arraycopy(ivData, 0, ivAndEncryptedMessage, 0, blockSize);
            System.arraycopy(encryptedMessage, 0, ivAndEncryptedMessage,
                    blockSize, encryptedMessage.length);

            final String ivAndEncryptedMessageBase64 = DatatypeConverter
                    .printBase64Binary(ivAndEncryptedMessage);

            return ivAndEncryptedMessageBase64;
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException(
                    "key argument does not contain a valid key " + cipherDescription.getAlg());
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(
                    "Unexpected exception during encryption", e);
        }
    }

    public String decrypt(final String ivAndEncryptedMessageBase64,
                          final String symKeyHex,
                          final CipherDescription cipherDescription) {
        final byte[] symKeyData = HashService.get(symKeyHex, cipherDescription.getBlockSize());
        final byte[] ivAndEncryptedMessage = DatatypeConverter
                .parseBase64Binary(ivAndEncryptedMessageBase64);
        return decrypt(ivAndEncryptedMessage, symKeyData, cipherDescription);
    }

    public String decrypt(final byte[] ivAndEncryptedMessage,
                          final byte[] symKeyData,
                          final CipherDescription cipherDescription) {
        try {
            final Cipher cipher = Cipher.getInstance(cipherDescription.toInitString());
            final int blockSize = cipher.getBlockSize();

            // create the key
            final SecretKeySpec symKey = new SecretKeySpec(symKeyData, cipherDescription.getAlg());

            // retrieve random IV from start of the received message
            final byte[] ivData = new byte[blockSize];
            System.arraycopy(ivAndEncryptedMessage, 0, ivData, 0, blockSize);
            final IvParameterSpec iv = new IvParameterSpec(ivData);

            // retrieve the encrypted message itself
            final byte[] encryptedMessage = new byte[ivAndEncryptedMessage.length
                    - blockSize];
            System.arraycopy(ivAndEncryptedMessage, blockSize,
                    encryptedMessage, 0, encryptedMessage.length);

            cipher.init(Cipher.DECRYPT_MODE, symKey, iv);

            final byte[] encodedMessage = cipher.doFinal(encryptedMessage);

            // concatenate IV and encrypted message
            final String message = new String(encodedMessage, UTF_8);

            return message;
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException(
                    "key argument does not contain a valid AES key");
        } catch (BadPaddingException e) {
            return null;
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(
                    "Unexpected exception during decryption", e);
        }
    }

    public static void main(String[] args) throws Exception {
        SymmetricService service = new SymmetricService();
        CipherDescription cipherDescription = new CipherDescription("AES", "CBC", "PKCS5Padding");
//        String key = "Bar12345Bar12345";
        String key = service.generateKey(cipherDescription);
        System.out.println("key: " + key);
        String message = "ацуп34н5рук 634цр 45 4цукіен ghrt yttr en";
//        String encrypted = new String(service.encryptInit(key.getBytes(UTF_8), message.getBytes(UTF_8), iv, cipherDescription));
//        String decrypted = new String(service.decrypt(key.getBytes(UTF_8), encrypted.getBytes(UTF_8), iv, cipherDescription));
        String encrypted = service.encrypt(message, key, cipherDescription);
        String decrypted = service.decrypt(encrypted, key, cipherDescription);
        System.out.println(encrypted);
        System.out.println(decrypted);
        System.out.println(message.equals(decrypted));
    }
}
