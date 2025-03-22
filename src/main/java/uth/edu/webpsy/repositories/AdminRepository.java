package uth.edu.webpsy.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import uth.edu.webpsy.models.User;
import uth.edu.webpsy.models.Admin;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByUserId(Long userId);
}
