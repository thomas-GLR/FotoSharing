package local.epul4a.fotosharing.dto;

import local.epul4a.fotosharing.entity.Partage;
import local.epul4a.fotosharing.entity.User;
import local.epul4a.fotosharing.enums.PermissionLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPartageDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String permissionLevel;

    public UserPartageDto(User user, PermissionLevel permissionLevel) {
        this.id = user.getId();
        this.firstName = user.getName().split(" ")[0];
        this.lastName = user.getName().split(" ")[1];
        this.permissionLevel = permissionLevel == null ? null : permissionLevel.name();
    }
}
