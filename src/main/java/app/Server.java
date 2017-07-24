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

        Main.main(args);
    }

}
