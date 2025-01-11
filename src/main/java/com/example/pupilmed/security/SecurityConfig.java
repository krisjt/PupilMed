package com.example.pupilmed.security;

import com.example.pupilmed.security.auth.jwt.AuthEntryPointJwt;
import com.example.pupilmed.security.auth.jwt.AuthTokenFilter;
import com.example.pupilmed.security.auth.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailService;
    private final AuthTokenFilter authTokenFilter;

    private final String roleAdmin = "ADMIN";

    @Autowired
    AuthEntryPointJwt unauthorizedHandler;

//    @Bean
//    public AuthTokenFilter authTokenFilter(){
//        return new AuthTokenFilter();
//    }

    @Autowired
    public SecurityConfig(CustomUserDetailsService myUserDetailService, AuthTokenFilter authTokenFilter) {
        this.userDetailService = myUserDetailService;
        this.authTokenFilter = authTokenFilter;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(
                        req -> req.requestMatchers("/login/**", "/logout/**").permitAll()
                                .requestMatchers("/admin/**").hasRole(roleAdmin)
                                .requestMatchers("/owner/**").hasAnyRole("OWNER",roleAdmin)
                                .requestMatchers("/vet/**").hasAnyRole("VET",roleAdmin)
                                .anyRequest().authenticated()
                )
                .userDetailsService(userDetailService)
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

}
