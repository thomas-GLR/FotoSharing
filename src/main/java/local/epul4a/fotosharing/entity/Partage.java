package local.epul4a.fotosharing.entity;

import jakarta.persistence.*;
import local.epul4a.fotosharing.enums.PermissionLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Partage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "photo_id", nullable = false)
    private Photo photo;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private PermissionLevel permissionLevel;
    private LocalDateTime createdAt;

    public Partage() {

    }
}
