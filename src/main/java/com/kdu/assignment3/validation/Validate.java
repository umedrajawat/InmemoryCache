package com.kdu.assignment3.validation;

import com.google.gson.JsonObject;

import java.util.regex.Pattern;

public class Validate {

    private Validate(){

    }

    /**
     * Validates the address. Ensures that no special characters are present.
     * @param address
     * @return
     */
    public static Boolean validateAddressEntered(String address){
        //Uses an inbuilt pattern to match.
        return (!Pattern.compile("\\W").matcher(address).matches());
    }

    /**
     * Function validates the json response.
     * @param jsonObject
     * @return
     */
   public static Boolean validateJSON(JsonObject jsonObject){
            //Checks the status code of the Json response to check if the response was proper.
            JsonObject jsonObject1=jsonObject.getAsJsonObject("info");
            return (jsonObject1.getAsJsonPrimitive("statuscode").getAsInt() == 0);

    }
}
