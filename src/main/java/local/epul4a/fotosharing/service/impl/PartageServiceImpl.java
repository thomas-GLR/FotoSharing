package local.epul4a.fotosharing.service.impl;

import local.epul4a.fotosharing.dto.PhotoDto;
import local.epul4a.fotosharing.dto.UserPartageDto;
import local.epul4a.fotosharing.entity.Partage;
import local.epul4a.fotosharing.entity.Photo;
import local.epul4a.fotosharing.entity.User;
import local.epul4a.fotosharing.enums.PermissionLevel;
import local.epul4a.fotosharing.enums.Visibility;
import local.epul4a.fotosharing.repository.PartageRepository;
import local.epul4a.fotosharing.repository.PhotoRepository;
import local.epul4a.fotosharing.repository.UserRepository;
import local.epul4a.fotosharing.service.PartageService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;

@Service
public class PartageServiceImpl implements PartageService {
    private final PartageRepository partageRepository;
    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;

    public PartageServiceImpl(PartageRepository partageRepository, UserRepository userRepository, PhotoRepository photoRepository) {
        this.partageRepository = partageRepository;
        this.userRepository = userRepository;
        this.photoRepository = photoRepository;
    }
    public List<PhotoDto> getAllSharedAndPublicPhoto(User currentUser) {
       return partageRepository.findSharedPhotosWithUserId(currentUser).stream()
               .map(photo -> {
                   PermissionLevel permission = photo.getPartages().stream()
                           .filter(partage -> Objects.equals(partage.userId(), currentUser.getId()))
                           .map(Partage::getPermissionLevel)
                           .findFirst().orElse(PermissionLevel.VIEW);
                   return new PhotoDto(permission == PermissionLevel.EDIT, photo);
               })
               .toList();
    }

    public List<UserPartageDto> getUsersWithPermissions(Photo photo) {
        List<User> users = this.userRepository.findAll();
        Map<User, PermissionLevel> partages = this.partageRepository.findAllByPhoto(photo).stream()
                .collect(Collectors.toMap(Partage::getUser, Partage::getPermissionLevel));
        return users.stream()
                .map(user -> {
                    PermissionLevel permissionLevel = partages.getOrDefault(user, null);
                    return new UserPartageDto(user, permissionLevel);
                })
                .toList();
    }

    public void handlePartage(Long photoId, Long userId, String permission) {
        User user = this.userRepository.findById(userId).get();
        Photo photo = this.photoRepository.findById(photoId).get();
        Optional<Partage> partage = this.partageRepository.findByPhotoAndUser(photo, user);
        PermissionLevel permissionLevel;
        switch (permission) {
            case "VIEW" -> permissionLevel = PermissionLevel.VIEW;
            case "EDIT" -> permissionLevel = PermissionLevel.EDIT;
            default -> permissionLevel = null;
        }

        Partage newPartage;

        if (partage.isPresent() && permissionLevel == null) {
            this.partageRepository.delete(partage.get());
            return;
        }

        if (partage.isEmpty()) {
            newPartage = new Partage();
            newPartage.setUser(user);
            newPartage.setPhoto(photo);
            newPartage.setCreatedAt(now());
        } else {
            newPartage = partage.get();
        }

        newPartage.setPermissionLevel(permissionLevel);

        this.partageRepository.save(newPartage);
    }
}
