package local.epul4a.fotosharing.repository;

import local.epul4a.fotosharing.entity.Partage;
import local.epul4a.fotosharing.entity.Photo;
import local.epul4a.fotosharing.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PartageRepository extends JpaRepository<Partage, Long> {
    @Query("SELECT p from Photo p left join Partage pa on pa.photo = p where pa.user = ?1 or (p.visibility = 1 and p.validate = true and p.owner != ?1)")
    List<Photo> findSharedPhotosWithUserId(User userId);
}
