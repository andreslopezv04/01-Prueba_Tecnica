package com.vortexbird.orders.infrastructure.adapter.out.persistence.mapper;

import com.vortexbird.orders.domain.model.User;
import com.vortexbird.orders.infrastructure.adapter.out.persistence.entity.UserJpaEntity;
import org.springframework.stereotype.Component;

/*
 * Convierte entre la entidad JPA de usuario y el dominio User.
 */
@Component
public class UserPersistenceMapper {
    public User toDomain(UserJpaEntity userJpaEntity){
        return new User(userJpaEntity.getId(),
                userJpaEntity.getFirstName(),
                userJpaEntity.getLastName(),
                userJpaEntity.getEmail(),
                userJpaEntity.getPassword(),
                userJpaEntity.getRoles());
    }
}
