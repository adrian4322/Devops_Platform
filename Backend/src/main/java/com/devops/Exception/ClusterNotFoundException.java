package com.devops.Exception;

public class ClusterNotFoundException extends RuntimeException{
    public ClusterNotFoundException(String message){
        super(message);
    }
}