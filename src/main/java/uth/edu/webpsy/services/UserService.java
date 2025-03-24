package uth.edu.webpsy.services;

import uth.edu.webpsy.dtos.RegisterRequest;
import uth.edu.webpsy.models.Role;
import uth.edu.webpsy.models.User;
import uth.edu.webpsy.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public User finByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.orElseThrow(() -> new RuntimeException("user not found"));
    }
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
    public void saveUser(User user) {
        userRepository.save(user);
    }
//    public User findByEmail(String email) {
//        return userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
//    }
//    public User findByUsername(String username) {
//        Optional<User> user = userRepository.findByUsername(username);
//        return user.orElseThrow(() -> new RuntimeException("User not found"));
//    }
    public void registerUser(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại!");
        }
        if(userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email đã tồn tại!");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Mã hóa password
        user.setRole(Role.valueOf(request.getRole().toUpperCase()));

        userRepository.save(user);
    }
}
