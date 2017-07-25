package app;

import app.model.Files;
import bb.sparkjava.BBSparkTemplate;
import javarepl.Main;
import app.views.*;

import java.io.File;

import static spark.Spark.*;

public class Server {

    public static void main(String[] args) throws Exception {
        staticFileLocation("/static");

        BBSparkTemplate.init();

        get("/", (req, resp) -> Index.render());

        get("/major_versions", (req, resp) -> {
            String applicationName = req.queryParams("app");
            return Index.MajorVersions.render(applicationName);
        });

        get("/releases", (req, resp) -> {
            String applicationName = req.queryParams("app");
            String majorVersion = req.queryParams("major-version");
            return Index.Releases.render(applicationName, majorVersion);
        });

        Main.main(args); // start up a jconsole TODO only in dev mode
    }

}
