package ru.otus.hw;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static ru.otus.hw.security.AuthorityConstants.ADMIN;
import static ru.otus.hw.security.AuthorityConstants.AUTHOR;
import static ru.otus.hw.security.AuthorityConstants.READER;

@TestConfiguration
public class InMemoryUserDetailsManagerConfiguration {
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails admin = User
                .builder()
                .username("admin")
                .password("$2a$10$T6zbnzV9yl84EnZog0j.heRstyvG9ArosPF3lEPgzLMX0g5ZIxj7i")
                .roles(ADMIN)
                .build();
        UserDetails author = User
                .builder()
                .username("author")
                .password("$2a$10$T6zbnzV9yl84EnZog0j.heRstyvG9ArosPF3lEPgzLMX0g5ZIxj7i")
                .roles(AUTHOR)
                .build();
        UserDetails reader = User
                .builder()
                .username("reader")
                .password("$2a$10$T6zbnzV9yl84EnZog0j.heRstyvG9ArosPF3lEPgzLMX0g5ZIxj7i")
                .roles(READER)
                .build();
        return new InMemoryUserDetailsManager(admin, author, reader);
    }
}