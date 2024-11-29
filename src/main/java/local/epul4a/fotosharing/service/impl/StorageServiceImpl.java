package local.epul4a.fotosharing.service.impl;

import local.epul4a.fotosharing.entity.Photo;
import local.epul4a.fotosharing.enums.Visibility;
import local.epul4a.fotosharing.service.StorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Service
public class StorageServiceImpl implements StorageService {
    private final Path root = Paths.get("./uploads");
    @Override
    public void save(MultipartFile file) {
        try {
            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
//      String url = MvcUriComponentsBuilder
//              .fromMethodName(ImageController.class, "getImage", file.getOriginalFilename()).build().toString();
            Photo imageInfo = new Photo(
                    "",
                    "",
                    this.root.resolve(file.getOriginalFilename()).toString(),
                    Visibility.PRIVATE,
                    // TODO récupérer l'utilisateur depuis la session
            );
            this.imageRepository.save(imageInfo);
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("Un fichier de ce nom existe déjà.");
            }

            throw new RuntimeException(e.getMessage());
        }
    }
}
