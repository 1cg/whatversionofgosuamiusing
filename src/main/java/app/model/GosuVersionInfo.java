package app.model;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class GosuVersionInfo {

    public static final String GOSU_VERSION_INFO_COLLECTION = "gosu-version-info-collection";
    private final Document _document;

    private GosuVersionInfo(Document document) {
        _document = document;
    }

    public static Iterable<GosuVersionInfo> getAll() {
        try(MongoClient client = Mongo.getClient()) {
            MongoDatabase db = client.getDatabase(Mongo.DB_NAME);

            MongoCollection<Document> collection = db.getCollection(GOSU_VERSION_INFO_COLLECTION);
            FindIterable<Document> documents = collection.find();
            return documents.map(GosuVersionInfo::new);
        }
    }

}
