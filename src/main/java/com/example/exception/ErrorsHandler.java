package com.example.exception;

import javax.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.response.base.BaseResponse;

@ControllerAdvice
public class ErrorsHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = EntityNotFoundException.class)
	public ResponseEntity<BaseResponse> notFoundException(final NotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponse.notFound(e.getMessage()));
	}

	@ExceptionHandler(value = BadRequestException.class)
	public ResponseEntity<BaseResponse> badRequest(final BadRequestException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse.failed(e.getMessage()));
	}

	@ExceptionHandler(value = EmptyFieldException.class)
	public ResponseEntity<BaseResponse> emptryFieldException(final EmptyFieldException e) {
		return ResponseEntity.badRequest().body(BaseResponse.failed(e.getMessage()));
	}

	@ExceptionHandler(value = AuthorizationException.class)
	public ResponseEntity<BaseResponse> authException(final AuthorizationException e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(BaseResponse.unAuthorized(e.getMessage()));
	}

}
