package com.usfx.empleados_grpc;

import com.usfx.empleados_grpc.config.GrpcServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class EmpleadosGrpcApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext context =
				SpringApplication.run(
						EmpleadosGrpcApplication.class,
						args
				);

		GrpcServer grpcServer =
				context.getBean(GrpcServer.class);

		try {

			grpcServer.bloquearHastaApagado();

		} catch (InterruptedException e) {

			Thread.currentThread().interrupt();
		}
	}
}