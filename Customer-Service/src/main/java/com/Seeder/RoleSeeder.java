package com.Seeder;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.Entity.Role;
import com.Enum.RolesEnum;
import com.Repository.RoleRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(1)
@Component
@RequiredArgsConstructor
public class RoleSeeder implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private RoleRepo rr;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {

		loadRoles();
		log.debug("Roles Added");

	}

	private void loadRoles() {

		RolesEnum[] roleEnums = new RolesEnum[] { RolesEnum.USER, RolesEnum.ADMIN, RolesEnum.SUPER_ADMIN };

		// Java 8–style Map creation
		Map<RolesEnum, String> roleDescription = new HashMap<>();
		roleDescription.put(RolesEnum.USER, "Default User Role");
		roleDescription.put(RolesEnum.ADMIN, "Administrator Role");
		roleDescription.put(RolesEnum.SUPER_ADMIN, "Super Administrator Role");

		for (RolesEnum roleEnum : roleEnums) {
			boolean isRoleEmpty = !rr.findByName(roleEnum).isPresent();

			if (isRoleEmpty) {
				Role role = new Role();
				role.setName(roleEnum);
				role.setDescription(roleDescription.get(roleEnum));
				rr.save(role);
				log.info("Role with name: {} created", role.getName());
			}
		}

	}
}
