package local.epul4a.fotosharing.service.impl;

import local.epul4a.fotosharing.dto.AlbumDto;
import local.epul4a.fotosharing.entity.Album;
import local.epul4a.fotosharing.entity.Photo;
import local.epul4a.fotosharing.entity.User;
import local.epul4a.fotosharing.enums.Visibility;
import local.epul4a.fotosharing.repository.AlbumRepository;
import local.epul4a.fotosharing.repository.PhotoRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static java.time.LocalDateTime.now;

@Service
public class AlbumService {
    private final AlbumRepository albumRepository;
    private final PhotoRepository photoRepository;

    public AlbumService(AlbumRepository albumRepository, PhotoRepository photoRepository) {
        this.albumRepository = albumRepository;
        this.photoRepository = photoRepository;
    }

    public List<AlbumDto> getAllAlbum(User user) {
        return this.albumRepository.findAllAlbumForUser(user).stream()
                .map(AlbumDto::new)
                .toList();
    }

    public AlbumDto getAlbum(Long id) {
        return new AlbumDto(this.albumRepository.findById(id).get());
    }

    public void create(String name, String description, User user) {
        Album album = new Album();
        album.setName(name);
        album.setDescription(description);
        album.setCreatedAt(now());
        album.setUpdatedAt(now());
        album.setOwner(user);
        album.setVisibility(Visibility.PRIVATE);

        this.albumRepository.save(album);
    }

    public void edit(Long id, String name, String description, Visibility visibility, Long[] photosId) {
        List<Photo> photos = this.photoRepository.findAllById(Arrays.stream(photosId).toList());
        Album album = this.albumRepository.findById(id).get();
        album.setVisibility(visibility);
        album.setName(name);
        album.setDescription(description);
        album.setPhotos(photos);
        this.albumRepository.save(album);
    }

    public void delete(Long id) {
        Album album = this.albumRepository.findById(id).get();
        if (!album.getPhotos().isEmpty()) {
            album.clearPhotos();
        }
        this.albumRepository.save(album);
        this.albumRepository.delete(album);
    }
}
