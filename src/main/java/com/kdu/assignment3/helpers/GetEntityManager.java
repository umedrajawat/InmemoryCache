package com.kdu.assignment3.helpers;

import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Class to return us a single instance of entity manager.
 */
public class GetEntityManager {

    private static GetEntityManager getEntityManager=null;



    private static Logger logger=Logger.getLogger(GetEntityManager.class.getName());
    private static EntityManagerFactory emf=null;
    public static EntityManager entityManager=null;

    /**
     * Private constructor to initialize the entity manager once.
     */
    private GetEntityManager(){
        connectToDatabase();
    }

    /**
     * This static method is called initialize the entity manger and return the instance
     * of that entity manager.
     * @return
     */
    public static void initializeEntityManager(){
        if(getEntityManager==null){
            getEntityManager=new GetEntityManager();
        }
    }

    /**
     * Initializing the entity manager.
     * */
    private static void connectToDatabase()
    {
            emf= Persistence.createEntityManagerFactory("mysqlpersistence");
            entityManager=emf.createEntityManager();
            entityManager.getTransaction().begin();
            logger.info("Connected");
    }


}

