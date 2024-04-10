package com.example.jwttest.auth;




import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.jwttest.email.EmailService;
import com.example.jwttest.email.token.ConfirmationToken;
import com.example.jwttest.email.token.ConfirmationTokenRepository;
import com.example.jwttest.email.token.ConfirmationTokenService;
import com.example.jwttest.exceptionHandling.exceptions.TokenExpiredException;
import com.example.jwttest.exceptionHandling.exceptions.UsedEmailException;
import com.example.jwttest.exceptionHandling.exceptions.UserAlreadyExistsException;
import com.example.jwttest.exceptionHandling.exceptions.UserNotFoundException;
import com.example.jwttest.models.User;
import com.example.jwttest.repo.UserRepository;
import com.example.jwttest.services.JwtService;

import io.jsonwebtoken.ExpiredJwtException;

import org.springframework.security.core.Authentication;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepo;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final ConfirmationTokenService confirmationTokenService;


    public void register(RegisterRequest request){

        //handle user already exists in db exception

        User checkUser=userRepo.findByUsername(request.getUsername()).orElse(null);
        
        if(checkUser!=null){
            throw new UserAlreadyExistsException("User already exists");
        }

        User checkEmail=userRepo.findByEmail(request.getEmail()).orElse(null);

        if(checkEmail!=null){
            throw new UsedEmailException("Email already in use");

        }

        String encodedPassword=passwordEncoder.encode(request.getPassword());
        User user=User.builder().username(request.getUsername()).firstname(request.getFirstname())
        .lastname(request.getLastname()).email(request.getEmail()).enabled(true)
        .password(encodedPassword).role("ROLE_USER").confirmed(false).build();
        userRepo.save(user);

        //send the confirmation mail
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken=ConfirmationToken.builder().token(token).createdAt(LocalDateTime.now()).
                                    expiresAt(LocalDateTime.now().plusMinutes(15)).user(user).build();
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        String link = "http://localhost:8080/api/auth/confirm?token=" + token;
        emailService.send(request.getEmail(), link);

        // Create extra claims with user role
        //Map<String, Object> extraClaims = new HashMap<>();
        //extraClaims.put("role", user.getRole());

        //Generate user tokens
        //String jwtToken=jwtService.generateToken(extraClaims,user);
        //String refreshToken=jwtService.generateRefreshToken(extraClaims,user);
        //return AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).build();

    }

    public AuthenticationResponse authenticate(AuthenticationRquest request){

        Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
       
        // Cast Authentication to User
        User user = (User) authentication.getPrincipal();

        // Authentication successful else globalhandler will send authexception
        // Proceed with further actions


        //Optional<User> userOptional = userRepo.findByUsername(request.getUsername());
        //User user = userOptional.orElseThrow(() -> new UserNotFoundException("User not found"));

        // Create extra claims with user role
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", user.getRole());

        //Generate user tokens
        String jwtToken=jwtService.generateToken(extraClaims,user);
        String refreshToken=jwtService.generateRefreshToken(extraClaims,user);
        return AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).build();
    }

    public AuthenticationResponse refreshToken(RefreshRequest request)  {
        
        final String refreshToken;
        final String username;

        refreshToken=request.getRefreshToken();

        try {
            username=jwtService.extractUsername(refreshToken); 
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException("Refresh token expired");
        }

        //UserDetails userDetails=this.userRepo.findByUsername(username).orElseThrow();
        Optional<User> userOptional = userRepo.findByUsername(username);
        //invalid token erro
        User user = userOptional.orElseThrow(() -> new UserNotFoundException("User not found"));

        // Create extra claims with user role
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", user.getRole());
        

        if (jwtService.isTokenValid(refreshToken, user)){
            String accessToken=jwtService.generateToken(extraClaims,user);
            AuthenticationResponse authResponse=AuthenticationResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
            return authResponse;
         
        }

        else{
            throw new RuntimeException("refresh token is expired please make a new login");
        }

            
    
    }

    public void confirmUser(String token){
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new RuntimeException("token not found"));
        if (confirmationToken.isUsed()) {
            throw new RuntimeException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("token expired");
        }

        //update user state to confirmed
        Long userId=confirmationToken.getUser().getId();
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setConfirmed(true);
        userRepo.save(user);

        //generate tokens

        System.out.println("confirmation token: "+token);
        // Create extra claims with user role
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", user.getRole());

        //Generate user tokens
        String jwtToken=jwtService.generateToken(extraClaims,user);
        String refreshToken=jwtService.generateRefreshToken(extraClaims,user);
        //return AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).build();



    }
    
}
