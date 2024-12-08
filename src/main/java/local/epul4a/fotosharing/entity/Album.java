package local.epul4a.fotosharing.entity;


import jakarta.persistence.*;
import local.epul4a.fotosharing.enums.Visibility;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false)
    private String name;
    private String description;
    private Visibility visibility;
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="album_photo",
            joinColumns={@JoinColumn(name="album_id", referencedColumnName="id")},
            inverseJoinColumns={@JoinColumn(name="photo_id",
                    referencedColumnName="id")})
    private List<Photo> photos;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @OneToMany(mappedBy = "album", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval=true)
    private List<AlbumPartage> albumPartages;

    public Album() {

    }

    public void clearPhotos() {
        photos.clear();
    }

    public void clearPartages() {
        albumPartages.clear();
    }
}
