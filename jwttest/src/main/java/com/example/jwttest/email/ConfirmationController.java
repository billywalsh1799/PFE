package com.example.jwttest.email;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.jwttest.auth.AuthenticationService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins ="*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/email")
public class ConfirmationController {
    private final EmailService emailService;
    private final AuthenticationService authenticationService;
    @PostMapping("/send-email")
    public ResponseEntity<?> sendEmail(@RequestBody EmailRequest emailRequest) {
        try {
            // Create a SimpleMailMessage object
          
            emailService.send(emailRequest.getTo(), emailRequest.getMail());

            return ResponseEntity.ok("Email sent successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to send email: " + e.getMessage());
        }
    }

    @GetMapping("confirm")
    public ResponseEntity<String> confirm(@RequestParam("token") String token) {
        authenticationService.confirmUser(token);
        return ResponseEntity.ok("User confirmed successfully");
    }

    
    
}
