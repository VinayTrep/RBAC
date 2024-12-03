package com.example.vrcSecurityAssignment;


import com.example.vrcSecurityAssignment.model.Role;
import com.example.vrcSecurityAssignment.model.constants.RoleName;
import com.example.vrcSecurityAssignment.repository.RoleRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RoleSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final RoleRepository roleRepository;


    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.loadRoles();
    }

    private void loadRoles() {
        RoleName[] roleNames = new RoleName[] { RoleName.USER, RoleName.ADMIN, RoleName.SUPERADMIN };

        Arrays.stream(roleNames).forEach((roleName) -> {
            Optional<Role> optionalRole = roleRepository.findByName(roleName);

            optionalRole.ifPresentOrElse(System.out::println, () -> {
                Role roleToCreate = new Role();
                roleToCreate.setName(roleName);
                roleRepository.save(roleToCreate);
            });
        });
    }
}
