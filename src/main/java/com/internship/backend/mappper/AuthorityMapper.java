package com.internship.backend.mappper;

import com.internship.backend.dto.AuthorityDTO;
import com.internship.backend.model.Authority;
import com.internship.backend.model.User;

public class AuthorityMapper {

    public Authority mapToAuthority(AuthorityDTO authorityDTO, User user) {
        Authority authority = new Authority();
        authority.setName(authorityDTO.getRole());
        authority.setUser(user);
        return authority;
    }
}
