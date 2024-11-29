package local.epul4a.fotosharing.controller;

import local.epul4a.fotosharing.service.StorageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class PhotoController {

    private final StorageService storageService;

    public PhotoController(StorageService storageService) {
        this.storageService = storageService;
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
    public String uploadImage(Model model, @RequestParam("file") MultipartFile file) {
        String message;

        try {
            storageService.save(file);

            message = "L'image a été téléchargée avec succès : " + file.getOriginalFilename();
            model.addAttribute("message", message);
        } catch (Exception e) {
            message = "Impossible de télécharger l'image : " + file.getOriginalFilename() + ". Erreur : " + e.getMessage();
            model.addAttribute("message", message);
        }

        return "upload_form";
    }
}
