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

    File self;
    Resource parent;
    List<Resource> resources;
    boolean needsToBeExtracted;
    private boolean isDirectory;


    public Resource(File self, Resource parent) {
        this.self = self;
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

    public boolean isDirectory() {
        return isDirectory;
    }

    //ex. Server.java
    public String getName() {
        return self.getName();
    }

    //if is a directory
    public List<Resource> getResources() {
        assert isDirectory;
        if (resources == null) {
            findResources();
        }
        return resources;
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


        List<File> releasesFiles;
        if (self.getName().endsWith(".zip") || self.getName().endsWith(".jar")) {
            try {
                releasesFiles = UnzipUtility.getFileListFromZip(self.getAbsolutePath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            assert(self.isDirectory());
            releasesFiles = Arrays.asList(self.listFiles());
        }


        for (File releaseFile : releasesFiles) {
            resources.add(new Release(releaseFile));
        }
    }

    //if your parent is a zip, extract yourself
    private void ensureExtracted() {
        if (needsToBeExtracted) {
            assert(self.exists());
            {
                try {
                    self = UnzipUtility.unzipFileFromZip(parent.self.getAbsolutePath(), self.getName());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            assert(self.exists());
        }
        needsToBeExtracted = false;
    }
}
