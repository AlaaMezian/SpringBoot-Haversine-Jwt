package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmptyFieldException extends RuntimeException{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmptyFieldException(String message) {
        super(message + " " + "this field is required");
    }
    
    public EmptyFieldException(Throwable cause) {
        super(cause);
    }
}
