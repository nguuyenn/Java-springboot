package uth.edu.webpsy.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uth.edu.webpsy.models.User;
import uth.edu.webpsy.services.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String showAdminDashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            System.out.println("Chưa đăng nhập");
            return "redirect:/auth/web-login";
        }

        User user = (User) userService.findByEmail(authentication.getName());
        if (user == null) {
            System.out.println(" Không tìm thấy user.");
            return "redirect:/auth/web-login";
        }

        System.out.println("Đăng nhập thành công, user: " + user.getEmail());
        System.out.println("Role của user: " + user.getRole().name());

        if (!user.getRole().name().equals("ADMIN")) {
            System.out.println("User không phải ADMIN.");
            return "redirect:/auth/web-login";
        }

        model.addAttribute("user", user);
        model.addAttribute("users", userService.getAllUsers());
        return "admin_dashboard";
    }

}
