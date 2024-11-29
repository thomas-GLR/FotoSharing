package local.epul4a.fotosharing.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Commentaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false)
    private String text;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "photo_id", nullable = false)
    private Photo photo;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Commentaire() {

    }
}
