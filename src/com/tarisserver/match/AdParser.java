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


    /**
     * Ge the JSONObject of a single advertisement from our central database
     * @param collection
     * @param campaign_id
     * @param advertisement_id
     * @return
     */

    //GETAD WITHOUT ERROR CHECKS
    public static JSONObject getAd(DBCollection collection, String campaign_id, String advertisement_id){

        //FIRST FIND REQUIRED CAMPAIGN DOCUMENT WITHIN A COLLECTION

        DBObject query = new BasicDBObject("_id", new ObjectId(campaign_id));
        DBObject campaign = collection.findOne(query);

        //NOW CONVERT YOUR CAMPAIGN FROM A DBOJECT INTO A JSON OBJECT FOR EASY PARSING
        String serialized_json = JSON.serialize(campaign);
        JSONObject campaign_json = new JSONObject(serialized_json);

        //INITIALIZE AD TO BE RETURNED
        JSONObject myAd = new JSONObject();
        //ENSURE CAMPAIGN HAS ADS
        if(campaign_json.has("ads")){
            //GET AN ARRAY OF ADS BELONGING TO THE CAMPAIGN
            JSONArray campaign_ads = campaign_json.getJSONArray("ads");
            for(int i = 0; i < campaign_ads.length(); i++){
                JSONObject ad = new JSONObject(campaign_ads.get(i).toString());

                if(ad.getJSONObject("_id").get("$oid").equals(advertisement_id)) {
                    myAd = ad;
                }
            }
        }
        return myAd;
    }

    

    //WITH ERROR CHECKING AKA AMATURE WAY
//    public static JSONObject getAd(DBCollection collection, String campaign_id, String advertisement_id){
//
//        //FIRST FIND REQUIRED CAMPAIGN DOCUMENT WITHIN A COLLECTION
//        DBObject query;
//        if(ObjectId.isValid(campaign_id)){
//            query = new BasicDBObject("_id", new ObjectId(campaign_id));
//            System.out.println("VALID");
//        } else {
//            JSONObject campaignDoesNotExist = new JSONObject();
//            campaignDoesNotExist.put("Message", "Campaign with id " + campaign_id + " does not exist");
//            //return campaignDoesNotExist;
//            return null;
//        }
//
//        //DBObject query = new BasicDBObject("_id", new ObjectId(campaign_id));
//        DBObject campaign = collection.findOne(query);
//        if(campaign == null){
//            JSONObject campaignDoesNotExist = new JSONObject();
//            campaignDoesNotExist.put("Message", "Campaign with id " + campaign_id + " does not exist");
//            //return campaignDoesNotExist;
//            return null;
//        }
//
//        //NOW CONVERT YOUR CAMPAIGN FROM A DBOJECT INTO A JSON OBJECT FOR EASY PARSING
//        String serialized_json = JSON.serialize(campaign);
//        JSONObject campaign_json = new JSONObject(serialized_json);
//
//        //INITIALIZE AD TO BE RETURNED
//        JSONObject myAd = new JSONObject();
//
//        //ENSURE CAMPAIGN HAS ADS
//        if(campaign_json.has("ads")){
//
//            //GET AN ARRAY OF ADS BELONGING TO THE CAMPAIGN
//            JSONArray campaign_ads = campaign_json.getJSONArray("ads");
//
//            for(int i = 0; i < campaign_ads.length(); i++){
//
//                JSONObject ad = new JSONObject(campaign_ads.get(i).toString());
//
//                if(ad.getJSONObject("_id").get("$oid").equals(advertisement_id)){
//                    myAd = ad;
//                } else{
//                    //myAd.put("Message", "Ad with id " + advertisement_id + " does not exist");
//                    myAd = null;
//                }
//            }
//
//        } else {
//            //myAd.put("Message", "Ad with id " + advertisement_id + " does not exist");
//            myAd = null;
//        }
//
//        return myAd;
//    }




    public static void main(String[] args) {

        try{

            //CONNECT TO OUR DB
            MongoClient mongoClient = new MongoClient("localhost", 27017);
            DB tarisDB = mongoClient.getDB("tarisapi_1");
            DBCollection campaigns = tarisDB.getCollection("campaigns");


            JSONObject advertisement = AdParser.getAd(campaigns, "53e51c45d135ef0000728fd2", "53e51f61d135ef0000728fd3" );
            //JSONObject advertisement = AdParser.getAd(campaigns, "53dd4d97ad65e0b823a87766", "53dd58b89a7c2cbc226f87d4");
            System.out.println(advertisement);




        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (MongoException e) {
            e.printStackTrace();
        }


    }



}
