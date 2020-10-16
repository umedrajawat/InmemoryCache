package com.kdu.assignment3.exceptions;

//Custom exception for connection problem with the api.
public class ApiConnectionException extends Exception{

    public ApiConnectionException(String s){
        super(s);
    }
}
