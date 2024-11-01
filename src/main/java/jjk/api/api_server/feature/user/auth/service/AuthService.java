package jjk.api.api_server.feature.user.auth.service;

import jjk.api.api_server.common.util.JwtUtil;
import jjk.api.api_server.feature.user.auth.dto.SignInDto;
import jjk.api.api_server.feature.user.auth.dto.SignUpDto;
import jjk.api.api_server.feature.user.auth.model.CustomUserDetails;
import jjk.api.api_server.feature.user.user.entity.Role;
import jjk.api.api_server.feature.user.user.entity.User;
import jjk.api.api_server.feature.user.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Set;

@Slf4j
@Service
public class AuthService {
    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;
    private final ModelMapper modelMapper;

    public AuthService(AuthenticationManager authenticationManager, JwtUtil jwtUtil, CustomUserDetailsService customUserDetailsService, ModelMapper modelMapper,
                       UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<String> signIn(SignInDto signInDto) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInDto.getLoginId(), signInDto.getPassword()));
        } catch (AuthenticationException e) {
            log.error("Invalid username or password", e);
            return ResponseEntity.status(401).body("Invalid username or password");
        }
        final CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername(signInDto.getLoginId());
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(jwt);
    }

    @Transactional
    public ResponseEntity<String> signUp(SignUpDto signUpDto) {
        try {
            User user = modelMapper.map(signUpDto, User.class);
            userRepository.save(user);
        } catch (Exception e) {
            log.error("Failed to sign up", e);
            return ResponseEntity.status(500).body("Failed to sign up");
        }
        return ResponseEntity.ok("Signed up successfully");
    }
}
