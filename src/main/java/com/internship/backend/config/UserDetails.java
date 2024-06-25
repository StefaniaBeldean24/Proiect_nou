//package com.internship.backend.config;
//
//import com.internship.backend.model.Users;
//import com.internship.backend.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//
//@Service
//public class UserDetails implements UserDetailsService {
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Override
//    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String email)
//            throws UsernameNotFoundException {
//
//        String password = null;
//        List<GrantedAuthority> authorities = null;
//        Users user = userRepository.findByEmail(email);
//
//        if(Objects.isNull(user) ){
//            throw new UsernameNotFoundException("User details not found for the email: " + email);
//        }
//        else{
//            password = user.getPassword();
//            authorities = new ArrayList<>();
//            authorities.add(new SimpleGrantedAuthority(user.getRole().toString()));
//        }
//        return new User(email, password, authorities);
//    }
//}
