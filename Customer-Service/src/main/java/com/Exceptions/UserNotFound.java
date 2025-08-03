package com.Exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserNotFound extends RuntimeException{

	public UserNotFound(String msg) {
		super(msg);
		log.debug(msg);
		
	}

	
}
