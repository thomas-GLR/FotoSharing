package local.epul4a.fotosharing.repository;

import local.epul4a.fotosharing.entity.Partage;
import local.epul4a.fotosharing.entity.Photo;
import local.epul4a.fotosharing.entity.User;
import local.epul4a.fotosharing.enums.Visibility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PartageRepository extends JpaRepository<Partage, Long> {
    @Query("SELECT p from Photo p join Partage pa on pa.photo = p where pa.user = ?1 or p.visibility = ?2")
    List<Photo> findSharedPhotosWithUserId(User userId, Visibility visibility);
}
