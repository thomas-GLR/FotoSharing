package local.epul4a.fotosharing.repository;

import local.epul4a.fotosharing.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlbumPartageRepository extends JpaRepository<AlbumPartage, Long> {
    List<AlbumPartage> findAllByAlbum(Album album);
    Optional<AlbumPartage> findByAlbumAndUser(Album album, User user);
}
