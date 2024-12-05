package local.epul4a.fotosharing.repository;

import local.epul4a.fotosharing.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
}
