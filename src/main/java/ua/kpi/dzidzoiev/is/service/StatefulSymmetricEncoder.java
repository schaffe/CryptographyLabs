package ua.kpi.dzidzoiev.is.service;

import ua.kpi.dzidzoiev.is.service.symmetric.CipherDescription;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by dzidzoiev on 12/1/15.
 */
public class StatefulSymmetricEncoder {

    private Cipher cipher;

    public Cipher getCipher() {
        return cipher;
    }

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

    public byte[] encryptInit(final String key,
                              final CipherDescription cipherDescription) {
        final byte[] symKeyData = HashService.get(key, cipherDescription.getBlockSize());
        return encryptInit(symKeyData, cipherDescription);
    }

    public byte[] encryptInit(final byte[] symKeyData,
                              final CipherDescription cipherDescription) {
        try {
            cipher = Cipher.getInstance(cipherDescription.toInitString());
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

            return ivData;
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException(
                    "key argument does not contain a valid key " + cipherDescription.getAlg());
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(
                    "Unexpected exception during encryption", e);
        }
    }

    public byte[] encrypt(byte[] message) {
        try {
            return cipher.doFinal(message);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(
                    "Unexpected exception during encryption", e);
        }
    }

    public void decryptInit(final byte[] iv,
                            final String symKeyHex,
                            final CipherDescription cipherDescription) {
        final byte[] symKeyData = HashService.get(symKeyHex, cipherDescription.getBlockSize());
        decryptInit(iv, symKeyData, cipherDescription);
    }

    public void decryptInit(final byte[] ivData,
                            final byte[] symKeyData,
                            final CipherDescription cipherDescription) {
        try {
            cipher = Cipher.getInstance(cipherDescription.toInitString());
            final SecretKeySpec symKey = new SecretKeySpec(symKeyData, cipherDescription.getAlg());
            final IvParameterSpec iv = new IvParameterSpec(ivData);
            cipher.init(Cipher.DECRYPT_MODE, symKey, iv);
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException(
                    "key argument does not contain a valid AES key");
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(
                    "Unexpected exception during decryption", e);
        }
    }

    public byte[] decrypt(byte[] encryptedMessage) {
        try {
            return cipher.doFinal(encryptedMessage);
        } catch (IllegalBlockSizeException e) {
            throw new IllegalArgumentException(
                    "key argument does not contain a valid AES key");
        } catch (BadPaddingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
