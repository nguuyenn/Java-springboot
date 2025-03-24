package uth.edu.webpsy.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import uth.edu.webpsy.dtos.RegisterRequest;
import uth.edu.webpsy.jwt.JwtUtil;
import uth.edu.webpsy.models.User;
import uth.edu.webpsy.services.UserService;
import uth.edu.webpsy.dtos.LoginRequest;
import uth.edu.webpsy.dtos.LoginResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthRestController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public AuthRestController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Lấy user từ database
        User user = userService.finByEmail(request.getEmail());

        // Tạo JWT Token với Role
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

        return new LoginResponse(token);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        userService.registerUser(request);
        return ResponseEntity.ok("Tạo tài khoản thành công!");
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        // Xóa SecurityContext
        SecurityContextHolder.clearContext();

        // Xóa session
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Xóa cookie (nếu có)
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok("Logged out successfully");
    }

}
