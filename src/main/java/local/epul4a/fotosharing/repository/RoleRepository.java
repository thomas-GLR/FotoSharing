package local.epul4a.fotosharing.repository;
import local.epul4a.fotosharing.entity.Role;
import local.epul4a.fotosharing.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(RoleType name);
}