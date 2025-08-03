package com.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Entity.Role;
import com.Entity.User;
import com.Enum.RolesEnum;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {

	Optional<User> findByEmail(String email);
	
	Optional<User> findById(Long id);

	Boolean existsByEmail(String email);
	
	Boolean existsByRole(Role role);
}
