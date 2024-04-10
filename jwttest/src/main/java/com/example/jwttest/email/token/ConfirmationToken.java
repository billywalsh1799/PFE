package com.example.jwttest.email.token;

import java.time.LocalDateTime;

import com.example.jwttest.models.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmationToken {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    @Column(nullable = false, columnDefinition = "boolean default true") // Add default value
    private boolean used;

    //@ManyToOne
    @ManyToOne(cascade = CascadeType.ALL) // Add cascade attribute here
    @JoinColumn(
            nullable = false,
            name = "user_id"
    )

    private User user;

    
}
