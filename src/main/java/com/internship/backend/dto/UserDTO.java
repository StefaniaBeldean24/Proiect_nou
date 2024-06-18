package com.internship.backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private String role;
    private String username;
    private String password;
    private String email;

}
