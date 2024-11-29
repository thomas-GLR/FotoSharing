package local.epul4a.fotosharing.repository;
import local.epul4a.fotosharing.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}