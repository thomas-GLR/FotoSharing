package local.epul4a.fotosharing.service.impl;

import local.epul4a.fotosharing.dto.AlbumDto;
import local.epul4a.fotosharing.dto.PhotoDto;
import local.epul4a.fotosharing.dto.UserPartageDto;
import local.epul4a.fotosharing.entity.*;
import local.epul4a.fotosharing.enums.PermissionLevel;
import local.epul4a.fotosharing.enums.Visibility;
import local.epul4a.fotosharing.repository.AlbumPartageRepository;
import local.epul4a.fotosharing.repository.AlbumRepository;
import local.epul4a.fotosharing.repository.PhotoRepository;
import local.epul4a.fotosharing.repository.UserRepository;
import local.epul4a.fotosharing.service.PhotoService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;

@Service
public class AlbumService {
    private final AlbumRepository albumRepository;
    private final PhotoRepository photoRepository;
    private final AlbumPartageRepository albumPartageRepository;
    private final UserRepository userRepository;

    public AlbumService(
            AlbumRepository albumRepository,
            PhotoRepository photoRepository,
            AlbumPartageRepository albumPartageRepository,
            UserRepository userRepository) {
        this.albumRepository = albumRepository;
        this.photoRepository = photoRepository;
        this.albumPartageRepository = albumPartageRepository;
        this.userRepository = userRepository;
    }

    public List<AlbumDto> getAllAlbum(User user) {
        return this.albumRepository.findAllAlbumForUser(user).stream()
                .map(album -> {
                    PermissionLevel permission = album.getAlbumPartages().stream()
                            .filter(partage -> Objects.equals(partage.getUser().getId(), user.getId()))
                            .map(AlbumPartage::getPermissionLevel)
                            .findFirst().orElse(PermissionLevel.VIEW);
                    return new AlbumDto(album, permission == PermissionLevel.EDIT);
                })
                .toList();
    }

    public AlbumDto getAlbum(Long id) {
        return new AlbumDto(this.albumRepository.findById(id).get());
    }

    public void create(String name, String description, User user, Long[] photosId) {
        List<Photo> photos = this.photoRepository.findAllById(Arrays.stream(photosId).toList());

        Album album = new Album();
        album.setName(name);
        album.setDescription(description);
        album.setCreatedAt(now());
        album.setUpdatedAt(now());
        album.setOwner(user);
        album.setVisibility(Visibility.PRIVATE);

        this.albumRepository.save(album);
        album.setPhotos(photos);
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
        this.albumRepository.saveAndFlush(album);
        this.albumRepository.delete(album);
    }

    public List<UserPartageDto> getUsersWithPermissions(Long id) {
        List<User> users = this.userRepository.findAll();
        Album album = this.albumRepository.findById(id).get();
        Map<User, PermissionLevel> partages = this.albumPartageRepository.findAllByAlbum(album).stream()
                .collect(Collectors.toMap(AlbumPartage::getUser, AlbumPartage::getPermissionLevel));
        return users.stream()
                .map(user -> {
                    PermissionLevel permissionLevel = partages.getOrDefault(user, null);
                    return new UserPartageDto(user, permissionLevel);
                })
                .toList();
    }

    public void handlePartage(Long albumId, Long userId, String permission) {
        User user = this.userRepository.findById(userId).get();
        Album album = this.albumRepository.findById(albumId).get();
        Optional<AlbumPartage> partage = this.albumPartageRepository.findByAlbumAndUser(album, user);
        PermissionLevel permissionLevel;
        switch (permission) {
            case "VIEW" -> permissionLevel = PermissionLevel.VIEW;
            case "EDIT" -> permissionLevel = PermissionLevel.EDIT;
            default -> permissionLevel = null;
        }

        AlbumPartage newPartage;

        if (partage.isPresent() && permissionLevel == null) {
            this.albumPartageRepository.delete(partage.get());
            return;
        }

        if (partage.isEmpty()) {
            newPartage = new AlbumPartage();
            newPartage.setUser(user);
            newPartage.setAlbum(album);
        } else {
            newPartage = partage.get();
        }

        newPartage.setPermissionLevel(permissionLevel);

        this.albumPartageRepository.save(newPartage);
    }
}
