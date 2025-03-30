package uth.edu.webpsy.dtos;

import uth.edu.webpsy.models.Role;

public class RegisterResponse {
    private String message;
    private String email;
    private String username;
    private Role role;

    public RegisterResponse(String message, String email, String username, Role role) {
        this.message = message;
        this.email = email;
        this.username = username;
        this.role = role;
    }

    public String getMessage() { return message; }
    public String getEmail() { return email; }
    public String getUsername() { return username; }
    public Role getRole() { return role; }
}
