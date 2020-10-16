package com.kdu.assignment3.exceptions;

//If the response json is not valid, this exception is thrown.
public class InvalidJsonResponseException extends Exception{

    public InvalidJsonResponseException(String s){
        super(s);
    }
}
