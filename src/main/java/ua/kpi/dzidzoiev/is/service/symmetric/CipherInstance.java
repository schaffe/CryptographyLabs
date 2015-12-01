package ua.kpi.dzidzoiev.is.service.symmetric;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dzidzoiev on 12/1/15.
 */
public class CipherInstance {
    public static final Pattern pattern = Pattern.compile("(.+)/(.+)/(.+) \\((\\d+)\\)");
    private String alg;
    private String mode;
    private String padding;
    private int keySize;

    public CipherInstance() {
    }

    public CipherInstance(String alg, String mode, String padding, int keySize) {
        this.alg = alg;
        this.mode = mode;
        this.padding = padding;
        this.keySize = keySize;
    }

    public String getAlg() {
        return alg;
    }

    public CipherInstance setAlg(String alg) {
        this.alg = alg;
        return this;
    }

    public String getMode() {
        return mode;
    }

    public CipherInstance setMode(String mode) {
        this.mode = mode;
        return this;
    }

    public String getPadding() {
        return padding;
    }

    public CipherInstance setPadding(String padding) {
        this.padding = padding;
        return this;
    }

    public int getKeySize() {
        return keySize;
    }

    public CipherInstance setKeySize(int keySize) {
        this.keySize = keySize;
        return this;
    }

    @Override
    public String toString() {
        return String.join("/", alg, mode, padding);
    }

    public static CipherInstance parseCipher(String str) {
        Matcher matcher = pattern.matcher(str);
        return new CipherInstance(matcher.group(1), matcher.group(2), matcher.group(3),
                Integer.parseInt(matcher.group(4)));
    }

    public static void main(String[] args) {

    }
    private static final Set<CipherInstance> availableCiphers;
    static {
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
}
