package local.epul4a.fotosharing.controller;

import local.epul4a.fotosharing.entity.User;
import local.epul4a.fotosharing.service.StorageService;
import local.epul4a.fotosharing.service.UserService;
import local.epul4a.fotosharing.service.impl.UserServiceImpl;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Controller
public class PhotoController {
    private static final List<String> contentTypes = Arrays.asList("image/png", "image/jpeg", "image/gif");
    private final StorageService storageService;
    private final UserService userService;

    public PhotoController(StorageService storageService, UserService userService) {
        this.storageService = storageService;
        this.userService = userService;
    }

    @GetMapping("/photos")
    public String photoPage(Model model) {
        return "photos";
    }
    @GetMapping("/photos/nouveau")
    public String uploadPage(Model model) {
        return "upload";
    }

    @PostMapping("/photos/upload")
    public String uploadImage(
            Model model,
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            Authentication authentication) {
        String fileContentType = file.getContentType();
        long fileSize = file.getSize();
        String message;
        if(contentTypes.contains(fileContentType) && fileSize <= 1000000) {
            User owner = this.userService.findByEmail(authentication.getName());
            try {
                storageService.save(file, owner, title, description);

                message = "L'image a été téléchargée avec succès : " + file.getOriginalFilename();
                model.addAttribute("message", message);
            } catch (Exception e) {
                message = "Impossible de télécharger l'image : " + file.getOriginalFilename() + ". Erreur : " + e.getMessage();
                model.addAttribute("message", message);
            }

            return "redirect:/photos";
        } else {
            message = "Le format ou la taille de l'image est incorrect";
            model.addAttribute("message", message);
            return "upload";
        }
    }
}
