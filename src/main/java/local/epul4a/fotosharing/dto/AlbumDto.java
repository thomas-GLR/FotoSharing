package local.epul4a.fotosharing.dto;

import local.epul4a.fotosharing.entity.Album;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AlbumDto {
    private Long id;
    private String name;
    private String description;
    private String ownerName;
    private String visibility;
    private List<PhotoDto> photos;

    public AlbumDto(Album album) {
        this.id = album.getId();
        this.name = album.getName();
        this.description = album.getDescription();
        this.ownerName = album.getOwner().getName();
        this.visibility = album.getVisibility().name();
        this.photos = album.getPhotos().stream()
                .map(currentAlbum -> new PhotoDto(currentAlbum, true))
                .toList();
    }
}
