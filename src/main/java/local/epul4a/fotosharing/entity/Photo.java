package local.epul4a.fotosharing.entity;

import jakarta.persistence.*;
import local.epul4a.fotosharing.enums.Visibility;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false)
    private String title;
    private String description;
    @Column(nullable=false)
    private String url;
    private Visibility visibility;
    @ManyToOne( cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Photo() {

    }
}