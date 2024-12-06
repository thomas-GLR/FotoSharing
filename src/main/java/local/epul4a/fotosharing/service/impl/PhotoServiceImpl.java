package local.epul4a.fotosharing.service.impl;

import local.epul4a.fotosharing.dto.PhotoDto;
import local.epul4a.fotosharing.entity.Photo;
import local.epul4a.fotosharing.entity.User;
import local.epul4a.fotosharing.enums.Visibility;
import local.epul4a.fotosharing.repository.PhotoRepository;
import local.epul4a.fotosharing.service.PhotoService;
import local.epul4a.fotosharing.service.StorageService;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.time.LocalDateTime.now;

@Service
public class PhotoServiceImpl implements PhotoService {
    private final PhotoRepository photoRepository;
    private final StorageService storageService;
    public PhotoServiceImpl(PhotoRepository photoRepository, StorageService storageService) {
        this.photoRepository = photoRepository;
        this.storageService = storageService;
    }
    public List<PhotoDto> getUserPhotos(User currentUser) {
        return this.photoRepository.findByOwner(currentUser).stream().map(PhotoDto::new).toList();
    }
    public void editer(Long id, String title, String description, Visibility visibility) {
        Photo photo = this.photoRepository.findById(id).get();
        photo.setTitle(title);
        photo.setDescription(description);
        photo.setVisibility(visibility);
        photo.setUpdatedAt(now());
        this.photoRepository.save(photo);
    }
    public void delete(Long id) {
        Photo photo = this.photoRepository.findById(id).get();
        boolean deleteResult = this.storageService.deleteFile(photo.getUrl());
        if (deleteResult) {
            this.photoRepository.deleteById(id);
        }
    }
    public List<PhotoDto> getPhotoForValidation() {
        return this.photoRepository.findByVisibilityAndValidate(Visibility.PUBLIC, false).stream()
                .map(PhotoDto::new)
                .toList();
    }

    public void validatePhoto(Long id, boolean validate) {
        Photo photo = this.photoRepository.findById(id).get();
        photo.setValidate(validate);
        if (!validate) {
            photo.setVisibility(Visibility.PRIVATE);
        }
        this.photoRepository.save(photo);
    }
    public List<PhotoDto> getAllPhotos(User currentUser) {
        return this.photoRepository.findAllPhotos(currentUser).stream()
                .map(PhotoDto::new)
                .toList();
    }
}
