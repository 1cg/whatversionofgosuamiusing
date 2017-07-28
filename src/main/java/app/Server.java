package app;

import app.model.Application;
import app.model.Release;
import app.model.Resource;
import app.model.Version;
import bb.sparkjava.BBSparkTemplate;
import app.views.*;
import spark.Request;

import java.net.URLEncoder;
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
                    return Index.Versions.render(Application.getAppByFileName(app), null);
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
                    return Index.Versions.Releases.render(Application.getAppByFileName(app), version);
                }
            } else {
                return Index.render(Application.getAppByFileName(req.params("app")), null);
            }
        });

        get("/:app/:version", (req, resp) -> Index.render(Application.getAppByFileName(req.params("app")), req.params("version")));

        get("/:app/:version/:release/*", (req, resp) -> {

            String app = req.params("app");
            String version = req.params("version");
            String release = req.params("release");
            String path = Arrays.asList(req.splat()).stream().collect(Collectors.joining("/"));
            String filter = req.queryParamOrDefault("filter", "");

            Application appByCode = Application.getAppByFileName(app);
            Version versionByCode = appByCode.getVersionByName(version);
            Release releaseByCode = versionByCode.getReleaseByName(release);
            Resource selectedResource = releaseByCode.getResourceByPath(path);

            // filter
            if ("resource-list".equals(req.queryParams("ic-target-id"))) {
                resp.header("X-IC-PushURL", filterUrl(req, filter));

                return Explore.ResourceList.render(appByCode,
                        versionByCode,
                        releaseByCode,
                        selectedResource,
                        path,
                        filter);

            } else {
                // full request
                return Explore.render(appByCode,
                        versionByCode,
                        releaseByCode,
                        selectedResource,
                        path,
                        filter);
            }

        });

        //Main.main(args); // start up a jconsole TODO only in dev mode
    }

    private static String filterUrl(Request req, String filter) {
        return fixPath(req) + (filter.isEmpty() ? "" : "?filter=" + URLEncoder.encode(filter));
    }

    private static String fixPath(Request req) {
        return req.pathInfo().replace("//", "/");
    }

}
