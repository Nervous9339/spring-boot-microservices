package ru.nothingtdh.photoapp.api.users.ui.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseModel {

    private String userId;
    private String firstName;
    private String lastName;
    private String email;
}
