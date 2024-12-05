package local.epul4a.fotosharing.service;

import local.epul4a.fotosharing.dto.PhotoDto;
import local.epul4a.fotosharing.entity.User;

import java.util.List;

public interface PhotoService {
    public List<PhotoDto> getAllPhotos(User currentUser);
}
