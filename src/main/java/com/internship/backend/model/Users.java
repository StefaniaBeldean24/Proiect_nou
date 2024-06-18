package com.internship.backend.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;
import java.util.Set;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Valid
@Builder
public class Users {


    @NotNull(message="id must not be null")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    @Size(min=4, max=10, message="username must be between 4 and 10 characters")
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank
    @Size(min=8, max=255, message = "password must be between 8 and 30 characters")
    private String password;

    @NotBlank
    @Size(min=6, max=30, message= "email must be between 6 and 30 characters")
    private String email;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Users users = (Users) o;
        return id == users.id && username.equals(users.username) && password.equals(users.password) && email.equals(users.email);
    }

    //@JsonIgnore
    @JsonManagedReference
    @OneToMany(mappedBy="user", fetch = FetchType.EAGER)
    private Set<Authority> authorities;

    @JsonIgnore
    @OneToMany(mappedBy="user", fetch = FetchType.EAGER)
    private List<Reservation> reservations;

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + username.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + email.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
