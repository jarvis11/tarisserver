package com.tarisserver.match;

/**
 * Created by Bhaskar on 8/13/2014.
 */
import com.tarisserver.match.MongoOps;
import com.tarisserver.match.AdParser;

import java.net.UnknownHostException;
import java.util.Date;

import com.mongodb.*;
import com.mongodb.util.JSON;

import java.util.List;
import java.util.Set;
import java.util.Arrays;
import java.util.ArrayList;

//org.json imports
import org.json.*;

public class main2 {
    public static void main(String[] args) {

        try{

            //CONNECT TO OUR DB
            MongoClient mongoClient = new MongoClient("localhost", 27017);
            DB tariDB = mongoClient.getDB("tarisapi_1");

            DBCollection campaigns = tariDB.getCollection("campaigns");

            // find all where i > 50
            DBObject query = new BasicDBObject("type", "local");

            List<DBObject> individual_campaigns = new ArrayList<DBObject>();

            DBCursor cursor = campaigns.find(query);
            try {
                while (cursor.hasNext()) {
                    //System.out.println(cursor.next());
                    individual_campaigns.add(cursor.next());
                }
            } finally {
                cursor.close();
            }
            
            AdParser myAdParser = new AdParser();
            JSONObject myCampaign = myAdParser.getAd(tariDB, campaigns, "53dd4d97ad65e0b823a87766", "23423");

            System.out.println(myCampaign);

            System.out.println();

            System.out.println(cursor.size());

            DBObject myDbObject = new BasicDBObject();


            //Get a SINGLE DOCUMENT by parsing through your docs and adding to a list
//            for(int i = 0; i < individual_campaigns.size(); i++){
//                if(individual_campaigns.get(i).)
//            }

            DBObject firstObject = individual_campaigns.get(0);

            //CONVERT DB OBJECT TO JSON
            String jsonObject_before = JSON.serialize(firstObject);
            //TO CONVERT BACK JUST USE
            //DBObject bson = ( DBObject ) JSON.parse( json );
            JSONObject jsonObject = new JSONObject(jsonObject_before);

            System.out.println(jsonObject);
            System.out.println();
            //System.out.println(jsonObject.getJSONArray("ads"));

            JSONArray adsArray = jsonObject.getJSONArray("ads");
            //GET THE ADS OF THE OBJECT
            JSONObject adObject = new JSONObject(adsArray.get(0).toString());

            //GET THE ACTUAL TARGETING SPEC OF AN AD
            System.out.println(adObject.getJSONObject("target"));
            System.out.println();

            JSONObject targetObject = new JSONObject(adObject.getJSONObject("target").toString());



            JSONArray descriptor_tags = targetObject.getJSONArray("microlocation_descriptor_tag");
            //System.out.println(adObject.getString("type"));

            for(int i = 0; i < descriptor_tags.length(); i++){
                System.out.print(descriptor_tags.get(i) + ", ");
            }

            System.out.println();
           // System.out.println(targetObject);
            //JSONArray targetArray = (adO)
            //JSONArray descriptor_tags = adObject.getJSONArray("microlocation_descriptor_tag");

            //System.out.println(descriptor_tags.get(0));

            DBCollection venues = tariDB.getCollection("venues");


            List<DBObject> individual_venues = new ArrayList<DBObject>();

            DBCursor venue_cursor = venues.find();
            try {
                while (venue_cursor.hasNext()) {
                    //System.out.println(cursor.next());
                    individual_venues.add(venue_cursor.next());
                }
            } finally {
                venue_cursor.close();
            }

//            for(int i = 0; i < individual_venues.size(); i++){
//                System.out.println(individual_venues.get(i));
//            }

            List<JSONObject> json_venues = new ArrayList<JSONObject>();

            for (int i = 0; i < individual_venues.size(); i++){
                String jOB = JSON.serialize(individual_venues.get(i));
                JSONObject jsonToAdd = new JSONObject(jOB);
                json_venues.add(jsonToAdd);
                System.out.println(json_venues.get(i));
            }

            List<JSONObject> venue_addresses = new ArrayList<JSONObject>();

            for (int i = 0; i < json_venues.size(); i++ ){

                JSONArray addressArray = json_venues.get(i).getJSONArray("addresses");

                for(int j = 0; j < addressArray.length(); j++){
                    JSONObject single_address = new JSONObject(addressArray.get(j).toString());

                    //ARRAY FULL OF VENUE ADDRESSES
                    venue_addresses.add(single_address);
                }
            }



            //GET AN ARRAY FULL OF MICROLOCATIONS
            List<JSONArray> venue_microlocations = new ArrayList<JSONArray>();

            for(int i= 0; i < venue_addresses.size(); i++){

                venue_microlocations.add(i, venue_addresses.get(i).getJSONArray("microlocations") );

            }


            for (int i = 0; i < venue_microlocations.size(); i++){
                System.out.println(venue_microlocations.get(i));
            }

            List<JSONObject> all_microlocations = new ArrayList<JSONObject>();

            System.out.println();
            System.out.println();
            System.out.println();

            for (int i = 0; i < venue_microlocations.size(); i++){
                JSONArray microlocationsArray = venue_microlocations.get(i);
                for (int j = 0; j < microlocationsArray.length(); j++){
                    JSONObject microlocation = new JSONObject(microlocationsArray.get(j).toString());

                    all_microlocations.add(microlocation);
                }
            }

            for(int z = 0; z < all_microlocations.size(); z++){

                if(all_microlocations.get(z).has("fish")){
                    System.out.println(all_microlocations.get(z).get("fish"));
                }else {

                    System.out.println("FALSE");



                }
            }

            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();

            JSONArray microlocationInQuestion = all_microlocations.get(0).getJSONArray("descriptor_tag");

            System.out.println(microlocationInQuestion.get(0));








        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (MongoException e) {
            e.printStackTrace();
        }


    }

}
