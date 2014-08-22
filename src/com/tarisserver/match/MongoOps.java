package com.tarisserver.match;

/**
 * Created by Bhaskar on 8/16/2014.
 */

//Java util imports
import java.util.List;
import java.util.Set;
import java.util.Arrays;
import java.util.ArrayList;

//org.json imports
import org.bson.types.ObjectId;
import org.json.*;

//Mongo Imports
import java.net.UnknownHostException;
import com.mongodb.*;
import com.mongodb.util.JSON;


public class MongoOps {

    public void findDocumentById(DBCollection collection, DBObject dbObj, String id) {

        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));
        dbObj = collection.findOne(query);
    }


}
