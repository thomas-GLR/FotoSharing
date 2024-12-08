package local.epul4a.fotosharing.entity;

import jakarta.persistence.*;
import local.epul4a.fotosharing.enums.Visibility;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean validate;
    @OneToMany(mappedBy="photo", fetch=FetchType.EAGER, orphanRemoval=true)
    private List<Partage> partages;

    public Photo() {

    }

    public Photo(String title, String description, String url, Visibility visibility, User owner) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.visibility = visibility;
        this.owner = owner;
        this.validate = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public String visibilityName() {
        return getVisibility().name();
    }

    public String ownerName() {
        return getOwner().getName();
    }

    public String createdAtToString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return getCreatedAt().format(formatter);
    }

    public String updatedAtToString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return getUpdatedAt().format(formatter);
    }
}
