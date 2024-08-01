package com.internship.backend.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
class AuthorityTest {

    private Authority authorityAdmin;
    private Authority authorityClient;


    @BeforeEach
    void setUp() {
        authorityAdmin = createDefaultAuthorityAdmin();
        authorityClient = createDefaultAuthorityClient();
    }

    public Authority createDefaultAuthorityAdmin() {
        return Authority.builder()
                .id(1)
                .name("ROLE_ADMIN")
                .build();
    }

    public Authority createDefaultAuthorityClient() {
        return Authority.builder()
                .id(2)
                .name("ROLE_CLIENT")
                .build();
    }

    @AfterEach
    void tearDown() {
        authorityAdmin = null;
        authorityClient = null;
    }

    @Test
    void getId() {
        assertEquals(1, authorityAdmin.getId());
        assertEquals(2, authorityClient.getId());
    }

    @Test
    void getName() {
        assertEquals("ROLE_ADMIN", authorityAdmin.getName());
        assertEquals("ROLE_CLIENT", authorityClient.getName());
    }

    @Test
    void getUser() {
        User user = User.builder()
                .id(1)
                .username("user")
                .password("password")
                .email("email@yahoo.com")
                .authorities(Set.of(authorityAdmin))
                .build();

        Set<Authority> expectedAuthority = Set.of(authorityAdmin);
        assertEquals(expectedAuthority, user.getAuthorities());
    }

    @Test
    void setId() {
        authorityAdmin.setId(4);
        authorityClient.setId(5);

        assertEquals(4, authorityAdmin.getId());
        assertEquals(5, authorityClient.getId());
    }

    @Test
    void setName() {
        authorityAdmin.setName("ROLE_CLIENT");
        authorityClient.setName("ROLE_ADMIN");

        assertEquals("ROLE_CLIENT", authorityAdmin.getName());
        assertEquals("ROLE_ADMIN", authorityClient.getName());
    }

    @Test
    void setUser() {

        User user2 = User.builder()
                .id(2)
                .username("user2")
                .password("password2")
                .email("email2@yahoo.com")
                .authorities(new HashSet<>())
                .build();

        authorityAdmin.setUser(user2);
        user2.getAuthorities().add(authorityAdmin);
        Set<Authority> expectedAuthority = Set.of(authorityAdmin);
        assertEquals(expectedAuthority, user2.getAuthorities());
    }
}