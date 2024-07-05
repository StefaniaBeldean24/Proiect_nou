package com.internship.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;


import java.util.List;
import java.util.Set;

@Document
@Getter
@Setter
public class Users {

    @Id
    private Integer id;

    @Field
    private String username;

    @Field
    private String password;

    @Field
    private String email;

    @Field
    private Set<Authority> authorities;

    @Field
    //@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Reservation> reservations;


    public Users(String username, String password, String email, Set<Authority> authorities) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.authorities = authorities;
    }

    public Users() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Users users = (Users) o;
        return id == users.id && username.equals(users.username) && password.equals(users.password) && email.equals(users.email);
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