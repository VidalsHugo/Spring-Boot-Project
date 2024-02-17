package com.devnews4.demo.controllers;

import com.devnews4.demo.domain.user.*;
import com.devnews4.demo.repositories.UserRepository;
import com.devnews4.demo.infra.security.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("auth") // -- EX: localhost:8080/auth/login
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) {

        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        System.out.println(usernamePassword);
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDTO(token));

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

    @GetMapping("/logout")
    public ResponseEntity logout(HttpServletRequest request){
        
        String authToken = tokenService.extractTokenFromHeader(request.getHeader("Authorization"));
        System.out.println(authToken);
        if (authToken != null) {
            tokenService.blacklist.add(authToken); // Adiciona o token à lista negra
            System.out.println("Token revogado com sucesso: " + authToken);
        }

        System.out.println("DESLOGADO COM SUCESSO !");

        //TODO retornar para a tela de login
        return ResponseEntity.ok().build();
    }
}
