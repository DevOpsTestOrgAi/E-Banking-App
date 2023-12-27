package com.ebanking.ClientService.exception;

import lombok.Data;

@Data
public class ServiceCustomException extends RuntimeException{

    private String errorCode;

    public ServiceCustomException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
