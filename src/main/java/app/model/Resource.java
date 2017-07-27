package app.model;

import app.UnzipUtility;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

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
        if (parent == null) {
            alreadyExtracted = true;
        }
    }

    public String getName() {
        return self.getName();
    }

    //if is a directory
    public List<Resource> getResources() throws IOException {
        assert (self.isDirectory() || self.getName().endsWith(".zip") || self.getName().endsWith(".jar"));
        if (resources == null) {
            findResources();
        }
        return resources;
    }

    //if is a non-directory
    public String getContent() throws IOException {
        ensureExtracted();
        assert(self.isFile());
        assert(self.canRead());
        return new String(Files.readAllBytes(Paths.get(self.getAbsolutePath())));
    }

    private void findResources() throws IOException {
        ensureExtracted();
        resources = new ArrayList<>();


        List<File> releasesFiles;
        if (self.getName().endsWith(".zip") || self.getName().endsWith(".jar")) {
            releasesFiles = UnzipUtility.getFileListFromZip(self.getAbsolutePath());
        } else {
            assert(self.isDirectory());
            releasesFiles = Arrays.asList(self.listFiles(new FilterFilesStartWNumber()));
        }


        for (File releaseFile : releasesFiles) {
            resources.add(new Release(releaseFile));
        }
    }

    //if your parent is a zip, extract yourself
    private void ensureExtracted() throws IOException {
        if (!alreadyExtracted) {
            assert(self.exists());
            if (parent.self.getName().endsWith(".zip") || parent.self.getName().endsWith(".jar")) {
                self = UnzipUtility.unzipFileFromZip(parent.self.getAbsolutePath(), self.getName());
            }
            assert(self.exists());
        }
        alreadyExtracted = true;
    }
}
