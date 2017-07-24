package app.model;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

public class Files {

    public static final String BUILDS_LOCATION = "//Files/network/builds/";

    public static File getBuildsDir() {
        try {
            return new File( new URI( "file://" + BUILDS_LOCATION ) );
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }


}
