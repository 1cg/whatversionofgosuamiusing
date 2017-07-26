package app.model;

import java.io.File;
import java.util.List;

/**
 * Created by hkalidhindi on 7/26/2017.
 */
public class Resource {

    File self;
    Resource parent;
    List<Resource> resources;

    public Resource(File self, Resource parent) {
        this.self = self;
        this.parent = parent;
    }

    public String getName() {
        return self.getName();
    }

    public List<Resource> getResources() {
        extractSelf();
        if (resources == null) {
            findResources();
        }
        return resources;
    }

    private void findResources() {

    }

    private void extractSelf() {
        //@TODO: use unzipUtility
    }
}
