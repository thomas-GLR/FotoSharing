package local.epul4a.fotosharing.service.impl;

import local.epul4a.fotosharing.dto.PhotoDto;
import local.epul4a.fotosharing.entity.User;
import local.epul4a.fotosharing.enums.Visibility;
import local.epul4a.fotosharing.repository.PartageRepository;
import local.epul4a.fotosharing.service.PartageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartageServiceImpl implements PartageService {
    private final PartageRepository partageRepository;

    public PartageServiceImpl(PartageRepository partageRepository) {
        this.partageRepository = partageRepository;
    }
    public List<PhotoDto> getAllSharedPhoto(User currentUser) {
       return partageRepository.findSharedPhotosWithUserId(currentUser, Visibility.PUBLIC).stream().map(PhotoDto::new).toList();
    }
}
