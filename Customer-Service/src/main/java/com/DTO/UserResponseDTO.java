package com.DTO;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Slf4j
public class UserResponseDTO {

	private String username;

	private String email;

	private String firstName;

	private String middleName;

	private String lastName;

	private long mobileNumber;

	private String password;

}
