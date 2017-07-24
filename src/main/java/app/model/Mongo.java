package app.model;


import com.mongodb.MongoClient;

public class Mongo {

    public static MongoClient getClient() {
        return new MongoClient();
    }

    public static final String DB_NAME = "gosu-version-info";

}
