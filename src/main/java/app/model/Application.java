package app.model;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Application {

    private class FilterFilesStartWNumber implements FilenameFilter{
        @Override
        public boolean accept(File dir, String name) {
            return name.matches("[0-9].*");
        }
    }

    private String APP_VERSIONS_SEPARATOR = null;
    private String VERSION_RELEASES_SEPARATOR = "RELEASES";
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

    private List<String> getVersions() {
        File buildsDir = Files.getBuildsDir();
        String appPathName = buildsDir.getAbsolutePath() +  File.separatorChar + fileSystemName;
        File appFile = new File(appPathName);
        assert(appFile.exists());
        return Arrays.asList(appFile.list(new FilterFilesStartWNumber()));
    }

    private List<String> getReleases(String versionName) {
        String versionPath = Files.getBuildsDir().getAbsolutePath();
        if (APP_VERSIONS_SEPARATOR == null) {
            versionPath += File.separatorChar + fileSystemName + File.separatorChar + versionName;
        } else {
            versionPath += File.separatorChar + fileSystemName + File.separatorChar + APP_VERSIONS_SEPARATOR + File.separatorChar + versionName;
        }


        String releasesDirectoryPath;
        if (VERSION_RELEASES_SEPARATOR == null) {
            releasesDirectoryPath = versionPath;
        } else {
            releasesDirectoryPath = versionPath + File.separatorChar + VERSION_RELEASES_SEPARATOR;
        }
        File releasesDirectory = new File(releasesDirectoryPath);
        assert(releasesDirectory.exists());


        return Arrays.asList(releasesDirectory.list(new FilterFilesStartWNumber()));
    }

    public static List<String> getReleases(String appName, String versionName) {
        if (appName.equals("NO APP") || versionName.equals("NO VERSION")) {
            return new ArrayList<>();
        }
        for (Application app: APPLICATIONS) {
            if (app.getFileSystemName().equals(appName)) {
                return app.getReleases(versionName);
            }
        }
        throw new RuntimeException("Application: " + appName + " with version: " + versionName + " does not exist.");
    }
    public static List<String> getVersions(String fileSystemName) {
        if (fileSystemName.equals("NO APP")) {
            return new ArrayList<String>();
        }
        for (Application app: APPLICATIONS) {
            if (app.getFileSystemName().equals(fileSystemName)) {
                return app.getVersions();
            }
        }
        throw new RuntimeException("Application Name: " + fileSystemName + " does not exist.");
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
            System.out.println(app.getName());
            List<String> versions = app.getVersions();
            for (String versionName : versions) {
                System.out.println(versionName + ": " + app.getReleases(versionName));
            }
        }
    }

}