package local.epul4a.fotosharing.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    public void save(MultipartFile file);
}
