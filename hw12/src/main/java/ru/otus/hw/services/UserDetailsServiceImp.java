package ru.otus.hw.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.RoleEntity;
import ru.otus.hw.models.UserEntity;
import ru.otus.hw.repositories.UserRepository;

@Component
@RequiredArgsConstructor
public class UserDetailsServiceImp implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var roles = user.getRoles().stream().map(RoleEntity::getName).toList();
        var userBuilder = User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .disabled(false);
        for (var role : roles) {
            userBuilder.roles(role);
        }
        return userBuilder.build();
    }
}
