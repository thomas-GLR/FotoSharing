package local.epul4a.fotosharing.entity;

import jakarta.persistence.*;
import local.epul4a.fotosharing.enums.PermissionLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class AlbumPartage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "album_id", nullable = false)
    private Album album;
    private PermissionLevel permissionLevel;
}
