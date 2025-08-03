package com.DTO;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Slf4j
@ToString
@Validated
public class LoginRequest {

	@Schema(example = "avinashpatil07@gmail.com")
	@NotBlank(message = "Email cannot be empty")
	@Email(message = "Invalid email format")
	@JsonProperty(value = "email")
	private String email;
	
	@Schema(example = "Passw123")
	@NotBlank(message = "Password cannot be empty")
	//@Size(min = 8, message = "Password must be at least 8 characters long")
	@JsonProperty(value = "password")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message = "Password must contain at least one lowercase letter, one uppercase letter, and one digit")
	private String password;
}
