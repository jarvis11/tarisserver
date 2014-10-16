package com.tarisserver.match;

import com.mongodb.*;
import com.mongodb.util.JSON;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bhaskar on 8/18/2014.
 *
 * Taris Java server
 */
public class trialLogicUnorganized {

    public static void main(String[] args) {

        try{

            //CONNECT TO OUR DB
            MongoClient mongoClient = new MongoClient("localhost", 27017);
            DB tarisDB = mongoClient.getDB("tarisapi_1");
            DBCollection campaigns = tarisDB.getCollection("campaigns");



            //JSONObject advertisement = AdParser.getAd(campaigns,"53ee435285f90ca814282e57", "53ee438c85f90ca814282e59");
            //JSONObject advertisement = AdParser.getAd(campaigns, "53e51c45d135ef0000728fd2", "53e51f61d135ef0000728fd3");
            JSONObject advertisement = AdParser.getAd(campaigns, "53dd4d97ad65e0b823a87766", "53dd58b89a7c2cbc226f87d4");

            System.out.println("ADVERTISEMENT");
            System.out.println("=============");
            System.out.println(advertisement);


            //Get the bid of the ad
            Double bidAmount = advertisement.getJSONObject("bid").getDouble("amount");
            System.out.println(bidAmount);

            System.out.println();
            System.out.println("VENUE MATCHES");
            System.out.println("=============");

            //System.out.println(advertisement.getJSONObject("target").has("venue_name"));
            //GRAB ALL VENUES
            List<JSONObject> json_venues = VenueParser.getVenues(tarisDB.getCollection("venues"));


            List<JSONObject> json_microlocations = new ArrayList<JSONObject>();

            //NOW WE HAVE A LIST FULL OF JSON VENUES. LET PARSE THROUGH THAT LIST AND FIND MATCHES, ADDING ALL VALID MICROLOCATIONS TO A NEW ARRAY
            for(int i = 0; i < json_venues.size(); i++){

                //CHECK IF AD HAS VENUE NAME PARAMETER, AND IF IT MATCHES OUR VENUE, THEN ONLY CONCENTRATE ON THAT VENUE
                //ELSE CHECK ADDRESS PARAMETERS
                if((advertisement.getJSONObject("target").has("venue_name") && advertisement.getJSONObject("target").get("venue_name").equals(json_venues.get(i).get("name")))
                        || !advertisement.getJSONObject("target").has("venue_name")){



                    //GET JSON ARRAY OF ADDRESSES
                    JSONArray addressArray = json_venues.get(i).getJSONArray("addresses");

                    for(int j = 0; j < addressArray.length(); j++){

                        //NOW CHECK EACH ADDRESS FOR AD PARAMETERS
                        if((advertisement.getJSONObject("target").has("venue_street")
                                && advertisement.getJSONObject("target").get("venue_street")
                                .equals(addressArray.getJSONObject(i).get("street")))
                                || !advertisement.getJSONObject("target").has("venue_street")){

                            if((advertisement.getJSONObject("target").has("venue_city")
                                    && advertisement.getJSONObject("target").get("venue_city")
                                    .equals(addressArray.getJSONObject(i).get("city")))
                                    || !advertisement.getJSONObject("target").has("venue_city")){

                                if((advertisement.getJSONObject("target").has("venue_state")
                                        && advertisement.getJSONObject("target").get("venue_state")
                                        .equals(addressArray.getJSONObject(i).get("state")))
                                        || !advertisement.getJSONObject("target").has("venue_state")){


                                    if((advertisement.getJSONObject("target").has("venue_zip")
                                            && advertisement.getJSONObject("target").get("venue_zip")
                                            .equals(addressArray.getJSONObject(i).get("zip")))
                                            || !advertisement.getJSONObject("target").has("venue_zip")){

//                                        System.out.println(addressArray.getJSONObject(j).
//                                                getJSONArray("microlocations").getJSONObject(0).getJSONArray("descriptor_tag").get(0));

                                        JSONArray microlocations_array = addressArray.getJSONObject(j).getJSONArray("microlocations");
                                        for(int z = 0; z < microlocations_array.length(); z++){
                                            json_microlocations.add(microlocations_array.getJSONObject(z));
                                        }



                                    }
                                }
                            }
                        }

                    }
                }
            }

//            System.out.println(json_microlocations);
//            if(advertisement.getJSONObject("target").getJSONArray("microlocation_descriptor_tag").length() == 1){
//                System.out.println("TRUE");
//            }
//
//            System.out.println(advertisement.getJSONObject("target").getJSONArray("microlocation_descriptor_tag").get(0));

            List<JSONObject> descriptor_tag_matches = new ArrayList<JSONObject>();


            for(int i = 0; i < json_microlocations.size(); i++){

                System.out.println(json_microlocations.get(i));

                //CHECK IF EACH MICROLOCATIONS IS VALID
                //FIRST CHECK FOR DESCRIPTOR TAGS
                if(advertisement.getJSONObject("target").has("microlocation_descriptor_tag")){

                    List<Object> adDescriptorTags = new ArrayList<Object>();
                    for(int j = 0; j < advertisement.getJSONObject("target").
                            getJSONArray("microlocation_descriptor_tag").length(); j++){
                        adDescriptorTags.add(advertisement.getJSONObject("target").
                                getJSONArray("microlocation_descriptor_tag").get(j));
                    }

                    List<Object> microlocationDescriptorTags = new ArrayList<Object>();
                    for(int j = 0; j < json_microlocations.get(i).getJSONArray("descriptor_tag").length(); j++){
                        System.out.println(json_microlocations.get(i).get("_id"));

                        microlocationDescriptorTags.add(json_microlocations.get(i).getJSONArray("descriptor_tag").get(j));
                    }


                    if(microlocationDescriptorTags.containsAll(adDescriptorTags)){
                        descriptor_tag_matches.add(json_microlocations.get(i));
                    }

                }  else {
                    descriptor_tag_matches.add(json_microlocations.get(i));
                }

            }



            List<JSONObject> action_tag_matches = new ArrayList<JSONObject>();


            for(int i = 0; i < descriptor_tag_matches.size(); i++){

                //CHECK IF EACH MICROLOCATIONS IS VALID
                //FIRST CHECK FOR DESCRIPTOR TAGS
                if(advertisement.getJSONObject("target").has("microlocation_action_tag")){

                    List<Object> adActionTags = new ArrayList<Object>();
                    for(int j = 0; j < advertisement.getJSONObject("target").
                            getJSONArray("microlocation_action_tag").length(); j++){
                        adActionTags.add(advertisement.getJSONObject("target").
                                getJSONArray("microlocation_action_tag").get(j));
                    }

                    List<Object> microlocationActionTags = new ArrayList<Object>();
                    for(int j = 0; j < json_microlocations.get(i).getJSONArray("action_tag").length(); j++){

                        microlocationActionTags.add(json_microlocations.get(i).getJSONArray("action_tag").get(j));
                    }


                    if(microlocationActionTags.containsAll(adActionTags)){
                        action_tag_matches.add(descriptor_tag_matches.get(i));
                    }
                } else {
                    action_tag_matches.add(descriptor_tag_matches.get(i));
                }

            }

            List<JSONObject> final_matches = new ArrayList<JSONObject>();


            for(int i = 0; i < action_tag_matches.size(); i++){

                //CHECK IF EACH MICROLOCATIONS IS VALID
                //FIRST CHECK FOR DESCRIPTOR TAGS
                if(advertisement.getJSONObject("target").has("microlocation_price_tag")){

                    List<Object> adPriceTags = new ArrayList<Object>();
                    for(int j = 0; j < advertisement.getJSONObject("target").
                            getJSONArray("microlocation_price_tag").length(); j++){
                        adPriceTags.add(advertisement.getJSONObject("target").
                                getJSONArray("microlocation_price_tag").get(j));
                    }

                    List<Object> microlocationPriceTags = new ArrayList<Object>();
                    for(int j = 0; j < json_microlocations.get(i).getJSONArray("price_tag").length(); j++){

                        microlocationPriceTags.add(json_microlocations.get(i).getJSONArray("price_tag").get(j));
                    }

                    if(microlocationPriceTags.containsAll(adPriceTags)){
                        final_matches.add(action_tag_matches.get(i));
                    }
                } else {
                    final_matches.add(action_tag_matches.get(i));
                }

            }

            for(int i = 0; i < final_matches.size(); i++){
                System.out.println(final_matches.get(i).get("uuid"));
            }




        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (MongoException e) {
            e.printStackTrace();
        }
    }
}
