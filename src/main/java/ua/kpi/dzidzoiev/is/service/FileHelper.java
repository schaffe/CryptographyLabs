package ua.kpi.dzidzoiev.is.service;

import java.io.File;

/**
 * Created by dzidzoiev on 12/15/15.
 */
public class FileHelper {
    public static String getTempFile() {
        return "/media/Data/KPI/InformationSecurity/Labs/IndormationSecurity-JavaFX/src/main/resources/file/test.txt";
    }

    public static String getTempFolder() {
        return "/media/Data/KPI/InformationSecurity/Labs/IndormationSecurity-JavaFX/src/main/resources/file";
    }

    public static String getDir(String filePath) {
        return new File(filePath).getParent();
    }
}
