package com.Entity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.validation.annotation.Validated;

import com.Enum.UserStatusEnum;
import com.Security.CustomeUserService;
//import com.Enum.KycStatusEnum;
//import com.Enum.UserStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@Builder
@Entity
@Slf4j
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class User implements CustomeUserService{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "username", nullable = false)
	private String username;

	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "first_name", length = 50, nullable = false)
	private String firstName;


	@Column(name = "last_name", length = 100, nullable = false)
	private String lastName;

	
	@Column(name = "password", length = 255)
	private String password;
	
	@ManyToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "Role_Id", referencedColumnName = "id", nullable = false)
	@JsonIgnore
	private Role role;


	@Enumerated(EnumType.STRING)
	@Column(name = "Active_status", nullable = false)
	private UserStatusEnum status;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false, nullable = false)
	private Instant createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at", nullable = false)
	private  LocalDateTime updatedAt;


    @Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		log.debug("getAuthorities()");
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.getName().toString());
		return Collections.singletonList(authority);
	}

	@Override
	public boolean isAccountNonExpired() {
		log.debug("isAccountNonExpired()");
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		log.debug("isAccountNonLocked()");
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		log.debug("isCredentialsNonExpired()");
		return true;
	}

	@Override
	public boolean isEnabled() {
		log.debug("isEnabled()");
		return true;
	}

	



}
