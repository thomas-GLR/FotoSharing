package local.epul4a.fotosharing.repository;

import local.epul4a.fotosharing.entity.Album;
import local.epul4a.fotosharing.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AlbumRepository extends JpaRepository<Album, Long> {
    @Query("""
        select a
        from Album a
        left join FETCH a.albumPartages ap
        where a.visibility = 1 or a.owner = ?1 or ap.user = ?1
""")
    List<Album> findAllAlbumForUser(User user);
}
