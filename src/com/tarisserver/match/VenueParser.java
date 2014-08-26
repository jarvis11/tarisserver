package com.tarisserver.match;

/**
 * Created by bhaskarravi on 8/26/14.
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


public class VenueParser {


    //Translates a Collection of Venue Documents into a List of individual Venue JSON Objects
    public static List<JSONObject> getVenues(DBCollection collection){

        List<DBObject> individual_venues = new ArrayList<DBObject>();

        DBCursor venue_cursor = collection.find();
        try {
            while (venue_cursor.hasNext()) {
                //System.out.println(cursor.next());
                individual_venues.add(venue_cursor.next());
            }
        } finally {
            venue_cursor.close();
        }

        List<JSONObject> json_venues = new ArrayList<JSONObject>();

        for (int i = 0; i < individual_venues.size(); i++){
            String jOB = JSON.serialize(individual_venues.get(i));
            JSONObject jsonToAdd = new JSONObject(jOB);
            json_venues.add(jsonToAdd);
        }

        return json_venues;


    }

    public static JSONObject getVenue(DBCollection collection, String venue_id){

        DBObject query = new BasicDBObject("_id", new ObjectId(venue_id));
        DBObject venue = collection.findOne(query);

        //NOW CONVERT YOUR CAMPAIGN FROM A DBOJECT INTO A JSON OBJECT FOR EASY PARSING
        String serialized_json = JSON.serialize(venue);
        JSONObject venue_json = new JSONObject(serialized_json);


        return venue_json;
    }

    public static void main(String[] args) {

        try{

            //CONNECT TO OUR DB
            MongoClient mongoClient = new MongoClient("localhost", 27017);
            DB tarisDB = mongoClient.getDB("tarisapi_1");
            DBCollection venues = tarisDB.getCollection("venues");


            List<JSONObject> venueList = VenueParser.getVenues(venues);
            System.out.println(venueList);

            JSONObject venue = VenueParser.getVenue(venues, "53fc8c8fda0d563c19ac36cd");
            System.out.println(venue);




        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (MongoException e) {
            e.printStackTrace();
        }


    }
}
