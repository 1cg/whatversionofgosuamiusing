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

        File buildsDir = Files.getBuildsDir();

        get("/", (req, resp)-> Index.render());

        get("/apps", (req, resp) -> {
            String applicationName = req.session().attribute("ApplicationName");
            return Index.MajorVersions.render(applicationName);
        });

        post("/apps", (req, resp) -> {
            String applicationName = req.queryParams("app");
            req.session().attribute("ApplicationName", applicationName);
            return Index.render();
        });

        post("/releases", (req, resp) -> {
           String versionName = req.queryParams("version");
           req.session().attribute("ReleaseName", versionName);
           return Index.render();
        });

        get("/releases", (req, resp) -> {
            String applicationName = req.session().attribute("ApplicationName");
            String versionName = req.session().attribute("ReleaseName");
           return Index.Releases.render(applicationName, versionName);
        });

        Main.main(args);


    }

}
