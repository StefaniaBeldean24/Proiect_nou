    package com.internship.backend.config;

    import com.internship.backend.service.UserDetailsServiceImpl;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.http.HttpMethod;
    import org.springframework.security.config.Customizer;
    import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
    import org.springframework.security.core.userdetails.User;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.security.provisioning.InMemoryUserDetailsManager;
    import org.springframework.security.web.SecurityFilterChain;


    @Configuration
    @EnableWebSecurity
    public class SecurityConfig {

        @Autowired
        private UserDetailsServiceImpl userDetailsService;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        }


        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
            http    .httpBasic((Customizer.withDefaults()))
                    .csrf(CsrfConfigurer::disable)
                    .authorizeHttpRequests(configurer ->
                    configurer
                            .requestMatchers(HttpMethod.POST, "/api/users/register").permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/users/login").permitAll()
                            .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("CLIENT")
                            .requestMatchers(HttpMethod.POST, "/api/users/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PUT, "/api/users/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.GET, "/api/users/**").hasRole("ADMIN")
                            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").hasAnyRole("ADMIN", "CLIENT")
                            .anyRequest().authenticated()
            );
            return http.build();
        }
    }