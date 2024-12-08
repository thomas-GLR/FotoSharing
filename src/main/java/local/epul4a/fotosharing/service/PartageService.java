package local.epul4a.fotosharing.service;

import local.epul4a.fotosharing.dto.PhotoDto;
import local.epul4a.fotosharing.dto.UserPartageDto;
import local.epul4a.fotosharing.entity.Photo;
import local.epul4a.fotosharing.entity.User;

import java.util.List;

public interface PartageService {
    public List<PhotoDto> getAllSharedAndPublicPhoto(User currentUser);
    public List<UserPartageDto> getUsersWithPermissions(Photo photo);
    public void handlePartage(Long photoId, Long userId, String permission);
}
