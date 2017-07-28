package app;

import app.model.Application;
import app.model.Release;
import app.model.Resource;
import app.model.Version;
import bb.sparkjava.BBSparkTemplate;
import app.views.*;

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
            Application appByCode = Application.getAppByFileName(app);
            Version versionByCode = appByCode.getVersionByName(version);
            Release releaseByCode = versionByCode.getReleaseByName(release);
            Resource selectedResource = releaseByCode;
            if (path.length() > 0) {
                String[] pathArray = path.split("/");
                int i = 0;
                boolean zipped = false;
                while(i < pathArray.length) {
                    String currentResource = pathArray[i];
                    if (zipped) {
                        i += 1;
                        while (i < pathArray.length && !(currentResource.endsWith(".zip") || currentResource.endsWith(".jar"))) {
                            currentResource = currentResource + "/" + pathArray[i++];
                        }
                        i -= 1;
                    }
                    if (currentResource.endsWith(".zip") || currentResource.endsWith(".jar")) {
                        zipped = true;

                    }
                    selectedResource = selectedResource.getResourceByName(currentResource);
                    i += 1;
                }
            }
            return Explore.render(appByCode, versionByCode, releaseByCode, selectedResource, path);
        });

        //Main.main(args); // start up a jconsole TODO only in dev mode
    }

}
