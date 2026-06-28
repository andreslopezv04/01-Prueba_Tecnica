package com.vortexbird.orders.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

/*
 * Entidad de dominio que representa a un usuario del sistema.
 * Lo usan la seguridad (para autenticar) y el servicio (para resolver nombres).
 * Existe con el fin de saber quién crea y quién aprueba las órdenes sin acoplarse a la persistencia.
 */

@Getter
@AllArgsConstructor
public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Set<Role> roles;
}
