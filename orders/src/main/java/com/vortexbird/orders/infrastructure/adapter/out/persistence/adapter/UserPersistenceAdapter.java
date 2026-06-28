package com.vortexbird.orders.infrastructure.adapter.out.persistence.adapter;

import com.vortexbird.orders.application.port.out.UserRepositoryPort;
import com.vortexbird.orders.domain.model.User;
import com.vortexbird.orders.infrastructure.adapter.out.persistence.mapper.UserPersistenceMapper;
import com.vortexbird.orders.infrastructure.adapter.out.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/*
 * Adaptador de salida
 * que implementa UserRepositoryPort con Spring Data JPA.
 */
@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserRepositoryPort {

    private final UserJpaRepository userJpaRepository;
    private final UserPersistenceMapper userPersistenceMapper;

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userJpaRepository.findByEmail(email).map(userPersistenceMapper::toDomain);
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return userJpaRepository.findById(id).map(userPersistenceMapper::toDomain);
    }
}
