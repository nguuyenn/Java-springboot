package uth.edu.webpsy.controllers;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uth.edu.webpsy.models.User;
import uth.edu.webpsy.services.AuthService;

import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // Hiển thị trang đăng ký
    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    // Xử lý đăng ký
    @PostMapping("/register")
    public String register(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        String result = authService.registerUser(user.getUsername(),user.getEmail(), user.getPassword(), user.getRole());
        if (result.equals("Email đã tồn tại!")) {
            redirectAttributes.addFlashAttribute("error", "Email đã tồn tại!");
            return "redirect:/auth/register";
        }
        if (result.equals("Tên đăng nhập đã tồn tại!")) {
            redirectAttributes.addFlashAttribute("error", "Tên đăng nhập đã tồn tại!");
            return "redirect:/auth/register";
        }
        redirectAttributes.addFlashAttribute("success", "Đăng ký thành công!");
        return "redirect:/auth/register";
    }

    // Hiển thị trang đăng nhập
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    // Xử lý đăng nhập
    @PostMapping("/login")
    public String login(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        String result = authService.login(user.getEmail(), user.getPassword());
        if (result.equals("Email không tồn tại!") || result.equals("Sai mật khẩu!")) {
            redirectAttributes.addFlashAttribute("error", result);
            return "redirect:/auth/login";
        }
        // Chuyển hướng theo role
        if (result.equals("ADMIN")) {
            return "redirect:/dashboard/admin"; // Admin vào trang quản lý riêng
        } else {
            return "redirect:/dashboard"; // User bình thường vào trang chung
        }
    }

    //logout
    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.logout(); // Xóa session đăng nhập
        } catch (ServletException e) {
            e.printStackTrace();
        }
        return "redirect:/auth/login"; // Quay lại trang đăng nhập
    }

}
