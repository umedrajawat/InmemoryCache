package com.kdu.assignment3.jpaclasses;

import com.kdu.assignment3.constants.Constants;
import com.kdu.assignment3.helpers.GetEntityManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 * This class has all the methods which handles JPA database operations.
 */
public class TableOperations {

    private  static Logger logger=Logger.getLogger(TableOperations.class.getName());


    private TableOperations(){

    }
    /**
     * Enters the object having the values to the database
     * @param latLongAddr
     */

    public static void insert(LatLongAddr latLongAddr) {
        PropertyConfigurator.configure("src/log4j.properties");

        if(!GetEntityManager.entityManager.getTransaction().isActive())
        {
            GetEntityManager.entityManager.getTransaction().begin();
        }

        GetEntityManager.entityManager.persist(latLongAddr);
        logger.info("Written into table.");

        GetEntityManager.entityManager.getTransaction().commit();

    }

    /**
     * It takes the address and fetches the records from the database for that address.
     * If the record is not present, it throws NoResultException.
     * @param address
     * @throws NoResultException
     */
    synchronized public static void retrieveFromDatabase(String address) throws NoResultException {

        Query query = GetEntityManager.entityManager.createNamedQuery(Constants.matchingAddressQuery,LatLongAddr.class);
        query.setParameter("address", address);

        LatLongAddr latLongAddr= (LatLongAddr) query.getSingleResult();

        logger.debug("Database lookup");
        logger.debug("Location:"+latLongAddr.getAddress()+"\nLatitude:"+latLongAddr.getLatitude()+"\nLongitude"+latLongAddr.getLongitude());


        updateTimesAccessed(latLongAddr);

        if(!GetEntityManager.entityManager.getTransaction().isActive())
        {
            GetEntityManager.entityManager.getTransaction().begin();
        }

        GetEntityManager.entityManager.persist(latLongAddr);

        GetEntityManager.entityManager.getTransaction().commit();
    }

    /**
     * Updates the number of times accessed by one.
     * Is called every time a record is looked up in the cache or database.
     * @param latLongAddr
     */
    public static void updateTimesAccessed(LatLongAddr latLongAddr){
        //Updating how many times it was accessed.
        latLongAddr.setTimesAccessed(1+latLongAddr.getTimes_accessed());
    }

}
