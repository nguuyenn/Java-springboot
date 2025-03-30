package uth.edu.webpsy.controllers.Res;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uth.edu.webpsy.models.User;
import uth.edu.webpsy.services.UserService;

@RestController
@RequestMapping("/dashboard")
public class DashboardRestController {

    private final UserService userService;

    public DashboardRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/api")
    public ResponseEntity<?> getDashboardInfo() {
        // Lấy thông tin user từ SecurityContext
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            User user = userService.findByEmail(email);
            return ResponseEntity.ok("Welcome to the Dashboard, " + user.getUsername() + "!");
        }
        return ResponseEntity.status(403).body("Access Denied");
    }
}
