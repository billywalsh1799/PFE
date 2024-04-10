package com.example.jwttest.auth;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.example.jwttest.models.UserDto;
import com.example.jwttest.services.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@CrossOrigin(origins ="*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
    private final UserService userService;

    @GetMapping
    public String getMethodName() {
        return "only for admin role";
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getUsers() {
        List<UserDto> userDtos = userService.getUsers();
        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    @PatchMapping("/user/userid/{userId}/disable")
    public ResponseEntity<String> disableUser(@PathVariable Long userId) {
        userService.disableUser(userId);
        return ResponseEntity.ok("User disabled successfully");
       
    }
    @PatchMapping("/user/userid/{userId}/enable")
    public ResponseEntity<String> enableUser(@PathVariable Long userId) {
        userService.enableUser(userId);
        return ResponseEntity.ok("User enabled successfully");
       
    }

    @PutMapping("/user/userid/{userId}/update")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long userId,@RequestBody UpdateUserRequest request){
        UserDto updatedUser = userService.updateUser(userId, request.getRole(),request.isEnabled());
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
                                
    }

    @DeleteMapping("/user/userid/{userId}")
    public ResponseEntity<String> deleteUserId(@PathVariable Long userId){
        userService.deleteUserId(userId);
        return new ResponseEntity<>("user: "+userId+" successfully deleted",HttpStatus.OK);
    }

    


    
    
}
