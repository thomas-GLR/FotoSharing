package local.epul4a.fotosharing.service;

import local.epul4a.fotosharing.entity.User;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    public void save(MultipartFile file, User owner, String title, String description);
    public Resource load(Long id);
    public boolean deleteFile(String url);
}
