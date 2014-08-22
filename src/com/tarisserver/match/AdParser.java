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

public class AdParser {

//    DB database;
//    //JSONObject advertisement;
//    String campaign_id;
//    String advertisement_id;


    //RETURNS A SINGLE AD WITH ID advertisement_id
    public JSONObject getAd(DB database, DBCollection collection, String campaign_id, String advertisement_id){

        //SHOULD INCLUDE ERROR CHECK TO MAKE SURE CAMPAIGNS COLLECTION EXISTS!
        //DBCollection campaigns = database.getCollection(collection_name);

        //FIND THE CAMPAIGN YOU NEED TO FIND YOUR AD WITHIN
        DBObject query = new BasicDBObject("_id", new ObjectId(campaign_id));
        DBObject campaign = collection.findOne(query);

        //NOW CONVERT YOUR CAMPAIGN FROM A DBOJECT INTO A JSON OBJECT FOR EASY PARSING
        String serialized_json = JSON.serialize(campaign);
        JSONObject campaign_json = new JSONObject(serialized_json);
        JSONObject ad_json = new JSONObject();

//        DBObject directQuery = new BasicDBObject("ads._id", new ObjectId(advertisement_id));
//        DBObject ad = campaigns.findOne(directQuery);

        //NOW THAT WE HAVE GRABBED OUR CAMPAIGN, WE MUST FIND OUR AD WITHIN THE CAMPAIGN
        if(campaign_json.has("ads")){
            //GET AN ARRAY OF ADS BELONGING TO THE CAMPAIGN
            JSONArray campaign_ads = campaign_json.getJSONArray("ads");

            for(int i = 0; i < campaign_ads.length(); i++){
                JSONObject ad_in_question = new JSONObject(campaign_ads.get(i).toString());
                //System.out.println(ad_in_question.getJSONObject("_id").get("$oid"));
                if(ad_in_question.getJSONObject("_id").get("$oid").equals(advertisement_id)){
                    ad_json = ad_in_question;
                }



            }
        }




        return ad_json;

    }


    public static void main(String[] args) {

        try{

            //CONNECT TO OUR DB
            MongoClient mongoClient = new MongoClient("localhost", 27017);
            DB tarisDB = mongoClient.getDB("tarisapi_1");
            DBCollection campaigns = tarisDB.getCollection("campaigns");


            AdParser adParser = new AdParser();
            JSONObject myCampaign = adParser.getAd(tarisDB, campaigns, "53ee435285f90ca814282e57", "53ee438c85f90ca814282e59");

            System.out.println(myCampaign);


        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (MongoException e) {
            e.printStackTrace();
        }


    }



}