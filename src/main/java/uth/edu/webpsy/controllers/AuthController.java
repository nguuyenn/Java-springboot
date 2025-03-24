package uth.edu.webpsy.controllers;

import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uth.edu.webpsy.models.Role;
import uth.edu.webpsy.models.User;
import uth.edu.webpsy.services.UserService;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String showLoginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Sai email hoặc mật khẩu!");
        }
        return "login"; // Điều hướng đến login.html
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

//    @PostMapping("/register")
//    public String registerUser(@RequestParam("username") String username,
//                               @RequestParam("email") String email,
//                               @RequestParam("password") String password,
//                               @RequestParam("role") Role role,
//                               Model model) {
//        if (userService.findByEmail(email) != null) {
//            model.addAttribute("error", "Email đã được sử dụng!");
//            return "register";
//        }
//        if(userService.findByUsername(username) != null) {
//            model.addAttribute(("error"),"Tên đăng nhập đã tồn tại!");
//            return "register";
//        }
//
//        String encodedPassword = passwordEncoder.encode(password);
//
//        // Tạo user mới
//        User newUser = new User();
//        newUser.setUsername(username);
//        newUser.setEmail(email);
//        newUser.setPassword(encodedPassword);
//        newUser.setRole(role);
//
//        // Lưu user vào database
//        userService.saveUser(newUser);
//
//        return "redirect:/auth/login?success";
//    }
}
