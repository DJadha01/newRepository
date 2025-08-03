package com.Controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DTO.RegisterRequest;
import com.DTO.UserResponseDTO;
import com.Service.AuthenticationServices;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@RequestMapping("/api/v1/users")
@Tag(name = "UserController APIs", description = "All authentication related API's")
public class UserController {

	
	@Autowired
	private AuthenticationServices authentic;

	@Operation(summary = "Get Authenticated User", description = "Retrieves the details of the currently authenticated user.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully retrieved the authenticated user's details"),
			@ApiResponse(responseCode = "401", description = "Unauthorized - The user is not authenticated") })
	@GetMapping("/me")
	public ResponseEntity<UserResponseDTO> getAuthenticatedUser() {
		log.debug("getAuthenticatedUser()");
		return ResponseEntity.ok(authentic.getAuthenticatedUser());
	}
	
	 @Operation(summary = "Update User", description = "Updates the user details for the specified user ID.")
		@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "User successfully updated"),
				@ApiResponse(responseCode = "400", description = "Bad Request - Invalid input"),
				@ApiResponse(responseCode = "404", description = "User not found") })
		@PutMapping("/{id}")
		public ResponseEntity<UserResponseDTO> updateUser(@PathVariable long id, @RequestBody @Valid RegisterRequest registerRequest) {
			log.debug("updateUser({}, {})", id, registerRequest.getEmail());
			return ResponseEntity.ok(authentic.updateUserInservice(id, registerRequest));
		}

	 @PostMapping("/{userId}/kyc-status")
		public Mono<Boolean> getKycStatus(@PathVariable Long userId) {
			return Mono.just(authentic.getKycStatus(userId));
		}	
	 
	 @GetMapping("/report/{format}")
		public ResponseEntity<?> generateReport(@PathVariable("format") String format) {
			log.info("format:"  + format);
			authentic.getReportInService(format);
			return new ResponseEntity("ok", HttpStatus.OK);
		}

	
}
