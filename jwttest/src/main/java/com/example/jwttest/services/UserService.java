package com.example.jwttest.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.jwttest.email.token.ConfirmationToken;
import com.example.jwttest.email.token.ConfirmationTokenRepository;
import com.example.jwttest.exceptionHandling.exceptions.UserNotFoundException;
import com.example.jwttest.models.User;
import com.example.jwttest.models.UserDto;
import com.example.jwttest.repo.UserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepo;
    private final ConfirmationTokenRepository tokenRepo;
    

    @PersistenceContext
    private EntityManager entityManager;

    public User saveUser(User user){
        return userRepo.save(user);
    }
    
    public Optional<User> getUser(String username){
        return userRepo.findByUsername(username);
    }

    public List<User> getAll(){
        return userRepo.findAll();
    }

    /* public List<User> getUsers(){
        return userRepo.findAll();
    } */
    public List<UserDto> getUsers() {
        List<User> users = userRepo.findAll();
        return users.stream()
                    .map(user -> new UserDto(user.getId(),user.getFirstname(),user.getLastname()
                    ,user.getUsername(), user.getEmail(), user.getRole(),user.isEnabled()))
                    .collect(Collectors.toList());
    }

   /*  public void enableUser(Long userId) {
        Optional<User> optionalUser = userRepo.findById(userId);
        if (optionalUser.isPresent()) {
            System.out.println("user: "+optionalUser);
            User user = optionalUser.get();
            user.setEnabled(false);
            userRepo.save(user);
        } else {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }

    } */
    public void enableUser(Long userId) {
        User user =userRepo.findById(userId).orElseThrow(()-> new UserNotFoundException("user with id: "+userId+"not found"));                          
        user.setEnabled(true);
        userRepo.save(user);
    }
    public void disableUser(Long userId) {
        User user =userRepo.findById(userId).orElseThrow(()-> new UserNotFoundException("user with id: "+userId+"not found"));                          
        user.setEnabled(false);
        userRepo.save(user);
    }





    public UserDto updateUser(Long userId, String role, boolean enabled) {
        User user = userRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setRole(role);
        user.setEnabled(enabled);
        User updatedUser= userRepo.save(user);
        return new UserDto(updatedUser.getId(),updatedUser.getFirstname(),
                           updatedUser.getLastname(),updatedUser.getUsername(),updatedUser.getEmail(),
                           updatedUser.getRole(),updatedUser.isEnabled());
       
    }

    /* public void deleteUser(String username){
        userRepo.deleteByUsername(username);

    } */
    /* @Transactional
    public void deleteUser(String username) {
        Query query = entityManager.createQuery("DELETE FROM User u WHERE u.username = :username");
        query.setParameter("username", username);
        int rowsAffected = query.executeUpdate();

        if (rowsAffected == 0) {
            // User does not exist
            System.out.println("User with username '" + username + "' does not exist.");
            throw new UserNotFoundException("User with username '" + username + "' does not exist.");
            // You can throw an exception here, or handle it as needed
        } else {
            // Deletion successful
            System.out.println("User with username '" + username + "' has been deleted.");
        }
    
    } */


    public void deleteUserId(Long id) {
        //userRepo.deleteById(id);
        Optional<User> userOptional = userRepo.findById(id);
        userOptional.ifPresent(user -> {
            // Find associated confirmation tokens
            List<ConfirmationToken> confirmationTokens = tokenRepo.findByUser(user);
            // Delete associated confirmation tokens
            confirmationTokens.forEach(tokenRepo::delete);
            // Delete the user
            userRepo.delete(user);
        }); // Add closing parenthesis here
    }
    
        /* public void deleteUser(String username) {
        try {
            entityManager.createQuery("DELETE FROM User u WHERE u.username = :username")
                         .setParameter("username", username)
                         .executeUpdate();
        } catch (EntityNotFoundException ex) {
            // Handle the case where the user does not exist
            // For example, log the error or return an appropriate response
            System.out.println("User with username '" + username + "' does not exist.");
            throw new UserNotFoundException("User with username '" + username + "' does not exist.");
            
            // Alternatively, you can rethrow the exception to propagate it to the caller
            // throw ex;
        }
    
    } */
    

}
