package local.epul4a.fotosharing.dto;

import local.epul4a.fotosharing.controller.PhotoController;
import local.epul4a.fotosharing.entity.Photo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

@Getter
@Setter
public class PhotoDto {
    private Long id;
    private String title;
    private String description;
    private String url;
    private String visibility;
    private String ownerName;
    private String createdAt;
    private String updatedAt;

    public PhotoDto(Photo photo) {
        this.id = photo.getId();
        this.title = photo.getTitle();
        this.description = photo.getDescription();
        this.url = MvcUriComponentsBuilder
                .fromMethodName(PhotoController.class, "getImage", photo.getId()).build().toString();;
        this.visibility = photo.visibilityName();
        this.ownerName = photo.ownerName();
        this.createdAt = photo.createdAtToString();
        this.updatedAt = photo.updatedAtToString();
    }
}
