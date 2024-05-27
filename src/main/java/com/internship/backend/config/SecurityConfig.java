    package com.internship.backend.config;

    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.http.HttpMethod;
    import org.springframework.security.config.Customizer;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
    import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
    import org.springframework.security.core.userdetails.User;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.security.provisioning.InMemoryUserDetailsManager;
    import org.springframework.security.web.SecurityFilterChain;

    @Configuration
    @EnableWebSecurity
    public class SecurityConfig {

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder){
            UserDetails client = User.builder()
                    .username("client")
                    .password(passwordEncoder.encode("password"))
                    .roles("CLIENT")
                    .build();
            UserDetails admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("password"))
                    .roles("ADMIN")
                    .build();
            return new InMemoryUserDetailsManager(client, admin);
        }


        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
            http    .httpBasic((Customizer.withDefaults()))
                    .csrf(CsrfConfigurer::disable)
                    .authorizeHttpRequests(configurer ->
                    configurer
                            .requestMatchers(HttpMethod.GET, "/api/users").hasAnyRole("CLIENT", "ADMIN")
                            .requestMatchers(HttpMethod.GET, "/api/users/**").hasAnyRole("CLIENT", "ADMIN")
                            .requestMatchers(HttpMethod.POST, "/api/users").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PUT, "/api/users").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.DELETE, "/api/users").hasRole("ADMIN")
                            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").hasRole("ADMIN")
//                            .requestMatchers(HttpMethod.GET, "/api/users/getAll").hasAnyRole("CLIENT", "ADMIN")
//                            .requestMatchers(HttpMethod.GET, "/api/users/register").hasAnyRole( "ADMIN")
//                            .requestMatchers(HttpMethod.POST, "/api/users").hasRole("ADMIN")
//                            .requestMatchers(HttpMethod.PUT, "/api/users/update/*").hasRole("ADMIN")
//                            .requestMatchers(HttpMethod.DELETE, "/api/users/delete").hasRole("ADMIN")
//                            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                            .anyRequest().authenticated()
            );
            return http.build();
        }
    }