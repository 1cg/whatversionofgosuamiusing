package app.model;

import java.io.File;
import java.util.List;

/**
 * Created by hkalidhindi on 7/26/2017.
 */
public class Version {
    File versionFile;
    Application parent;
    List<Release> releases = null;

    public Version(File versionFile, Application parent) {
        this.versionFile = versionFile;
        this.parent = parent;
        findReleases();
    }

    private void findReleases(){

    }

    public List<Release> getReleases(){
        if (releases == null) {
            findReleases();
        }
        return releases;
    }
}
