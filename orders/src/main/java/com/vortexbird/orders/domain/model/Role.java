package com.vortexbird.orders.domain.model;

/*
 * Roles del sistema (ADMIN, OPERATOR) que definen los permisos.
 * Son fijos y conocidos en compilación, por eso enum y no una tabla catálogo.
 */
public enum Role {
    ADMIN,
    OPERATOR
}
