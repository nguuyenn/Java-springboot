package uth.edu.webpsy.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uth.edu.webpsy.models.User;
import java.util.Collections;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Lazy
    @Autowired
    private UserService userService;

    //Xác thực người dùng
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User không tồn tại!");
        }
        //Cấp quyền cho user
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(user.getRole().name()) // Lấy name() của enum
        );

        // Tạo đối tượng UserDetails để Spring Security xử lý
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())  // Đặt username là email
                .password(user.getPassword()) // Lưu password đã mã hóa
                .authorities(authorities) // Gán quyền truy cập
                .build();
    }

}
