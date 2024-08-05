package com.internship.backend.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Document
@Getter
@Setter
@Builder
@AllArgsConstructor
public class User {

    @Id
    private String id;

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

    public User() {
        this.id = UUID.randomUUID().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User users = (User) o;
        return id.equals(users.id) && username.equals(users.username) && password.equals(users.password) && email.equals(users.email);
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