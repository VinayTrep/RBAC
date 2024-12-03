package com.example.vrcSecurityAssignment.service;

import com.example.vrcSecurityAssignment.dtos.RegisterUserDto;
import com.example.vrcSecurityAssignment.model.Role;
import com.example.vrcSecurityAssignment.model.User;
import com.example.vrcSecurityAssignment.model.constants.RoleName;
import com.example.vrcSecurityAssignment.repository.RoleRepository;
import com.example.vrcSecurityAssignment.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> allUsers() {
        return userRepository.findAll();
    }

    public User createAdministrator(RegisterUserDto input) {
        Optional<Role> optionalRole = roleRepository.findByName(RoleName.ADMIN);

        if (optionalRole.isEmpty()) {
            return null;
        }

        var user = new User();
                user.setFullName(input.getFullName());
                user.setEmail(input.getEmail());
                user.setPassword(passwordEncoder.encode(input.getPassword()));
                user.setRole(optionalRole.get());
                user.setEnabled(false);

        return userRepository.save(user);
    }

}
