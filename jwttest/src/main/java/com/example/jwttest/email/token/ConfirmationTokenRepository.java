package com.example.jwttest.email.token;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jwttest.models.User;

public interface ConfirmationTokenRepository  extends JpaRepository<ConfirmationToken, Long> {

    Optional<ConfirmationToken> findByToken(String token);
    List<ConfirmationToken> findByUser(User user);
    
}
