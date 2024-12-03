package com.example.vrcSecurityAssignment.repository;

import com.example.vrcSecurityAssignment.model.Role;
import com.example.vrcSecurityAssignment.model.constants.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(RoleName name);
}
