package com.collabrix.authservice.model.dtos.request;

import lombok.Data;

@Data
public class LoginUserDTO {
    private String email;
    private String password;
}
