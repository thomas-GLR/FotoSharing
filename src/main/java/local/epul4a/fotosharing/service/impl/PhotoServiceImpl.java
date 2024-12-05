package local.epul4a.fotosharing.service.impl;

import local.epul4a.fotosharing.dto.PhotoDto;
import local.epul4a.fotosharing.entity.User;
import local.epul4a.fotosharing.repository.PhotoRepository;
import local.epul4a.fotosharing.service.PhotoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhotoServiceImpl implements PhotoService {
    private final PhotoRepository photoRepository;
    public PhotoServiceImpl(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }
    public List<PhotoDto> getAllPhotos(User currentUser) {
        return this.photoRepository.findByOwner(currentUser).stream().map(PhotoDto::new).toList();
    }
}
