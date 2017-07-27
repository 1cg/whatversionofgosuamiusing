package app.model;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hkalidhindi on 7/26/2017.
 */
public class Version {
    private String VERSION_RELEASES_SEPARATOR = "RELEASES";
    File versionFile;
    Application parent;
    List<Release> releases = null;

    private class FilterFilesStartWNumber implements FilenameFilter {
        @Override
        public boolean accept(File dir, String name) {
            return name.matches("[0-9].*");
        }
    }

    public Version(File versionFile, Application parent) {
        this.versionFile = versionFile;
        this.parent = parent;
    }

    public String getName() {
        return versionFile.getName();
    }

    private void findReleases(){
        File RELEASE = getRELEASEFile();
        releases = new ArrayList<>();

        File[] releasesFiles = RELEASE.listFiles(new FilterFilesStartWNumber());
        for (File releaseFile : releasesFiles) {
            releases.add(new Release(releaseFile));
        }
    }


    private File getRELEASEFile() {
        File RELEASE = new File(versionFile.getAbsolutePath() + File.separatorChar + "RELEASES");
        assert(RELEASE.exists());
        assert(RELEASE.isDirectory());
        return RELEASE;
    }

    public List<Release> getReleases(){
        if (releases == null) {
            findReleases();
        }
        return releases;
    }

    public Release getReleaseByName(String releaseName) {
        if (releases == null) {
            findReleases();
        }
        for (Release release : releases) {
            if (release.getName().equals(releaseName)) {
                return release;
            }
        }
        return null;
    }
}
