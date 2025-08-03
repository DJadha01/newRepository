package com.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Entity.Role;
import com.Enum.RolesEnum;
import java.util.List;

@Repository
public interface RoleRepo extends JpaRepository<Role, Integer>{
	
	Optional<Role> findByName(RolesEnum name);	
	Boolean existsByName(RolesEnum role);
}
