package uth.edu.webpsy.config;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import uth.edu.webpsy.jwt.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import uth.edu.webpsy.jwt.JwtUtil;
import uth.edu.webpsy.services.JwtBlacklistService;
import uth.edu.webpsy.services.UserService;

@Configuration
@EnableWebSecurity //bật tính năng bảo mật
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    public SecurityConfig(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    //kiểm tra JWT token trong mỗi request để xác thực người dùng
    @Bean
    public JwtFilter jwtFilter(UserService userService, JwtUtil jwtUtil, UserDetailsService userDetailsService, JwtBlacklistService jwtBlacklistService) {
        return new JwtFilter(jwtUtil, userDetailsService, userService, jwtBlacklistService);
    }

    // mã hóa mật khẩu
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //Xử lý đăng nhập
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // tắt csrf vì api dùng jwt
                .authorizeHttpRequests(auth -> auth // phân quyền
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(("/css/**"), "/js/**", "/image/**").permitAll()
                        .requestMatchers("/admin/**").hasAuthority("ADMIN") // Chỉ admin mới vào được
                        .requestMatchers("/dashboard/**").hasAnyAuthority("STUDENT", "PARENT", "PSYCHOLOGIST")
                        .anyRequest().authenticated() // yêu cầu đăng nhập
                )
                .sessionManagement(session -> session // Bật session cho web
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                //kiểm tra JWT trước khi Spring Security xử lý request
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
