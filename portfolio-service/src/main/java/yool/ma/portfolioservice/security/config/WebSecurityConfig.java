package yool.ma.portfolioservice.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import yool.ma.portfolioservice.security.jwt.JwtAuthenticationEntryPoint;
import yool.ma.portfolioservice.security.jwt.JwtAuthenticationFilter;
import yool.ma.portfolioservice.security.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Bean
    public JwtAuthenticationFilter authenticationJwtTokenFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/auth/**").permitAll()

                                .requestMatchers("/api/directeur/**").permitAll()
                                .requestMatchers("/api/recruteur/**").permitAll()




                                .requestMatchers("/api/test/all").permitAll()
                                .requestMatchers("/h2-console/**").permitAll()
                                .requestMatchers("/api/profiles/**").permitAll()
                                .requestMatchers("/api/profiles/bio-check").permitAll()


                                .requestMatchers("/api/certifications/**").permitAll()
                                .requestMatchers("/api/certifications/profile/**").permitAll()
                                .requestMatchers("/api/projects/**").permitAll()
                                .requestMatchers("/api/projects/profile/**").permitAll()
                                .requestMatchers("/api/projects/**").permitAll()
                                .requestMatchers("/api/feedback/project/**").permitAll()
                                .requestMatchers("/api/feedback/reviewer/**").permitAll()
                                .requestMatchers("/api/feedback/**").permitAll()

                                .requestMatchers("/uploads/profile-pictures/**").permitAll()
                                .requestMatchers("/uploads/**").permitAll()


                                .requestMatchers("/api/project-media/download/**").permitAll() // Allow downloading files without authentication
                                .requestMatchers("/api/project-media/**").permitAll() // Allow downloading files without authentication

                                .requestMatchers("/api/user-media/**").permitAll() // Allow downloading files without authentication

                                .requestMatchers("/api/user-media/upload/**").permitAll() // Allow downloading files without authentication
                                .requestMatchers("/api/user-media/user/**").permitAll() // Allow downloading files without authentication

                                .requestMatchers("/api/user-media/download/**").permitAll() // Allow downloading files without authentication
                                .requestMatchers("/api/users/**").authenticated() // Require authentication for other user-media endpoints



                                // Swagger UI
                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()

                                .anyRequest().authenticated()
                );

        // Fix for H2 console
        http.headers(headers -> headers.frameOptions(frameOption -> frameOption.sameOrigin()));

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}