package app;


/**The code in this file is derived from www.codejava.net*/

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnzipUtility {

    private static final int BUFFER_SIZE = 4096;
    private static final String UNZIP_DESTINATION =  "./UnzippedFiles"; //@TODO: I am 100% sure this is wrong

    public List<String> getFileNamesFromZip(String zipFilePath) throws IOException {
        ArrayList<String> fileNames = new ArrayList<>();
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
        // iterates over entries in the zip file
        while (entry != null) {
            fileNames.add(new File(entry.getName()).getName());
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
        return fileNames;
    }

    //fileName ex: Server.java
    public void getFileFromZip(String zipFilePath, String fileName) throws IOException {
        File destDir = new File(UNZIP_DESTINATION);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
        String filePath = null;
        // iterates over entries in the zip file
        while (entry != null) {
            String entrySimpleName = new File(entry.getName()).getName();
            if (entrySimpleName.equals(fileName)) {
                filePath = UNZIP_DESTINATION + File.separator + entry.getName();
                break;
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        assert (filePath != null);
        extractFile(zipIn, filePath);
        zipIn.close();
    }

    public void unzip(String zipFilePath, String destDirectory) throws IOException {
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
        // iterates over entries in the zip file
        while (entry != null) {
            String filePath = destDirectory + File.separator + entry.getName();
            if (!entry.isDirectory()) {
                // if the entry is a file, extracts it
                extractFile(zipIn, filePath);
            } else {
                // if the entry is a directory, make the directory
                File dir = new File(filePath);
                dir.mkdir();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
    }
    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }
}