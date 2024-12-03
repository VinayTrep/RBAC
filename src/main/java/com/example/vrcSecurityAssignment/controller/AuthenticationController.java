package com.example.vrcSecurityAssignment.controller;

import com.example.vrcSecurityAssignment.config.JwtService;
import com.example.vrcSecurityAssignment.dtos.LoginResponse;
import com.example.vrcSecurityAssignment.dtos.LoginUserDto;
import com.example.vrcSecurityAssignment.dtos.RegisterUserDto;
import com.example.vrcSecurityAssignment.dtos.SignupResponseDto;
import com.example.vrcSecurityAssignment.exception.VerifyEmailException;
import com.example.vrcSecurityAssignment.exception.VerifyPasswordException;
import com.example.vrcSecurityAssignment.model.User;
import com.example.vrcSecurityAssignment.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService,
                                    AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    //It will validate the email and password, then send the request to service layer
    // It is responsible to send the response back to the user after signup
    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> register(@RequestBody RegisterUserDto registerUserDto) {
        validateEmail(registerUserDto.getEmail());
        validatePassword(registerUserDto.getPassword());
        SignupResponseDto responseDto = authenticationService.signup(registerUserDto);
        return ResponseEntity.ok(responseDto);
    }

    //It will validate the email then send the request to service layer
    // It is responsible to send the JWT token back to the user after login
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        validateEmail(loginUserDto.getEmail());
        User authenticatedUser = authenticationService.authenticate(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
    // This endpoint is used to confirm the email
    @PostMapping("/confirm-account")
    public ResponseEntity<String> confirmUserAccount(@RequestParam("token") String token) {
        return ResponseEntity.ok(authenticationService.confirmEmail(token));
    }

    //This method verifies email is in correct format
    private void validateEmail(String email){
        if (email.isEmpty()){
            throw new VerifyEmailException("Email Cannot be Empty");
        }

        String emailRegex = "^[A-Za-z0-9+_. -]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);

        if (! matcher.matches()){
            throw new VerifyEmailException("Email format is not correct");
        }
    }
    //This method verifies password is in correct format i.e. min 8 char, 1 symbol,
    // 1 number, 1 upper case, 1 lower case
    private void validatePassword(String password){
        if (password.isEmpty()){
            throw new VerifyPasswordException("Password Cannot be Empty");
        }
        String passwordRegex = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[@$!%*#?&])[A-Za-z0-9@$!%*#?&]{8,}$";
        Pattern passwordPattern = Pattern.compile(passwordRegex);
        Matcher passwordMatcher = passwordPattern.matcher(password);

        if (!passwordMatcher.matches()){
            throw new VerifyPasswordException("Password dose not follow required format");
        }
    }

}