package app.model;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Spliterator;
import java.util.logging.Filter;

public class Application {

    private.
    class FilterVersion implements FilenameFilter{
        @Override
        public boolean accept(File dir, String name) {
            return !name.matches("\\..*");
        }
    }

    private String name;
    private String fileSystemName;

    private static List<Application> APPLICATIONS = new ArrayList<>();
    static {
        APPLICATIONS.add(new Application("ClaimCenter", "CC"));
        APPLICATIONS.add(new Application("PolicyCenter", "PC"));
        APPLICATIONS.add(new Application("BillingCenter", "BC"));
    }

    public Application(String name, String fileSystemName) {
        this.name = name;
        this.fileSystemName = fileSystemName;
    }

    public String getName() {
        return name;
    }

    public String getFileSystemName() {
        return fileSystemName;
    }

    public static List<Application> getAll() {
        return APPLICATIONS;
    }

    public List<String> getVersions() {
        File buildsDir = Files.getBuildsDir();
        String appPathName = buildsDir.getAbsolutePath() +  File.separatorChar + fileSystemName;
        File appFile = new File(appPathName);
        assert(appFile.exists());
        return Arrays.asList(appFile.list(new FilterVersion()));
    }

    @Override
    public String toString() {
        return "Application{" +
                "name='" + name + '\'' +
                ", fileSystemName='" + fileSystemName + '\'' +
                '}';
    }

    public static void main(String[] args) {
        for (Application app : APPLICATIONS) {
            System.out.println(app.getVersions().toString());
        }
    }

}
