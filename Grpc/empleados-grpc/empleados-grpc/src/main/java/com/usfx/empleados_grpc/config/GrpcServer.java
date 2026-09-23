package com.usfx.empleados_grpc.config;

import com.usfx.empleados_grpc.service.EmpleadoGrpcService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class GrpcServer {
    private final EmpleadoGrpcService empleadoGrpcService;

    @Value("${grpc.server.port:9090}")
    private int puerto;

    private Server server;

    public GrpcServer(
            EmpleadoGrpcService empleadoGrpcService
    ) {
        this.empleadoGrpcService = empleadoGrpcService;
    }

    @PostConstruct
    public void iniciar() throws IOException {

        server = ServerBuilder
                .forPort(puerto)
                .addService(empleadoGrpcService)
                .build()
                .start();

        System.out.println(
                "======================================="
        );
        System.out.println(
                "Servidor gRPC iniciado correctamente"
        );
        System.out.println(
                "Puerto: " + puerto
        );
        System.out.println(
                "======================================="
        );
    }

    /*
     * Mantiene el proceso vivo esperando
     * hasta que el servidor sea detenido.
     */
    public void bloquearHastaApagado()
            throws InterruptedException {

        if (server != null) {
            server.awaitTermination();
        }
    }

    @PreDestroy
    public void detener() {

        if (server == null) {
            return;
        }

        System.out.println(
                "Deteniendo servidor gRPC..."
        );

        server.shutdown();

        try {

            if (!server.awaitTermination(
                    5,
                    TimeUnit.SECONDS
            )) {
                server.shutdownNow();
            }

        } catch (InterruptedException e) {

            server.shutdownNow();

            Thread.currentThread().interrupt();
        }
    }
}
