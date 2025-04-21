package uth.edu.webpsy.controllers.Res;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import uth.edu.webpsy.dtos.RegisterRequest;
import uth.edu.webpsy.dtos.RegisterResponse;
import uth.edu.webpsy.jwt.JwtUtil;
import uth.edu.webpsy.models.User;
import uth.edu.webpsy.services.JwtBlacklistService;
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
    private final JwtBlacklistService jwtBlacklistService;

    public AuthRestController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService, JwtBlacklistService jwtBlacklistService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.jwtBlacklistService = jwtBlacklistService;
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
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            jwtBlacklistService.blacklistToken(token);
        }
        return ResponseEntity.ok("Đăng xuất thành công");
    }
}
