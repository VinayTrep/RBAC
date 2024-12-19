package com.example.vrcSecurityAssignment.service;


import com.example.vrcSecurityAssignment.config.MessagePublisher;
import com.example.vrcSecurityAssignment.dtos.LoginUserDto;
import com.example.vrcSecurityAssignment.dtos.MailMessageDto;
import com.example.vrcSecurityAssignment.dtos.RegisterUserDto;
import com.example.vrcSecurityAssignment.dtos.SignupResponseDto;
import com.example.vrcSecurityAssignment.exception.*;
import com.example.vrcSecurityAssignment.model.ConfirmationToken;
import com.example.vrcSecurityAssignment.model.Role;
import com.example.vrcSecurityAssignment.model.User;
import com.example.vrcSecurityAssignment.model.constants.RoleName;
import com.example.vrcSecurityAssignment.repository.ConfirmationTokenRepository;
import com.example.vrcSecurityAssignment.repository.RoleRepository;
import com.example.vrcSecurityAssignment.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final MessagePublisher messagePublisher;

    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final RoleRepository roleRepository;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder, MessagePublisher messagePublisher, ConfirmationTokenRepository confirmationTokenRepository, RoleRepository roleRepository
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.messagePublisher = messagePublisher;

        this.confirmationTokenRepository = confirmationTokenRepository;
        this.roleRepository = roleRepository;
    }

    public SignupResponseDto signup(RegisterUserDto input) throws JsonProcessingException {
        //check if the email already exists in our system
        checkEmailExists(input.getEmail());

        Role role = roleRepository.findByName(RoleName.USER).orElseThrow(() -> new InvalidRoleException("Invalid User role"));
        User user = new User();
        user.setFullName(input.getFullName());
        user.setEmail(input.getEmail());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setEnabled(false);
        user.setRole(role);
        user = userRepository.save(user);

        // logic to create a confirmation token
        ConfirmationToken confirmationToken = createConfirmationToken(user);

        //  logic send email to the user to confirm their email address
        MailMessageDto mailMessageDto = new MailMessageDto();
        mailMessageDto.setFrom("vinaytrep2021@gmail.com");
        mailMessageDto.setTo(user.getEmail());
        mailMessageDto.setSubject("RBAC: Confirm your email");
        mailMessageDto.setBody("To confirm your account, please click here : "
                +"http://localhost:8005/auth/confirm-account?token="+confirmationToken.getConfirmationToken());
        ObjectMapper objectMapper = new ObjectMapper();
        String mailMessage = objectMapper.writeValueAsString(mailMessageDto);
        messagePublisher.sendNotification(mailMessage);

        return new SignupResponseDto(user.getFullName(), user.getEmail(), confirmationToken.getConfirmationToken(),user.getRole().getName().toString());
    }

    private  ConfirmationToken createConfirmationToken(User user) {
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setEmail(user.getEmail());
        confirmationToken.setConfirmationToken(UUID.randomUUID().toString());
        return confirmationTokenRepository.save(confirmationToken);
    }

    private void checkEmailExists(String email) throws UserEmailAlreadyExistsException {
        Optional<User> savedUser = userRepository.findByEmail(email);
        if(savedUser.isPresent()){
            throw new UserEmailAlreadyExistsException("Email already exists");
        }
    }

    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow( () -> new UserNotFoundException("User Not Found"));
    }

    public String confirmEmail(String confirmationToken) throws UserNotFoundException, VerifyEmailException, JsonProcessingException {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken).orElseThrow(
                () -> new InvalidTokenException("Confirmation Token not found")
        );

        if(token != null)
        {
            User user = userRepository.findByEmailIgnoreCase(token.getEmail()).orElseThrow(() -> new UserNotFoundException("User doesn't exist"));
            user.setEnabled(true);
            userRepository.save(user);

            //  logic send email to the user to confirm their email address
            MailMessageDto mailMessageDto = new MailMessageDto();
            mailMessageDto.setFrom("vinaytrep2021@gmail.com");
            mailMessageDto.setTo(user.getEmail());
            mailMessageDto.setSubject("RBAC: Verification confirmed");
            mailMessageDto.setBody("Email verified successfully!");
            ObjectMapper objectMapper = new ObjectMapper();
            String mailMessage = objectMapper.writeValueAsString(mailMessageDto);
            messagePublisher.sendNotification(mailMessage);
            return "Email verified successfully!";
        }
        throw new VerifyEmailException("Error: Couldn't verify email");
    }
}
