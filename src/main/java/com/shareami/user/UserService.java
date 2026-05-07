package com.shareami.user;

import com.shareami.user.dto.RegisterUserRequest;
import com.shareami.user.dto.UserResponse;
import com.shareami.user.exceptions.EmailAlreadyTakenException;
import com.shareami.user.exceptions.UserNotFoundException;
import com.shareami.user.exceptions.UsernameAlreadyTakenException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse register(RegisterUserRequest request) {

        if (userRepository.existsByUsername(request.username())) {
            throw new UsernameAlreadyTakenException("Username is taken");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyTakenException("User with this email already exists");
        }

        String passwordHash = passwordEncoder.encode(request.password());

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .passwordHash(passwordHash)
                .build();

        User savedUser = userRepository.save(user);

        return UserResponse.from(savedUser);
    }

    public UserResponse getUser(UUID id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User does not exist"));

        return UserResponse.from(user);
    }
}
