package com.ceylin.companyorganizationSoftware.Config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;
    private final  AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("api/auth/**").permitAll()
                        .requestMatchers("api/user/upload-profile-picture").hasAnyAuthority("ADMIN", "MANAGER","USER") // Allow authenticated users to upload profile picture
                        .requestMatchers("api/user/profile").hasAnyAuthority("ADMIN", "MANAGER","USER")
                        .requestMatchers("api/user/users").hasAnyAuthority("ADMIN", "MANAGER")  // Allow both ADMIN and MANAGER to retrieve users
                        .requestMatchers("api/user/update/**", "api/user/delete/**").hasAnyAuthority("ADMIN", "MANAGER")  // Allow both ADMIN and MANAGER to update/delete users
                        //.requestMatchers("api/user/update","api/user/update/**").hasAuthority("MANAGER")
                        //.requestMatchers("api/user/delete","api/user/delete/**").hasAuthority("MANAGER")
                        //.requestMatchers("api/user/belonging-department").hasAuthority("MANAGER")
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("https://delta1.eu-west-1.elasticbeanstalk.com");
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedOrigin("http://localhost:3000/");
        configuration.setAllowedOriginPatterns(java.util.List.of("*"));
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
