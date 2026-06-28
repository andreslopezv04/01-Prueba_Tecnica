package com.vortexbird.orders.application.port.out;

/*
 * Puerto de salida para guardar y leer los archivos de factura.
 * Lo implementan el adaptador local y el de S3 (si se elije).
 * Desacopla el almacenamiento del resto del sistema (req. 4.2).
 */
public interface StoragePort {
    String store(byte[] content, String filename);
    byte[] load(String reference);
}
