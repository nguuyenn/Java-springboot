package uth.edu.webpsy.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @GetMapping
    public String showDashboard() {
        return "dashboard"; // Dùng chung cho Student, Parent, Psychologist
    }
    @GetMapping("/admin")
    public String showAdminDashboard() {
        return "admin_dashboard"; // Trả về trang dành riêng cho Admin
    }

}
