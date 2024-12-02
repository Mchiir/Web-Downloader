package com.digital;

import org.apache.commons.io.FileUtils;
import java.io.File;
import java.net.URL;

public class FileDownloader {
    public static long downloadFile(String fileURL, String saveDir) throws Exception {
        URL url = new URL(fileURL);
        File file = new File(saveDir + File.separator + "index.html");
        long startTime = System.currentTimeMillis();
        FileUtils.copyURLToFile(url, file);
        long endTime = System.currentTimeMillis();
        long fileSize = file.length() / 1024; // KB
        System.out.println("Downloaded " + file.getName() + " (" + fileSize + " KB) in " + (endTime - startTime) + " ms");
        return fileSize;
    }
}