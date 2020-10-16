package com.kdu.assignment3.geocoding;


import com.kdu.assignment3.constants.Constants;
import com.kdu.assignment3.exceptions.InvalidJsonResponseException;
import com.google.gson.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.kdu.assignment3.jpaclasses.LatLongAddr;
import com.kdu.assignment3.jpaclasses.TableOperations;
import org.apache.log4j.Logger;
import com.kdu.assignment3.validation.Validate;

/**
 * Class which handles getting the api response for an address.
 */
public class GetApiResponse {


    private static Logger logger=Logger.getLogger(GetApiResponse.class.getName());
    //the base url for the api call with the api key.

    private String finalUrlForAPICall= Constants.urlString;
    public LatLongAddr latLongAddr=new LatLongAddr();
    private JsonObject finalLatLong;

    /**
     * Establishes the http connection.
     * Gets the response from the connection.
     * @param address
     * @throws IOException
     * @throws InvalidJsonResponseException
     */
    public void getApiResponseAndExtractLatLong(String address) throws IOException, InvalidJsonResponseException {

            constructUrl(address);


            URL urlForGetRequest = new URL(finalUrlForAPICall);
            String readLine = null;
            HttpURLConnection connection = (HttpURLConnection) urlForGetRequest.openConnection();
            connection.setRequestMethod("GET");


            int responseCode = connection.getResponseCode();
            //checks if the connection was established properly
            if (responseCode == HttpURLConnection.HTTP_OK) {
                //Buffered reader to read the inputstream from the connection.
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                //The response reads the data from the buffered reader.
                StringBuilder response = new StringBuilder();
                while ((readLine = in .readLine()) != null) {
                    response.append(readLine);
                } in .close();

                getLatLongJson(response);

            }
            //Exception is thrown of the connection is not established.
            else {
                throw new ConnectException("Could not establish a proper connection. Please check your internet.");
            }
        }


    /**
     * Takes the address and constructs the url.
     * @param address
     */
    private void constructUrl(String address){

            latLongAddr.setAddress(address.toLowerCase());

            String[] urlAddress=address.split(" ");
            logger.info("Address is");
            for(String a:urlAddress){
                logger.info(a);
            }


            for(String s:urlAddress){
                finalUrlForAPICall=finalUrlForAPICall+s+",";
            }
            finalUrlForAPICall=finalUrlForAPICall.substring(0,finalUrlForAPICall.length()-1);
        }

    /**
     * Get the json containing the Lat, Lng from the response json.
     * @param response
     * @throws InvalidJsonResponseException
     * @throws IndexOutOfBoundsException
     */
     public void getLatLongJson(StringBuilder response) throws InvalidJsonResponseException,IndexOutOfBoundsException {

            //Extracting the latitude and longitude from the api response.
            JsonParser jsonParser = new JsonParser();
            JsonObject jo = (JsonObject)jsonParser.parse(String.valueOf(response));

            if(Validate.validateJSON(jo)) {

                JsonArray requiredResultArray = jo.getAsJsonArray("results");

                JsonArray resultLocations = requiredResultArray.get(0).getAsJsonObject().getAsJsonArray("locations");
                finalLatLong = resultLocations.get(0).getAsJsonObject().getAsJsonObject("latLng");


                getLatLongFromJson(finalLatLong);
            }
            else{
                throw new InvalidJsonResponseException("Please check the address you entered. There are no matching records");
            }
        }

    /**
     * Gets the latitude and longitude from the json containing lat long.
     * @param finalLatLong
     */
    public void getLatLongFromJson(JsonObject finalLatLong){

            String latitude;
            String longitude;

            String[] latLongString=String.valueOf(finalLatLong).split(",");


            latitude=latLongString[0].split(":")[1];
            longitude=latLongString[1].split(":")[1];

            logger.info("API response");
            logger.info("Latitude is:"+latitude);
            logger.info("Longitude is:"+longitude.substring(0,longitude.length()-1));


            //the JPA class for the table is set with the values fetched from the api.
            //This object is then used to store these values into the database.
            latLongAddr.setLatitude(Float.parseFloat(latitude.substring(0,latitude.length()-1)));
            latLongAddr.setLongitude(Float.parseFloat(longitude.substring(0,longitude.length()-1)));
            latLongAddr.setTimesAccessed(1);

            persistValuesToDatabase();
        }

    /**
     * To insert the values in the database.
     */
    public void persistValuesToDatabase(){
            TableOperations.insert(latLongAddr);
        }

}
