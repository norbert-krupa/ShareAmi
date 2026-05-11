package com.shareami.auth;

import com.shareami.auth.dto.LoginRequest;
import com.shareami.auth.dto.LoginResponse;
import com.shareami.auth.exceptions.InvalidCredentialsException;
import com.shareami.user.User;
import com.shareami.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {

        UsernamePasswordAuthenticationToken credentialsToken = new UsernamePasswordAuthenticationToken(request.email(), request.password());

        try {
            authenticationManager.authenticate(credentialsToken);

        }
        catch (BadCredentialsException | UsernameNotFoundException ex) {
            throw new InvalidCredentialsException("Invalid email or password");
        }


        // TODO: Refactor UserDetailsServiceImpl to return a UserPrincipal wrapping the full User entity,
        // to avoid this duplicate DB lookup after authentication.
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalStateException("User authenticated but not found: " + request.email()));

        String token = jwtService.generateToken(user.getId(), user.getUsername());

        return new LoginResponse(token, user.getId(), user.getUsername(), user.getEmail());
    }
}
