package local.epul4a.fotosharing.repository;

import local.epul4a.fotosharing.entity.Photo;
import local.epul4a.fotosharing.entity.User;
import local.epul4a.fotosharing.enums.Visibility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findByOwner(User owner);
    List<Photo> findByVisibilityAndValidate(Visibility visibility, boolean validate);
    @Query("select p from Photo p left join Partage pa on pa.photo = p where p.owner = ?1 or pa.user = ?1 or p.visibility = 1")
    List<Photo> findAllPhotos(User currentUser);
}