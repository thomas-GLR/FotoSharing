package local.epul4a.fotosharing.service.impl;

import local.epul4a.fotosharing.entity.Photo;
import local.epul4a.fotosharing.entity.User;
import local.epul4a.fotosharing.enums.Visibility;
import local.epul4a.fotosharing.repository.PhotoRepository;
import local.epul4a.fotosharing.service.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class StorageServiceImpl implements StorageService {
    private final Path root = Paths.get("./uploads");
    private final PhotoRepository photoRepository;

    public StorageServiceImpl(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }
    @Override
    public void save(MultipartFile file, User owner, String title, String description) {
        try {
            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
            Photo imageInfo = new Photo(
                    title,
                    description,
                    this.root.resolve(file.getOriginalFilename()).toString(),
                    Visibility.PRIVATE,
                    owner
            );
            this.photoRepository.save(imageInfo);
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("Un fichier de ce nom existe déjà.");
            }

            throw new RuntimeException(e.getMessage());
        }
    }

    public Resource load(Long id) {
        try {
            Photo photo = photoRepository.findById(id).get();
            Path file = new File(photo.getUrl()).toPath();
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Impossible de lire le fichier !");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Erreur : " + e.getMessage());
        }
    }
}
