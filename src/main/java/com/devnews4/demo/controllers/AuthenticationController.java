package com.devnews4.demo.controllers;

import com.devnews4.demo.domain.user.AuthenticationDTO;
import com.devnews4.demo.domain.user.RegisterDTO;
import com.devnews4.demo.domain.user.User;
import com.devnews4.demo.domain.user.UserRole;
import com.devnews4.demo.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("auth") // -- EX: localhost:8080/auth/login
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) {

        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        return ResponseEntity.ok().build();

    }
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO data){

        if(this.userRepository.findByLogin(data.login()) != null)
            return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        var genericRole = UserRole.USER; // Qualquer usuario criado sera USER por padrao
        User newUser = new User(data.login(), encryptedPassword, genericRole);

        this.userRepository.save(newUser);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/users")
    public List<User> users(){
        return this.userRepository.findAll();
    }
}
