package com.vortexbird.orders.infrastructure.adapter.out.persistence.repository;


import com.vortexbird.orders.infrastructure.adapter.out.persistence.entity.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/*
 * Repositorio Spring Data de usuarios (búsqueda por email para el login).
 */

public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {
    Optional<UserJpaEntity> findByEmail(String email);
}
