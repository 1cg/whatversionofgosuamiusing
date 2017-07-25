package app;

import app.model.Application;
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

        get("/versions", (req, resp) -> {
            String app = req.queryParams("app");
            if (app.isEmpty()) {
                return "";
            } else {
                resp.header("X-IC-PushURL", "/" + app);
                return Versions.render(Application.getByCode(app), null);
            }
        });

        get("/releases", (req, resp) -> {
            String app = req.queryParams("app");
            String version = req.queryParams("version");
            if (version.isEmpty()) {
                return "";
            } else {
                resp.header("X-IC-PushURL", "/" + app + "/" + version);
                return Releases.render(Application.getByCode(app).getReleases(version));
            }
        });

        get("/", (req, resp) -> Index.render(null, null));
        get("/:app", (req, resp) -> Index.render(Application.getByCode(req.params("app")), null));
        get("/:app/:version", (req, resp) -> Index.render(Application.getByCode(req.params("app")), req.params("version")));

        Main.main(args); // start up a jconsole TODO only in dev mode
    }

}
