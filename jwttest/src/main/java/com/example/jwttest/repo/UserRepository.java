package com.example.jwttest.repo;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jwttest.models.User;



public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    void deleteByUsername(String username);
    void deleteById(Long id);

    
}
