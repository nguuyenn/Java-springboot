package uth.edu.webpsy.controllers;

import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uth.edu.webpsy.dtos.LoginRequest;
import uth.edu.webpsy.dtos.RegisterRequest;
import uth.edu.webpsy.models.User;
import uth.edu.webpsy.repositories.UserRepository;
import uth.edu.webpsy.services.UserService;

@Controller // MVC Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    // Hiển thị trang đăng nhập
    @GetMapping("/web-login")
    public String showLoginPage() {
        return "login";
    }

    // Xử lý đăng nhập
    @PostMapping("/web-login")
    public String login(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        String result = userService.login(user.getEmail(), user.getPassword());
        if (result.equals("Email không tồn tại!") || result.equals("Sai mật khẩu!")) {
            redirectAttributes.addFlashAttribute("error", result);
            return "redirect:/auth/web-login";
        }
        // Chuyển hướng theo role
        if (result.equals("ADMIN")) {
            return "redirect:/admin/dashboard"; // Admin vào trang quản lý riêng
        } else {
            return "redirect:/dashboard"; // User bình thường vào trang chung
        }
    }


    // Hiển thị trang đăng ký
    @GetMapping("/web-register")
    public String showRegisterPage() {
        return "register";
    }

    // Xử lý đăng ký
    @PostMapping("/web-register")
    public String processRegister(@ModelAttribute RegisterRequest request, Model model) {
        if (userService.existsByEmail(request.getEmail())) {
            model.addAttribute("error", "Email đã tồn tại!");
            return "register";
        }
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            model.addAttribute("error", "Tên đăng nhập đã tồn tại!");
            return "register";
        }
        User newUser = userService.registerUser(request);
        model.addAttribute("success", "Đăng ký thành công! Hãy đăng nhập.");
        return "register";
    }
}
