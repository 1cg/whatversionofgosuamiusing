package app.model;

import java.util.ArrayList;
import java.util.List;

public class Application {

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

    @Override
    public String toString() {
        return "Application{" +
                "name='" + name + '\'' +
                ", fileSystemName='" + fileSystemName + '\'' +
                '}';
    }
}
