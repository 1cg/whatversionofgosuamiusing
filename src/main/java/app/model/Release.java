package app.model;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;

import java.io.File;

/**
 * Created by hkalidhindi on 7/26/2017.
 */
public class Release extends Resource {

    private final Version _version;
    String _gosuVersionInfo = null;

    public Release(Version version, File self) {
        super(self, null);
        _version = version;
    }

    public String getGosuVersionInfo() {
        if (_gosuVersionInfo == null) {
            MongoDatabase db = Mongo.getClient().getDatabase("gosu_release_info");
            MongoCollection<Document> collection = db.getCollection("releases_info");
            Document doc = collection.find(new Document("_id", getMongoID())).first();
            if (doc != null) {
                _gosuVersionInfo = doc.getString("version_info");
            } else {
                _gosuVersionInfo = "No Info";
            }
        }
        return _gosuVersionInfo;
    }

    public void setGosuVersionInfo(String version) {
        MongoDatabase db = Mongo.getClient().getDatabase("gosu_release_info");
        MongoCollection<Document> collection = db.getCollection("releases_info");
        Document doc = collection.find(new Document("_id", getMongoID())).first();
        Document query = new Document("_id", getMongoID()).append("version_info", version);
        if (doc != null) {
            collection.replaceOne(
                    new Document("_id", getMongoID()),
                    query,
                    new UpdateOptions().upsert( true ));
        } else {
            collection.insertOne(query);
        }
        _gosuVersionInfo = version;
    }

    public Object getMongoID() {
        return _version.getApplication().getFileSystemName() + "-" + _version.getName() + "-" + this.getName();
    }
}
