package app.model;

import java.io.File;

/**
 * Created by hkalidhindi on 7/26/2017.
 */
public class Release extends Resource {

    public Release(File self) {
        super(self, null);
    }

    String gosuVersion = null;

    public void addGosuVersion(String version) {
        if (gosuVersion == null) {
            gosuVersion = version;
        } else {
            //@TODO: send some sort of feedback/double check
        }
    }
}
