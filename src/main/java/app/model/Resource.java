package app.model;

import app.UnzipUtility;

import java.io.File;
import java.nio.file.Files;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

/**
 * Created by hkalidhindi on 7/26/2017.
 */
public class Resource {

    private class FilterFilesStartWNumber implements FilenameFilter {
        @Override
        public boolean accept(File dir, String name) {
            return name.matches("[0-9].*");
        }
    }

    public File self;
    Resource parent;
    List<Resource> resources;
    boolean needsToBeExtracted;
    private boolean isDirectory;
    String name;
    public ZipEntry zipEntry= null;


    public Resource(File self, Resource parent) {
        this.self = self;
        this.name = self.getName();
        this.parent = parent;
        this.isDirectory = (self.isDirectory() || self.getName().endsWith(".zip") || self.getName().endsWith(".jar"));
        if (parent == null) {
            //release
            needsToBeExtracted = false;
        } else {
            if (parent.self.getName().endsWith(".zip") || parent.self.getName().endsWith(".jar")) {
                needsToBeExtracted = true;
            } else {
                needsToBeExtracted = false;
            }
        }
    }

    public Resource(ZipEntry zipEntry, Resource parent) {
        assert(!zipEntry.isDirectory());
        this.self = new File(zipEntry.getName());
        this.name = zipEntry.getName();
        this.zipEntry = zipEntry;
        this.parent = parent;
        this.isDirectory = (zipEntry.isDirectory() || self.getName().endsWith(".zip") || self.getName().endsWith(".jar"));
        needsToBeExtracted = true;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    //ex. Server.java, bin/j.java
    public String getName() {
        return name;
    }


    //if is a directory
    public List<Resource> getResources() {
        assert isDirectory;
        if (resources == null) {
            findResources();
        }
        return resources;
    }

    public List<Resource> getResources(String filter) {
        assert isDirectory;
        if (resources == null) {
            findResources();
        }
        return resources.stream().filter(resource -> resource.getName().contains(filter)).collect(Collectors.toList());
    }

    // TODO Harika: replace this with a real variable
    //      derived from the zip entry size (bytes) and the number of bytes read so far
    // Use a java.lang.Thread to do the unzipping the the background
    //
    // and the web app will
    //      recheck the progress every 700ms or so eventually re-rendering when the
    //      item is fully unzipped
    //
    private int _percentUnzipped = 0; // <- Make this real
    private Integer totalBytes = null;
    private int bytesRead = 0;

    public void setTotalBytes(int totalBytes){
        this.totalBytes = totalBytes;
    }

    public void bytesMoreRead(int bytesMoreRead) {
        bytesRead += bytesMoreRead;
        _percentUnzipped = bytesRead * 100 / totalBytes;
    }

    public boolean isUnzipping() {
        if (totalBytes == null) {
            return false;
        }
        return _percentUnzipped < 100;
    }

    public int getPercentUnzipped() {
        return _percentUnzipped;
    }

    //if is a directory
    public Resource getResourceByName(String resourceName) {
        assert isDirectory;
        if (resources == null) {
            findResources();
        }
        for (Resource resource : resources) {
            if (resource.getName().equals(resourceName)) {
                return resource;
            }
        }
        return null;
    }

    public Resource getResourceByPath(String path) {
        Resource selectedResource = this;
        if (path.length() > 0) {
            String[] pathArray = path.split("/");
            int i = 0;
            boolean zipped = false;
            while (i < pathArray.length) {
                String currentResource = pathArray[i];
                if (zipped) {
                    i += 1;
                    while (i < pathArray.length && !(currentResource.endsWith(".zip") || currentResource.endsWith(".jar"))) {
                        currentResource = currentResource + "/" + pathArray[i++];
                    }
                    i -= 1;
                }
                if (currentResource.endsWith(".zip") || currentResource.endsWith(".jar")) {
                    zipped = true;

                }
                selectedResource = selectedResource.getResourceByName(currentResource);
                i += 1;
            }
        }
        return selectedResource;
    }

    //if is a non-directory
    public String getContent() {
        ensureExtracted();
        assert(!isDirectory);
        assert(self.canRead());
        try {
            return new String(Files.readAllBytes(Paths.get(self.getAbsolutePath())));
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    private void findResources() {
        ensureExtracted();
        resources = new ArrayList<>();

        if (self.getName().endsWith(".zip") || self.getName().endsWith(".jar")) {
            try {
                List<ZipEntry> zipEntries = UnzipUtility.getEntriesFromZip(self.getAbsolutePath());
                for (ZipEntry zipEntry : zipEntries) {
                    if (!zipEntry.isDirectory()) {
                        resources.add(new Resource(zipEntry, this));
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            assert(self.isDirectory());
            List<File> resourceFiles = Arrays.asList(self.listFiles());
            for (File resourceFile : resourceFiles) {
                resources.add(new Resource(resourceFile, this));
            }
        }
    }

    //if your parent is a zip, extract yourself
    private void ensureExtracted() {
        if (needsToBeExtracted) {
            assert(self.exists());

//            Thread thread = new Thread(){
//                public void run(){
                    try {
                        self = UnzipUtility.unzipFileFromZip(parent.self.getAbsolutePath(), Resource.this);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
//                }
//            };
//
//            thread.start();

            assert(self.exists());
        }
        needsToBeExtracted = false;
    }
}
