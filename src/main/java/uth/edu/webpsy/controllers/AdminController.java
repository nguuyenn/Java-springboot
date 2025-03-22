package uth.edu.webpsy.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uth.edu.webpsy.models.User;
import uth.edu.webpsy.services.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/register")
    public String registerAdmin(@RequestBody User user) {
        return adminService.registerAdmin(user.getUsername(), user.getEmail(), user.getPassword());
    }
}
