package ua.kpi.dzidzoiev.is.service.symmetric;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by dzidzoiev on 12/1/15.
 */
public class CipherDescription {
    private String alg;
    private String mode;
    private String padding;
    private int blockSize;
    public Cipher cipher;

    public CipherDescription() {
    }



    public CipherDescription(String alg, String mode, String padding, int blockSize) {
        this.alg = alg;
        this.mode = mode;
        this.padding = padding;
        this.blockSize = blockSize;
    }

    public CipherDescription(String alg, String mode, String padding) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.alg = alg;
        this.mode = mode;
        this.padding = padding;
        this.cipher = Cipher.getInstance(this.toInitString());
        this.blockSize = cipher.getBlockSize();
    }

    public String getAlg() {
        return alg;
    }

    public CipherDescription setAlg(String alg) {
        this.alg = alg;
        return this;
    }

    public String getMode() {
        return mode;
    }

    public CipherDescription setMode(String mode) {
        this.mode = mode;
        return this;
    }

    public String getPadding() {
        return padding;
    }

    public CipherDescription setPadding(String padding) {
        this.padding = padding;
        return this;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public CipherDescription setBlockSize(int blockSize) {
        this.blockSize = blockSize;
        return this;
    }

    public String toInitString() {
        return String.join("/", alg, mode, padding);
    }

    @Override
    public String toString() {
        return String.join("/", alg, mode, padding) + " (" + blockSize + ")";
    }

    public static void main(String[] args) {

    }


}
