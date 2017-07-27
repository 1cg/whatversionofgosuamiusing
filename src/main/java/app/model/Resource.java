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
    boolean alreadyExtracted = false;


    public Resource(File self, Resource parent) {
        this.self = self;
        this.parent = parent;
    }

    public String getName() {
        return self.getName();
    }

    //if is a directory
    public List<Resource> getResources() {
        assert (self.isDirectory() || self.getName().endsWith(".zip") || self.getName().endsWith(".jar"));
        if (resources == null) {
            findResources();
        }
        return resources;
    }

    //if is a non-directory
    public String getContent() {
        ensureExtracted();
        assert(self.isFile());
        assert(self.canRead());
        try {
            return new String(Files.readAllBytes(Paths.get(self.getAbsolutePath())));
        } catch (IOException e){
            //@TODO: handle
            return null;
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
                //TODO: handle error
                releasesFiles = new ArrayList<>();
            }
        } else {
            assert(self.isDirectory());
            releasesFiles = Arrays.asList(self.listFiles(new FilterFilesStartWNumber()));
        }


        for (File releaseFile : releasesFiles) {
            resources.add(new Release(releaseFile));
        }
    }

    //if your parent is a zip, extract yourself
    private void ensureExtracted() {
        if (!alreadyExtracted) {
            assert(self.exists());
            if (parent.self.getName().endsWith(".zip") || parent.self.getName().endsWith(".jar")) {
                try {
                    self = UnzipUtility.unzipFileFromZip(parent.self.getAbsolutePath(), self.getName());
                } catch (IOException e) {
                    //TODO: handle error
                }
            }
            assert(self.exists());
        }
        alreadyExtracted = true;
    }
}
