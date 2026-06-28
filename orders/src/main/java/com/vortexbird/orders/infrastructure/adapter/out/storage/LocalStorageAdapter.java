package com.vortexbird.orders.infrastructure.adapter.out.storage;

import com.vortexbird.orders.application.port.out.StoragePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/*
 * Adaptador de StoragePort que guarda las facturas en disco local.
 * Maneja los errores de entrada y salida.
 */
@Component
@Profile ("!s3")
public class LocalStorageAdapter implements StoragePort {

    @Value("${storage.local.path:uploads}")
    private String basePath;
    @Override
    public String store(byte[] content, String filename) {
        try {
            String uniqueName = UUID.randomUUID() + "_" + filename;
            Path directory = Paths.get(basePath);
            Files.createDirectories(directory);
            Path target = directory.resolve(uniqueName);
            Files.write(target, content);
            return "/files/" + uniqueName;
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo", e);
        }
    }

    @Override
    public byte[] load(String reference) {
        try {
            String uniqueName = reference.replaceFirst("^/files/", "");
            Path target = Paths.get(basePath).resolve(uniqueName);
            return Files.readAllBytes(target);
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo", e);
        }
    }
}
