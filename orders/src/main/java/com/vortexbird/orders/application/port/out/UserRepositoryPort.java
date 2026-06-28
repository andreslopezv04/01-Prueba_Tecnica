package com.vortexbird.orders.application.port.out;

import com.vortexbird.orders.domain.model.User;

import java.util.Optional;

/*
 * Puerto de salida para consultar usuarios.
 * Lo implementa el adaptador JPA de usuarios
 * Y lo usan el servicio y la seguridad.
 */
public interface UserRepositoryPort {
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserById (Long id);
}
