package uth.edu.webpsy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uth.edu.webpsy.models.Role;
import uth.edu.webpsy.models.User;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByRole(Role role);
//    boolean existsByEmail(String email);
    Optional<User> findByUsername(String username);
}
