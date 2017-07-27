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

    private String name;
    private String fileSystemName;
    private List<Version> versions = null;
    private static List<Application> APPLICATIONS = new ArrayList<>();
    static {
        APPLICATIONS.add(new Application("ClaimCenter", "CC"));
        APPLICATIONS.add(new Application("PolicyCenter", "PC"));
        APPLICATIONS.add(new Application("BillingCenter", "BC"));
    }

    public static Application getAppByFileName(String app) {
        return APPLICATIONS.stream().filter(application -> application.getFileSystemName().equals(app)).findFirst().orElse(null);
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

    public List<Version> getVersions() {
        if (versions == null) {
            findVersions();
        }
        return versions;
    }

    private void findVersions() {
        versions = new ArrayList<>();
        File buildsDir = Files.getBuildsDir();
        String appPathName = buildsDir.getAbsolutePath() +  File.separatorChar + fileSystemName;
        File appFile = new File(appPathName);
        assert(appFile.exists());
        List<File> versionFiles =  Arrays.asList(appFile.listFiles(new FilterFilesStartWNumber()));
        for (File versionFile : versionFiles) {
            versions.add(new Version(versionFile, this));
        }
    }

    public Version getVersionByName(String releaseName) {
        if (versions == null) {
            findVersions();
        }
        for (Version version : versions) {
            if (version.versionFile.getName().equals(releaseName)) {
                return version;
            }
        }
        return null;
    }

    public List<String> getVersionsNames() {
        if (versions == null) {
            findVersions();
        }
        List<String> versionNames = new ArrayList<>();
        for (Version version : versions) {
            versionNames.add(version.versionFile.getName());
        }
        return versionNames;
    }

    public List<String> getReleasesNames(String versionName) {
        Version version = getVersionByName(versionName);
        List<Release> releases = version.getReleases();
        List<String> releasesNames = new ArrayList<>();
        for (Release release : releases) {
            releasesNames.add(release.getName());
        }
        return releasesNames;
    }


    @Override
    public String toString() {
        return "Application{" +
                "name='" + name + '\'' +
                ", fileSystemName='" + fileSystemName + '\'' +
                '}';
    }

    public static void main(String[] args) {
//        for (Application app : APPLICATIONS) {
//            System.out.println(app.getName());
//            List<String> versions = app.getVersions();
//            for (String versionName : versions) {
//                System.out.println(versionName + ": " + app.getReleases(versionName));
//            }
//        }
    }

}
