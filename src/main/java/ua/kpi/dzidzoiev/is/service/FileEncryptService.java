package ua.kpi.dzidzoiev.is.service;

import org.apache.commons.io.FilenameUtils;
import ua.kpi.dzidzoiev.is.MainApp;
import ua.kpi.dzidzoiev.is.service.symmetric.CipherDescription;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Created by dzidzoiev on 12/13/15.
 */
public class FileEncryptService {

    public static final int FILE_BUFFER_SIZE = 2048;

    public void encrypt(File source, File destDir, CipherDescription cipherDescription, String key) {
        if (!destDir.isDirectory())
            throw new IllegalStateException("Destination is not a directory");
        if (!source.isFile())
            throw new IllegalStateException("Source is not a file!");

        try {
            StatefulSymmetricEncoder encoder = new StatefulSymmetricEncoder();

            Path aSource = Paths.get(source.getAbsolutePath());
            Path aDest = Paths.get(createEncryptedFile(destDir, source));
            FileChannel sourceChannel = FileChannel.open(aSource, StandardOpenOption.READ);
            FileChannel destChannel = FileChannel.open(aDest, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
            ByteBuffer readBuffer = ByteBuffer.allocate(FILE_BUFFER_SIZE * 2);
            ByteBuffer writeBuffer = ByteBuffer.allocate(FILE_BUFFER_SIZE * 4);

            byte[] iv = encoder.encryptInit(key, cipherDescription);
            writeBuffer.put(iv);
            while (sourceChannel.read(readBuffer) > 0) {
                readBuffer.flip();
                writeBuffer.put(encoder.encrypt(readBuffer.array()));
                writeBuffer.flip();
                destChannel.write(writeBuffer);
                readBuffer.clear();
                writeBuffer.clear();
            }
            sourceChannel.close();
            destChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    private String createEncryptedFile(File directory, File oldFile) {
        String newName = "encrypted_" + FilenameUtils.getName(oldFile.getAbsolutePath());
        return new File(directory, newName).getAbsolutePath();
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        FileEncryptService fileEncryptService = new FileEncryptService();
        URL fileUrl = MainApp.class.getClassLoader().getResource("file/test.txt");
//        URL fileUrl = MainApp.class.getClassLoader().getResource("file/test_big.txt");
        System.out.println(fileUrl);
        File file = new File(fileUrl.toURI());
        fileEncryptService.encrypt(file, file.getParentFile(), new CipherDescription("AES", "CBC", "PKCS5Padding"), "key");
    }
}
