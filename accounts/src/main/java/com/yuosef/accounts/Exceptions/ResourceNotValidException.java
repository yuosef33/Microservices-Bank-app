package com.yuosef.accounts.Exceptions;

public class ResourceNotValidException extends RuntimeException{
    public ResourceNotValidException(String message,String fieldName,String fieldValue) {
        super(String.format("%s not valid for field %s with value %s", message,fieldName,fieldValue));
    }

}
