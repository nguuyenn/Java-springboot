package uth.edu.webpsy.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uth.edu.webpsy.models.User;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @GetMapping
    public String showUserDashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/auth/web-login";
        }
        model.addAttribute("user", user);
        return "dashboard"; // Trả về trang dashboard.html
    }
}
