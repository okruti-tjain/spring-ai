package com.baeldung.springai.memory.springsecurity;

import com.baeldung.springai.memory.user.LoginRequest;
import com.baeldung.springai.memory.user.User;
import com.baeldung.springai.memory.user.UserRegistrationRequest;
import com.baeldung.springai.memory.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtils jwtUtil;
    private final UserService userService;

    public AuthController(JwtUtils jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        return userService.authenticate(request.getEmail(),request.getPassword())
                .map(user -> {
                    String token = jwtUtil.generateJwtToken(user.getEmail());
                    return ResponseEntity.ok(token);
                })
                .orElse(ResponseEntity.status(401).body("Invalid credentials"));
    }
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody UserRegistrationRequest request) {
        User user = userService.registerUser(request);
        return ResponseEntity.ok(user);
    }
}
