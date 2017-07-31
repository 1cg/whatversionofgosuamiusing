package app;


/**The code in this file is derived from www.codejava.net*/

import app.model.Resource;

import java.io.*;
import java.nio.file.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class UnzipUtility {

    private static final int BUFFER_SIZE = 4096;
    private static String UNZIP_DESTINATION = "unzipped_cache";


    public static List<File> getFileListFromZip(String zipFilePath) throws IOException {
        try (ZipFile zipFile = new ZipFile(zipFilePath)) {
            return Collections.list(zipFile.entries())
                    .stream()
                    .map(o -> new File(o.getName()))
                    .collect(Collectors.toList());
        }
    }

    public static List<ZipEntry> getEntriesFromZip(String zipFilePath) throws IOException {
        try (ZipFile zipFile = new ZipFile(zipFilePath)) {
            return Collections.list(zipFile.entries())
                    .stream()
                    .collect(Collectors.toList());
        }
    }

    public static File unzipFileFromZip(String zipFilePath, Resource resource) throws IOException {
        File destDir = new File(UNZIP_DESTINATION);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        try (ZipFile zipFile = new ZipFile(zipFilePath)) {
            InputStream zipEntryIn = zipFile.getInputStream(resource.zipEntry);
            String filePath = UNZIP_DESTINATION + File.separator + resource.getName();
            filePath = filePath.replace("/", ".");
            File destFile = new File(filePath);
            if (!destFile.exists()) {
                destFile.createNewFile();
            }
            extractFile(zipEntryIn, filePath, resource);
            zipEntryIn.close();
            return  new File(filePath);
        }
    }

    //fileName ex: Server.java
    public static File unzipFileFromZip(String zipFilePath, String fileName) throws IOException {
        File destDir = new File(UNZIP_DESTINATION);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
        String filePath = null;
        // iterates over entries in the zip file
        while (entry != null) {
            String entryName = entry.getName();
            if (entryName.equals(fileName)) {
                filePath = UNZIP_DESTINATION + File.separator + entryName;
                break;
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        assert (filePath != null);
        extractFile(zipIn, filePath);

//          extractFile(Paths.get(zipFilePath), fileName);

//        Path zipPath = Paths.get((zipFilePath));
//        Path endLoc = Paths.get(filePath);
//        extractFile(zipPath, fileName, endLoc);
        zipIn.close();
        return new File(filePath);
    }

    public static void extractFile(Path zipFile, String fileName, Path outputFile) throws IOException {
        // Wrap the file system in a try-with-resources statement
        // to auto-close it when finished and prevent a memory leak
        try (FileSystem fileSystem = FileSystems.newFileSystem(zipFile, null)) {
            Path fileToExtract = fileSystem.getPath(fileName);
            Files.copy(fileToExtract, outputFile);
        }
    }

    private static void extractFile(Path zipFile, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        try (FileSystem fileSystem = FileSystems.newFileSystem(zipFile, null)) {
            Files.copy(zipFile, bos);
        }
    }

    private static void extractFile(InputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }

    private static void extractFile(InputStream zipIn, String filePath, Resource resource) throws IOException {
        resource.setTotalBytes(zipIn.available());
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            resource.bytesMoreRead(read);
            //System.out.println(resource.getPercentUnzipped());
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }

}