package uth.edu.webpsy.services;

import uth.edu.webpsy.dtos.RegisterRequest;
import uth.edu.webpsy.models.Role;
import uth.edu.webpsy.models.User;
import uth.edu.webpsy.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //Tìm user theo email
    public User findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.orElseThrow(() -> new RuntimeException("user không tồn tại!"));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    //Kiểm tra email đã tồn tại chưa
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    // Lưu user vào database
    public void saveUser(User user) {
        userRepository.save(user);
    }

    // Đăng ký user
    public User registerUser(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại!");
        }
        if(existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã được sử dụng!");
        }

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword())); // Mã hóa password
        // Nếu không có role, đặt mặc định là STUDENT
        if (request.getRole() == null) {
            newUser.setRole(Role.STUDENT);
        } else {
            newUser.setRole(request.getRole());
        }
        return userRepository.save(newUser);
    }

    //Lấy danh sách user
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public String login(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return "Email không tồn tại!";
        }
        User user = userOptional.get();
        if (!user.getPassword().equals(password)) {
            return "Sai mật khẩu!";
        }
        return user.getRole().toString();
    }
}
