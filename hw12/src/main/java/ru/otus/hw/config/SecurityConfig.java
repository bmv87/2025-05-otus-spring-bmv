package ru.otus.hw.config;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import ru.otus.hw.security.CustomAccessDeniedHandler;
import ru.otus.hw.security.CustomAuthenticationEntryPoint;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @SneakyThrows
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(config -> {
                    config.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .cors(Customizer.withDefaults())
                .httpBasic(conf -> conf.authenticationEntryPoint(new CustomAuthenticationEntryPoint()))
                .exceptionHandling(ex -> ex.accessDeniedHandler(accessDeniedHandler()))
                .authorizeHttpRequests(config -> {
                    config
                            .requestMatchers(HttpMethod.OPTIONS, "/api/v1/**").permitAll()
                            .requestMatchers(HttpMethod.DELETE, "/api/v1/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PUT, "/api/v1/**").hasAnyRole("ADMIN", "AUTHOR")
                            .requestMatchers(HttpMethod.POST, "/api/v1/**").hasAnyRole("ADMIN")
                            .requestMatchers(HttpMethod.GET, "/api/v1/**").authenticated()
                            .requestMatchers("/**").anonymous()
                            .anyRequest().authenticated();
                })
                .authenticationProvider(authenticationProvider())
                .build();
    }

    private AuthenticationProvider authenticationProvider() {
        PasswordEncoder passwordEncoder = encoder();
        final DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }
}
