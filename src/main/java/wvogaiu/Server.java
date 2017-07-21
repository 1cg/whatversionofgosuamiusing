package wvogaiu;

import bb.sparkjava.BBSparkTemplate;
import wvogaiu.views.*;

import static spark.Spark.*;

public class Server {

    public static void main(String[] args) {
        staticFileLocation("/static");

        BBSparkTemplate.init();

        get("/", (req, resp)-> Index.render());
    }

}
