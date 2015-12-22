package ua.kpi.dzidzoiev.is.signature;

import java.io.*;
import java.security.*;
import java.security.spec.*;

public class DsaSignatureGenerator {
    public static void main(String[] a) {
        if (a.length<3) {
            System.out.println("Usage:");
            System.out.println("java DsaSignatureGenerator keyFile"
                    + " msgFile sigFile");
            return;
        }
        String keyFile = a[0];
        String msgFile = a[1];
        String sigFile = a[2];
        String keyAlgo = "DSA";
        String sigAlgo = "SHA1withDSA";

        try {
            PrivateKey priKey = readPrivateKey(keyFile,keyAlgo);
            sign(msgFile,sigFile,sigAlgo,priKey);
        } catch (Exception e) {
            System.out.println("Exception: "+e);
            return;
        }
    }
    private static PrivateKey readPrivateKey(String input,
                                             String algorithm) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        FileInputStream priKeyStream = new FileInputStream(input);
        int priKeyLength = priKeyStream.available();
        byte[] priKeyBytes = new byte[priKeyLength];
        priKeyStream.read(priKeyBytes);
        priKeyStream.close();
        PKCS8EncodedKeySpec priKeySpec
                = new PKCS8EncodedKeySpec(priKeyBytes);
        PrivateKey priKey = keyFactory.generatePrivate(priKeySpec);
        System.out.println();
        System.out.println("Private Key Info: ");
        System.out.println("Algorithm = "+priKey.getAlgorithm());
        return priKey;
    }
    private static byte[] sign(String input, String output,
                               String algorithm, PrivateKey priKey) throws Exception {
        Signature sg = Signature.getInstance(algorithm);
        sg.initSign(priKey);
        System.out.println();
        System.out.println("Signature Object Info: ");
        System.out.println("Algorithm = "+sg.getAlgorithm());
        System.out.println("Provider = "+sg.getProvider());
        FileInputStream in = new FileInputStream(input);
        int bufSize = 1024;
        byte[] buffer = new byte[bufSize];
        int n = in.read(buffer,0,bufSize);
        int count = 0;
        while (n!=-1) {
            count += n;
            sg.update(buffer,0,n);
            n = in.read(buffer,0,bufSize);
        }
        in.close();
        FileOutputStream out = new FileOutputStream(output);
        byte[] sign = sg.sign();
        out.write(sign);
        out.close();
        System.out.println();
        System.out.println("Sign Processing Info: ");
        System.out.println("Number of input bytes = "+count);
        System.out.println("Number of output bytes = "+sign.length);
        return sign;
    }
}