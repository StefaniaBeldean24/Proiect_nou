package com.internship.backend.config;

import com.internship.backend.model.Authority;
import com.internship.backend.model.User;
import com.internship.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import static java.util.Optional.ofNullable;

import java.util.*;

@Component
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var username = authentication.getName();
        var password = authentication.getCredentials().toString();
        var optionalUser = ofNullable(userRepository.findByUsername(username));
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            if (passwordEncoder.matches(password, user.getPassword())){

                return new UsernamePasswordAuthenticationToken(username, password, getGrantedAuthorities(user.getAuthorities()));
            }
            else{

                throw new BadCredentialsException("Invalid password");
            }
        }
        else{

            throw new BadCredentialsException("No user register with username: " + username);
        }
    }

    private List<GrantedAuthority> getGrantedAuthorities(Set<Authority> authorities){
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (Authority authority : authorities){
            grantedAuthorities.add(new SimpleGrantedAuthority(authority.getName()));
        }
        return grantedAuthorities;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
