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
    import org.springframework.security.crypto.password.PasswordEncoder;
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

                            .requestMatchers(HttpMethod.GET, "/api/location/getAll").hasAnyRole("ADMIN", "CLIENT")
                            .requestMatchers(HttpMethod.POST, "/api/location/add").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PUT, "/api/location/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.DELETE, "/api/location/**").hasRole("ADMIN")

                            .requestMatchers(HttpMethod.GET, "/api/tennisCourts/getAll").hasAnyRole("ADMIN", "CLIENT")
                            .requestMatchers(HttpMethod.POST, "/api/tennisCourts/add").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PUT, "/api/tennisCourts/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.DELETE, "/api/tennisCourts/**").hasRole("ADMIN")

                            .requestMatchers(HttpMethod.GET, "/api/prices/getAll").hasAnyRole("ADMIN", "CLIENT")
                            .requestMatchers(HttpMethod.POST, "/api/prices/add").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PUT, "/api/prices/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.DELETE, "/api/prices/**").hasRole("ADMIN")

                            .requestMatchers(HttpMethod.GET, "/api/reservations/getAll").hasAnyRole("ADMIN", "CLIENT")
                            .requestMatchers(HttpMethod.POST, "/api/reservations/add").hasAnyRole("ADMIN", "CLIENT")
                            .requestMatchers(HttpMethod.PUT, "/api/reservations/**").hasAnyRole("ADMIN", "CLIENT")
                            .requestMatchers(HttpMethod.DELETE, "/api/reservations/**").hasAnyRole("ADMIN", "CLIENT")

                            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").hasAnyRole("ADMIN", "CLIENT")
                            .anyRequest().authenticated()
            );
            return http.build();
        }
    }