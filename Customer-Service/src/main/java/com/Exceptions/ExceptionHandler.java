package com.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.DTO.ErrorHandlingResponse;

@RestControllerAdvice
public class ExceptionHandler {
	
	@org.springframework.web.bind.annotation.ExceptionHandler(UserAlreadyExist.class)
	public ResponseEntity<ErrorHandlingResponse> UserAlreadyPresent(UserAlreadyExist re){
		
		return new ResponseEntity(new ErrorHandlingResponse().builder().msg(re.getMessage()).build(), HttpStatus.NOT_FOUND);
		
	}
	
	@org.springframework.web.bind.annotation.ExceptionHandler(UserRoleExist.class)
	public ResponseEntity<ErrorHandlingResponse> UserRolePresent(UserRoleExist rol){
		
		return new ResponseEntity(new ErrorHandlingResponse().builder().msg(rol.getMessage()).build(), HttpStatus.NOT_FOUND);
		
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(UserNotFound.class)
	public ResponseEntity<ErrorHandlingResponse> UserNotFoundByID(UserNotFound rol){
		
		return new ResponseEntity(new ErrorHandlingResponse().builder().msg(rol.getMessage()).build(), HttpStatus.NOT_FOUND);
		
	}

}
