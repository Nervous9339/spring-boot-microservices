package ru.nothingtdh.photoapp.api.users.shared;

import lombok.Getter;
import lombok.Setter;
import ru.nothingtdh.photoapp.api.users.ui.model.AlbumResponseModel;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class UserDto implements Serializable {

    private String userId;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String encryptedPassword;
    private List<AlbumResponseModel> albums;
}
