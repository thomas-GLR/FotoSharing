package local.epul4a.fotosharing.service;

import local.epul4a.fotosharing.dto.PhotoDto;
import local.epul4a.fotosharing.entity.Photo;
import local.epul4a.fotosharing.entity.User;
import local.epul4a.fotosharing.enums.Visibility;

import java.util.List;

public interface PhotoService {
    public List<PhotoDto> getUserPhotos(User currentUser);
    public List<PhotoDto> getAllPhotos(User currentUser);
    public void editer(Long id, String title, String description, Visibility visibility);
    public void delete(Long id);
    public List<PhotoDto> getPhotoForValidation();
    public void validatePhoto(Long id, boolean validate);
    public Photo getPhoto(Long id);
}
