package com.kdu.assignment3.exceptions;

//If the address entered is not valid, this exception is thrown.
public class InvalidAddressException extends Exception {

    public InvalidAddressException(String s){
        super(s);
    }
}
