package com.kdu.assignment3.cache;

import com.kdu.assignment3.constants.Constants;
import com.kdu.assignment3.helpers.GetEntityManager;
import com.kdu.assignment3.jpaclasses.LatLongAddr;
import com.kdu.assignment3.jpaclasses.TableOperations;
import org.apache.log4j.Logger;

import javax.persistence.Query;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class LatLongCache implements Runnable{
    private static Logger logger=Logger.getLogger(LatLongCache.class.getName());
    public static Map<String, LatLongAddr> inMemoryCache = new ConcurrentHashMap<String, LatLongAddr>();
    public static int threadTerminatingVariable =-1;

    /**
     * The top accessed 10 values are retrieved from the database.
     * This is passed updateCache which will repopulate the cache.
     */
    public static void refreshCache(){

        Query query = GetEntityManager.entityManager.createNamedQuery(Constants.getByTimesAccessedQuery,LatLongAddr.class);

        List<LatLongAddr> latLongAddr= (List<LatLongAddr>) query.getResultList();

        updateCacheValue(latLongAddr);
    }


    /**
     * The cache is populated with the top 10 accessed items according to frequency of access.
     * @param latLongAddr
     */
    public static void updateCacheValue(List<LatLongAddr> latLongAddr){
            int cacheLimit=0;
            for(LatLongAddr obj:latLongAddr){
                if (cacheLimit <10) {
                    inMemoryCache.put(obj.getAddress(), obj);
                    cacheLimit=cacheLimit+1;
                }
            }
    }

    /**
     * Updating the times_accessed field in the database after a cache lookup.
     * @param address
     */
    synchronized public static void updateTimesAccessedAfterCacheLookup(String address){

        Query query = GetEntityManager.entityManager.createNamedQuery(Constants.matchingAddressQuery, LatLongAddr.class);
        query.setParameter("address", address);

        LatLongAddr latLongAddr= (LatLongAddr) query.getSingleResult();

        TableOperations.updateTimesAccessed(latLongAddr);

        if(!GetEntityManager.entityManager.getTransaction().isActive()){
            GetEntityManager.entityManager.getTransaction().begin();
        }
        GetEntityManager.entityManager.getTransaction().commit();
        logger.info("DB updated after cache lookup");
    }
    /**
     * This is called when the thread is started in the main.
     * Here the refreshCache function is called every 2 seconds.
     * This thread is killed when main thread dies.
     */
    public void run() {

        while (threadTerminatingVariable !=0) {
            refreshCache();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }

        }
    }
}
