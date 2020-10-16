package com.kdu.assignment3.mainclasspackage;

import com.kdu.assignment3.exceptions.InvalidAddressException;
import com.kdu.assignment3.cache.LatLongCache;
import com.kdu.assignment3.helpers.AddressHelper;
import com.kdu.assignment3.helpers.GetEntityManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MainClass {

    static Logger logger=Logger.getLogger(MainClass.class.getName());


    public static void main(String[] args) throws IOException, InvalidAddressException {

        //To initialize single instance of the entity manager.
        GetEntityManager.initializeEntityManager();

        //The thread for the cache is started. The run method is called when the start is called.
        Runnable runnable= new LatLongCache();
        Thread thread=new Thread(runnable);

        thread.start();

        //The log4j.properties file path is set for the logger.
        PropertyConfigurator.configure("src/log4j.properties");
        Scanner s=new Scanner(System.in);


        //This flag is a static field of the cache class used to stop execution when the user wants.
        //The thread there is also terminated.
        while(LatLongCache.threadTerminatingVariable !=0){
            try {
                logger.info("Enter 0 if you want to exit,1 to continue");
                LatLongCache.threadTerminatingVariable = s.nextInt();
                if(LatLongCache.threadTerminatingVariable ==0){
                    System.exit(0);
                }
                else {
                    AddressHelper.getLatitudeLongitudeFromAddress();
                }
            }
            catch (InvalidAddressException | InputMismatchException e){
                logger.error(e.getMessage());
                System.exit(0);
            }
        }

    }

}
