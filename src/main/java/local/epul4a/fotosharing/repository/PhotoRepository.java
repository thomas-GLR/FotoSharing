package local.epul4a.fotosharing.repository;

import local.epul4a.fotosharing.entity.Photo;
import local.epul4a.fotosharing.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findByOwner(User owner);
}
