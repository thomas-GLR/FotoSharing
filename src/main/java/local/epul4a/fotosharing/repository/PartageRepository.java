package local.epul4a.fotosharing.repository;

import local.epul4a.fotosharing.entity.Partage;
import local.epul4a.fotosharing.entity.Photo;
import local.epul4a.fotosharing.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PartageRepository extends JpaRepository<Partage, Long> {
    @Query("""
        SELECT p
        from Photo p
        left join FETCH p.partages pa
        where pa.user = ?1 or (p.visibility = 1 and p.validate = true and p.owner != ?1)
""")
    List<Photo> findSharedPhotosWithUserId(User userId);
    List<Partage> findAllByPhoto(Photo photo);
    Optional<Partage> findByPhotoAndUser(Photo photo, User user);
}
