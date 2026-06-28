package com.vortexbird.orders.infrastructure.security;

import com.vortexbird.orders.application.port.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.vortexbird.orders.domain.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 * Carga un usuario por email para la autenticación de Spring Security.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepositoryPort userRepositoryPort;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepositoryPort.findUserByEmail(email).orElseThrow(
                () -> {
                    log.warn("UserDetailsService - user not found: {}", email);
                    return new UsernameNotFoundException("User not found" + email);
                }
        );

        return new CustomUserDetails(user);
    }
}
