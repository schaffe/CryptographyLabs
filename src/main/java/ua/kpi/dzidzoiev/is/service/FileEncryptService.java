package ua.kpi.dzidzoiev.is.service;

import org.apache.commons.io.FilenameUtils;
import ua.kpi.dzidzoiev.is.MainApp;
import ua.kpi.dzidzoiev.is.service.symmetric.CipherDescription;

import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;

/**
 * Created by dzidzoiev on 12/13/15.
 */
public class FileEncryptService {

    public static final int FILE_BUFFER_SIZE = 2048;
    public static final int KEY_SIZE = 56;
    public static final int KEY_SIZE_OFFSET = 0;

    public String encrypt(String source, String destDir, CipherDescription cipherDescription, String key) {
        StatefulSymmetricEncoder encoder = new StatefulSymmetricEncoder();
        String newName = createEncryptedFile(destDir, source);

        try (
            InputStream sourceStream = new BufferedInputStream(new FileInputStream(source), FILE_BUFFER_SIZE * 2);
//            OutputStream outputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(newName), FILE_BUFFER_SIZE * 2), Holder.UTF_8))
            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(newName), FILE_BUFFER_SIZE * 2))
        {
            byte[] iv = ByteBuffer.allocate(KEY_SIZE).put(encoder.encryptInit(key, cipherDescription)).array();
//            byte[] bytes = ByteBuffer.allocate(KEY_SIZE_OFFSET).putInt(cipherDescription.getBlockSize()).array();
//            outputStream.write(bytes);
            outputStream.write(iv);
            CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, encoder.getCipher());

            byte[] buffer = new byte[FILE_BUFFER_SIZE];
            int count;
            int offset = 0;
            while ((count = sourceStream.read(buffer)) > 0) {
                cipherOutputStream.write(buffer, offset, count );
            }
            cipherOutputStream.close();

            return newName;
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public String decrypt(String source, String destDir, CipherDescription cipherDescription, String key) {
        StatefulSymmetricEncoder decoder = new StatefulSymmetricEncoder();

        String newName = createDecryptedFile(destDir, source);

        try (
                InputStream sourceStream = new BufferedInputStream(new FileInputStream(source), FILE_BUFFER_SIZE * 2);
//                InputStream sourceStream = new ZipInputStream(new BufferedInputStream(new FileInputStream(source), FILE_BUFFER_SIZE * 2), Holder.UTF_8);
                OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(newName), FILE_BUFFER_SIZE * 2))
        {
//            byte[] key_size = new byte[KEY_SIZE_OFFSET];
            byte[] key_bytes_full = new byte[KEY_SIZE];
//            sourceStream.read(key_size);
            sourceStream.read(key_bytes_full);
//            int keySize = ByteBuffer.wrap(key_size).getInt();
            int keySize = cipherDescription.getBlockSize();
            byte[] keyBytes = new byte[keySize];
            System.arraycopy(key_bytes_full, 0, keyBytes, 0, keySize);

            decoder.decryptInit(keyBytes, key, cipherDescription);

            CipherInputStream cipherInputStream = new CipherInputStream(sourceStream, decoder.getCipher());

            byte[] buffer = new byte[FILE_BUFFER_SIZE];
            int count;
            int offset = 0;
            while ((count = cipherInputStream.read(buffer)) > 0) {
                outputStream.write(buffer, offset, count );
            }
            cipherInputStream.close();

            return newName;
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    private String createEncryptedFile(String directory, String oldFile) {
        String newName = "encrypted_" + FilenameUtils.getName(oldFile);
        return new File(directory, newName).getAbsolutePath();
    }

    private String createDecryptedFile(String directory, String oldFile) {
        String newName = "decrypted_" + FilenameUtils.getName(oldFile);
        return new File(directory, newName).getAbsolutePath();
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        FileEncryptService fileEncryptService = new FileEncryptService();
//        URL fileUrl = MainApp.class.getClassLoader().getResource("file/test.txt");
        URL folderUrl = MainApp.class.getClassLoader().getResource("file");
        URL fileUrl = MainApp.class.getClassLoader().getResource("file/test_big.txt");
        System.out.println(fileUrl);
        String encrFile = fileEncryptService.encrypt(fileUrl.getFile(), folderUrl.getFile(), new CipherDescription("AES", "CBC", "PKCS5Padding"), "key");
        fileEncryptService.decrypt(encrFile, folderUrl.getFile(), new CipherDescription("AES", "CBC", "PKCS5Padding"), "key");
    }
}
