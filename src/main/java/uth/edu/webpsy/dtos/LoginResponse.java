package uth.edu.webpsy.dtos;

public class LoginResponse {
    private String token;

    public LoginResponse(String token) {
        this.token = token; }

    public String getToken() {
        return token; }
}
