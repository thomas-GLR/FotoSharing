package local.epul4a.fotosharing.controller;

import local.epul4a.fotosharing.dto.AlbumDto;
import local.epul4a.fotosharing.dto.PhotoDto;
import local.epul4a.fotosharing.dto.UserPartageDto;
import local.epul4a.fotosharing.entity.Album;
import local.epul4a.fotosharing.entity.User;
import local.epul4a.fotosharing.enums.Visibility;
import local.epul4a.fotosharing.service.PhotoService;
import local.epul4a.fotosharing.service.UserService;
import local.epul4a.fotosharing.service.impl.AlbumService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Controller
public class AlbumController {
    private final AlbumService albumService;
    private final UserService userService;
    private final PhotoService photoService;

    public AlbumController(AlbumService albumService, UserService userService, PhotoService photoService) {
        this.albumService = albumService;
        this.userService = userService;
        this.photoService = photoService;
    }

    @GetMapping("/albums")
    public String albumPage(Model model, Authentication authentication) {
        User user = this.userService.findByEmail(authentication.getName());
        List<AlbumDto> albums = this.albumService.getAllAlbum(user);
        model.addAttribute("albums", albums);
        model.addAttribute("currentUser", user.getName());
        return "albums";
    }

    @GetMapping("/albums/creer")
    public String createAlbumPage(Model model, Authentication authentication) {
        User user = this.userService.findByEmail(authentication.getName());
        List<PhotoDto> photos = this.photoService.getAllPhotos(user);
        model.addAttribute("album", null);
        model.addAttribute("images", photos);
        model.addAttribute("link", "/albums/creer");
        return "create-album";
    }

    @GetMapping("/albums/modifier/{id:.+}")
    public String editAlbumPage(@PathVariable("id") Long id,
                                Model model,
                                Authentication authentication) {
        User user = this.userService.findByEmail(authentication.getName());
        AlbumDto album = this.albumService.getAlbum(id);
        List<PhotoDto> photos = this.photoService.getAllPhotos(user);
        List<PhotoDto> photosFiltrer = new ArrayList<>();

        for(PhotoDto photo: photos) {
            boolean isPhotoAlreadyInAlbum = album.getPhotos().stream()
                    .anyMatch(albumPhoto -> Objects.equals(photo.getId(), albumPhoto.getId()));
            if (!isPhotoAlreadyInAlbum) {
                photosFiltrer.add(photo);
            }
        }

        photosFiltrer.addAll(album.getPhotos());
        Comparator<PhotoDto> photoDtoComparator = Comparator.comparing(PhotoDto::isChecked).reversed()
                .thenComparing(PhotoDto::getCreatedAt);
        photosFiltrer.sort(photoDtoComparator);
        model.addAttribute("album", album);
        model.addAttribute("images", photosFiltrer);
        model.addAttribute("link", "/albums/modifier/" + album.getId());
        return "create-album";
    }

    @PostMapping("/albums/creer")
    public String createAlbum(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam(value = "photos" , required = false) Long[] photosId,
            Authentication authentication
    ) {
        User user = this.userService.findByEmail(authentication.getName());
        this.albumService.create(name, description, user, photosId);
        return "redirect:/albums";
    }

    @PostMapping("/albums/modifier/{id:.+}")
    public String editAlbum(
            @PathVariable("id") Long id,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam(value = "photos" , required = false) Long[] photosId,
            @RequestParam("visibility") Visibility visibility
    ) {
        this.albumService.edit(id, name, description, visibility, photosId);
        return "redirect:/albums";
    }

    @GetMapping("/albums/delete/{id:.+}")
    public String deleteAlbum(@PathVariable("id") Long id) {
        this.albumService.delete(id);
        return "redirect:/albums";
    }

    @GetMapping("/albums/partage/{albumId:.+}")
    public String shareAlbum(@PathVariable("albumId") Long id, Model model) {
        AlbumDto album = this.albumService.getAlbum(id);

        List<UserPartageDto> users = this.albumService.getUsersWithPermissions(id);

        model.addAttribute("users", users);
        model.addAttribute("album", album);
        return "partage-albums";
    }

    @PostMapping("/albums/partage/{albumId:.+}/{userId:.+}")
    public String partageHandler(
            @PathVariable Long albumId,
            @PathVariable Long userId,
            @RequestParam("permission") String permission) {
        this.albumService.handlePartage(albumId, userId, permission);
        return "redirect:/albums/partage/" + albumId;
    }
}
