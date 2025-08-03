package com.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.DTO.LoginRequest;
import com.DTO.RegisterRequest;
import com.DTO.UserResponseDTO;
import com.Service.AuthenticationServices;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication APIs", description = "All authentication related API's")
public class AuthenticController {
	
	@Autowired
	private AuthenticationServices authentic;

	@Operation(summary = "Register a new Customer", description = "With provided details register a new customer")
	@ApiResponses({@ApiResponse(responseCode = "201", description = "User Successfully registered"), @ApiResponse(responseCode = "400", description = "Bad Request")})
	@PostMapping("/signup")
	public ResponseEntity<?> userSignUp(@RequestBody @Valid RegisterRequest registerRequest){
		
		log.info("registerRequest "+ registerRequest);
		
		return new ResponseEntity(authentic.registerUser(registerRequest), HttpStatus.OK);
	}
	
	
	@Operation(summary = "Customer Login", description = "With provided details login a customer")
	@ApiResponses({@ApiResponse(responseCode = "201", description = "User Successfully Logged In"), @ApiResponse(responseCode = "400", description = "Bad Request")})
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest){
		
		log.info("Login Datails "+ loginRequest);
		
		return new ResponseEntity(authentic.authenticate(loginRequest), HttpStatus.OK);
	}
	
	  @PostMapping("/logout")
	    public ResponseEntity<String> logout(HttpServletRequest request) {
	        String authHeader = request.getHeader("Authorization");
	        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	            return ResponseEntity.badRequest().body("Invalid Authorization header");
	        }

	        String jwt = authHeader.substring(7).trim();
	        authentic.logout(jwt);
	        return ResponseEntity.ok("Logged out successfully");
	    }
	
}
 