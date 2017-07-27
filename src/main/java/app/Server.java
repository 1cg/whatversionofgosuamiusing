package app;

import app.model.Application;
import app.model.Files;
import bb.sparkjava.BBSparkTemplate;
import javarepl.Main;
import app.views.*;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Collectors;

import static spark.Spark.*;

public class Server {

    public static void main(String[] args) throws Exception {
        staticFileLocation("/static");

        BBSparkTemplate.init();

        get("/", (req, resp) -> {
            if ("versions".equals(req.queryParams("ic-target-id"))) {
                String app = req.queryParams("app");
                if (app.isEmpty()) {
                    return "";
                } else {
                    resp.header("X-IC-PushURL", "/" + app);
                    return Index.Versions.render(Application.getByCode(app), null);
                }
            } else {
                return Index.render(null, null);
            }
        });

        get("/:app", (req, resp) -> {
            if ("releases".equals(req.queryParams("ic-target-id"))) {
                String app = req.queryParams("app");
                String version = req.queryParams("version");
                if (version.isEmpty()) {
                    return "";
                } else {
                    resp.header("X-IC-PushURL", "/" + app + "/" + version);
                    return Index.Versions.Releases.render(Application.getByCode(app), version);
                }
            } else {
                return Index.render(Application.getByCode(req.params("app")), null);
            }
        });

        get("/:app/:version", (req, resp) -> Index.render(Application.getByCode(req.params("app")), req.params("version")));

        get("/:app/:version/:release/*", (req, resp) -> {
            String app = req.params("app");
            String version = req.params("version");
            String release = req.params("release");
            String path = Arrays.asList(req.splat()).stream().collect(Collectors.joining("/"));
            Application appByCode = Application.getByCode(app);
            return Explore.render(appByCode, version, release, path);
        });

        //Main.main(args); // start up a jconsole TODO only in dev mode
    }

}
