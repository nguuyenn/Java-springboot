package uth.edu.webpsy.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uth.edu.webpsy.models.Admin;
import uth.edu.webpsy.models.Role;
import uth.edu.webpsy.models.User;
import uth.edu.webpsy.repositories.UserRepository;
import uth.edu.webpsy.repositories.AdminRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    public String registerAdmin(String username, String email, String password) {
        if (email == null || email.isEmpty()) {
            return "Email không được để trống!";
        }

        if (userRepository.findByEmail(email).isPresent()) {
            return "Email đã tồn tại!";
        }
        if (userRepository.findByUsername(username).isPresent()) {
            return "Tên đăng nhập đã tồn tại!";
        }

        // Tạo User mới với quyền ADMIN
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(password); // Cần mã hóa mật khẩu trước khi lưu
        newUser.setRole(Role.ADMIN);

        // Lưu User vào database trước
        User savedUser = userRepository.save(newUser);

        // Tạo Admin và liên kết với User
        Admin newAdmin = new Admin();
        newAdmin.setUser(savedUser); // Đảm bảo User không bị null
        adminRepository.save(newAdmin);

        return "Đăng ký Admin thành công!";
    }

}
