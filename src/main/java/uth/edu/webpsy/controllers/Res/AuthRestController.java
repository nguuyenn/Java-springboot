package uth.edu.webpsy.controllers.Res;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import uth.edu.webpsy.dtos.RegisterRequest;
import uth.edu.webpsy.dtos.RegisterResponse;
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
    //dùng cho postman để test
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        //Xác thực người dùng bằng authenticationManager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        // Đặt authentication vào SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Lấy user từ database
        User user = userService.findByEmail(request.getEmail());

        // Tạo JWT Token với Role
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

        // Trả về ResponseEntity chứa LoginResponse
        return ResponseEntity.ok(new LoginResponse(token));
    }
    //dùng cho postman
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if(userService.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(new RegisterResponse("Email đã tồn tại!", null, null, null));
        }
        User newUser = userService.registerUser(request);
        RegisterResponse response = new RegisterResponse("Tạo tài khoản thành công!",
                newUser.getEmail(),
                newUser.getUsername(),
                newUser.getRole()

        );
        return ResponseEntity.ok(response);
    }
    //dùng trên postman
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        // Xóa SecurityContext
        SecurityContextHolder.clearContext();
        // Kiểm tra token (nếu có)
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Có thể lưu danh sách token đã logout để không cho phép sử dụng lại
            String token = authHeader.substring(7);
            jwtUtil.blacklistToken(token);
        }
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

        return ResponseEntity.ok("Đăng xuất thành công!");
    }
}
