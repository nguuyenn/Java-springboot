package uth.edu.webpsy.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uth.edu.webpsy.models.User;
import uth.edu.webpsy.repositories.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Tìm User theo ID
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    // Tìm User theo email
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Lưu User vào database
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Kiểm tra xem email đã tồn tại chưa
    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
