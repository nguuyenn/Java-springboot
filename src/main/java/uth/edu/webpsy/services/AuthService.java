package uth.edu.webpsy.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uth.edu.webpsy.models.*;
import uth.edu.webpsy.repositories.*;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    // Đăng ký người dùng mới
    public String registerUser(String username, String email, String password, Role role) {
        // Kiểm tra email đã tồn tại chưa
        if (userRepository.findByEmail(email).isPresent()) {
            return "Email đã tồn tại!";
        }
        if (userRepository.findByUsername(username).isPresent()) {
            return "Tên đăng nhập đã tồn tại!";
        }

        // Tạo user mới
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setRole(role);
        userRepository.save(newUser);
        // Nếu user là ADMIN, tạo thêm bản ghi trong bảng Admin
        if (role == Role.ADMIN) {
            Admin admin = new Admin();
            admin.setUser(newUser);
            adminRepository.save(admin);
        }


        return "Đăng ký thành công!";
    }

    // Xác thực đăng nhập
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

