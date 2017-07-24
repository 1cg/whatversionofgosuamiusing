package app;

import bb.sparkjava.BBSparkTemplate;
import javarepl.Main;
import app.views.*;

import static spark.Spark.*;

public class Server {

    public static void main(String[] args) throws Exception {
        staticFileLocation("/static");

        BBSparkTemplate.init();

        get("/", (req, resp)-> Index.render());

        Main.main(args);
    }

}
