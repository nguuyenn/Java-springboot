package uth.edu.webpsy.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uth.edu.webpsy.models.User;
import uth.edu.webpsy.services.JwtBlacklistService;
import uth.edu.webpsy.services.UserService;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final JwtBlacklistService jwtBlacklistService;

    public JwtFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService, UserService userService, JwtBlacklistService jwtBlacklistService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.jwtBlacklistService = jwtBlacklistService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Cắt bỏ "Bearer " để lấy JWT

            //Kiểm tra nếu token đã bị blacklist
            if (jwtBlacklistService.isTokenBlacklisted(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token đã bị chặn, vui lòng đăng nhập lại!");
                return; // Dừng xử lý request tiếp theo
            }
            String email = jwtUtil.extractUsername(token); // Trích xuất email từ JWT.
            HttpSession session = request.getSession();

            //Kiểm tra user có hợp lệ không
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = userService.findByEmail(email); // Dùng userService để lấy User

                //Kiểm tra JWT hợp lệ
                if (user != null && jwtUtil.validateToken(token, user.getEmail())) {
                    //UserDetails cho Spring Security
                    UserDetails userDetails = org.springframework.security.core.userdetails.User
                            .withUsername(user.getEmail())
                            .password(user.getPassword())
                            .authorities(new SimpleGrantedAuthority(user.getRole().name())) //Cấp quyền
                            .build();

                    //Cấp quyền cho user trong SecurityContext
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    session.setAttribute("loggedInUser", user);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
