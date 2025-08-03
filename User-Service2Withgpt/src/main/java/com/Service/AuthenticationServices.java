package com.Service;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.util.MultiValueMap;

import com.DTO.LoginRequest;
import com.DTO.LoginResponse;
import com.DTO.RegisterRequest;
import com.DTO.UserResponseDTO;

public interface AuthenticationServices {

	UserResponseDTO registerUser(RegisterRequest registerRequest);

	LoginResponse authenticate(LoginRequest loginRequest);

	public UserResponseDTO getAuthenticatedUser();

	public UserResponseDTO updateUserInservice(long id, @Valid RegisterRequest registerRequest);

	public boolean getKycStatus(Long userId);

	public void getReportInService(String format);

	void logout(String jwt);

}
