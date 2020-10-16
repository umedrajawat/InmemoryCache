package com.kdu.assignment3.helpers;

import com.kdu.assignment3.exceptions.InvalidAddressException;
import com.kdu.assignment3.exceptions.InvalidJsonResponseException;
import com.kdu.assignment3.cache.LatLongCache;
import com.kdu.assignment3.jpaclasses.TableOperations;
import com.kdu.assignment3.geocoding.GetApiResponse;
import org.apache.log4j.Logger;
import com.kdu.assignment3.validation.Validate;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.util.Scanner;


public class AddressHelper {


    public static Logger logger=Logger.getLogger(AddressHelper.class.getName());
    public static Scanner s=new Scanner(System.in);

    /**
     * This functions takes the user input for address and checks
     * 1.First in the cache
     * 2.Calls databaseLookupOrApiCall if it is not present in the database.
     * @throws IOException
     * @throws InvalidAddressException
     */
    private AddressHelper(){

    }
    public static void getLatitudeLongitudeFromAddress() throws IOException, InvalidAddressException {

        //Taking address to be searched as input from the user.
        String address;
        logger.info("Enter the address you want to search without spaces:");
        address = s.nextLine();
        logger.info("Address is:" + address);

        //Validates the address.
        //If address is not validated an exception is thrown.
        if(Validate.validateAddressEntered(address)) {

                //If the records are present in the cache they are retrieved.
                //It gets the object for that address.
                //The address is the key in the map which is used as the cache.
                if (LatLongCache.inMemoryCache.containsKey(address)) {

                    logger.info("Records were found in the cache");
                    logger.info("Latitude is:" + LatLongCache.inMemoryCache.get(address).getLatitude());
                    logger.info("Longitude is:" + LatLongCache.inMemoryCache.get(address).getLongitude());

                    //After the access of the record, times_accessed field is updated in the database.
                    LatLongCache.updateTimesAccessedAfterCacheLookup(address);

                } else {
                    //Database lookup or api call is handled by this function.
                    databaseLookupOrApiCall(address);
                }
        }
        else{
                //If the address is not validated.
                throw new InvalidAddressException("Address contains invalid characters");
        }
    }


    /**
     * Calls the database lookup function.
     * If that throws NoResultException, the api call is initiated.
     * @param address
     */
    public static void databaseLookupOrApiCall(String address){

            try {
                TableOperations.retrieveFromDatabase(address);
            }
            catch (NoResultException e) {
                logger.info("Records were not found in the database. Calling the API now:");
                GetApiResponse getApiResponse = new GetApiResponse();
                try {
                    getApiResponse.getApiResponseAndExtractLatLong(address);
                }
                //If the api call was not successful or if the response did not have proper status codes,
                //exceptions are thrown. These exceptions are handled here.
                catch (IOException | InvalidJsonResponseException |IndexOutOfBoundsException e1) {
                    logger.error(e1.getMessage());
                }

            }
    }

}


