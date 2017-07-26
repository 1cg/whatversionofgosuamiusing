package app.model;

import java.io.File;
import java.util.List;

/**
 * Created by hkalidhindi on 7/26/2017.
 */
public class Version {
    File versionFile;
    Application parent;
    List<Release> releases;

    public Version(File versionFile, Application parent, List<Release> releases) {
        this.versionFile = versionFile;
        this.parent = parent;
        this.releases = releases;
        findReleases();
    }

    private void findReleases(){

    }

    public List<Release> getReleases(){
        return releases;
    }
}
