package com.Service;

import com.DTO.RegisterRequest;
import com.DTO.UserResponse;

public interface AuthenticationServices {

	public UserResponse UserRegister(RegisterRequest registerRequest);

}
