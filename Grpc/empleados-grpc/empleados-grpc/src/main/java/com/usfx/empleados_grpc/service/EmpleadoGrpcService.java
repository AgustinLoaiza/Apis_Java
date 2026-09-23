package com.usfx.empleados_grpc.service;

import com.usfx.empleados_grpc.grpc.ActualizarEmpleadoRequest;
import com.usfx.empleados_grpc.grpc.EliminarResponse;
import com.usfx.empleados_grpc.grpc.EmpleadoInput;
import com.usfx.empleados_grpc.grpc.EmpleadoMessage;
import com.usfx.empleados_grpc.grpc.EmpleadoRequest;
import com.usfx.empleados_grpc.grpc.EmpleadoServiceGrpc;
import com.usfx.empleados_grpc.grpc.ListaEmpleados;
import com.usfx.empleados_grpc.grpc.Vacio;

import com.usfx.empleados_grpc.model.Empleado;
import com.usfx.empleados_grpc.repository.EmpleadoRepository;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpleadoGrpcService extends EmpleadoServiceGrpc.EmpleadoServiceImplBase{
    private final EmpleadoRepository repository;

    public EmpleadoGrpcService(
            EmpleadoRepository repository
    ) {
        this.repository = repository;
    }

    @Override
    public void listarEmpleados(
            Vacio request,
            StreamObserver<ListaEmpleados> responseObserver
    ) {

        List<EmpleadoMessage> empleados =
                repository.findAll()
                        .stream()
                        .map(this::convertirAProto)
                        .toList();

        ListaEmpleados response =
                ListaEmpleados.newBuilder()
                        .addAllEmpleados(empleados)
                        .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void obtenerEmpleado(
            EmpleadoRequest request,
            StreamObserver<EmpleadoMessage> responseObserver
    ) {

        Empleado empleado =
                repository.findById(request.getId())
                        .orElse(null);

        if (empleado == null) {

            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription(
                                    "Empleado con ID "
                                            + request.getId()
                                            + " no encontrado"
                            )
                            .asRuntimeException()
            );

            return;
        }

        responseObserver.onNext(
                convertirAProto(empleado)
        );

        responseObserver.onCompleted();
    }

    @Override
    public void crearEmpleado(
            EmpleadoInput request,
            StreamObserver<EmpleadoMessage> responseObserver
    ) {

        Empleado empleado = new Empleado();

        empleado.setNombre(
                request.getNombre()
        );

        empleado.setApellido(
                request.getApellido()
        );

        empleado.setCargo(
                request.getCargo()
        );

        empleado.setSalario(
                request.getSalario()
        );

        empleado.setEmail(
                request.getEmail()
        );

        Empleado empleadoGuardado =
                repository.save(empleado);

        responseObserver.onNext(
                convertirAProto(empleadoGuardado)
        );

        responseObserver.onCompleted();
    }

    @Override
    public void actualizarEmpleado(
            ActualizarEmpleadoRequest request,
            StreamObserver<EmpleadoMessage> responseObserver
    ) {

        Empleado empleado =
                repository.findById(request.getId())
                        .orElse(null);

        if (empleado == null) {

            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription(
                                    "Empleado con ID "
                                            + request.getId()
                                            + " no encontrado"
                            )
                            .asRuntimeException()
            );

            return;
        }

        EmpleadoInput datos =
                request.getEmpleado();

        empleado.setNombre(
                datos.getNombre()
        );

        empleado.setApellido(
                datos.getApellido()
        );

        empleado.setCargo(
                datos.getCargo()
        );

        empleado.setSalario(
                datos.getSalario()
        );

        empleado.setEmail(
                datos.getEmail()
        );

        Empleado empleadoActualizado =
                repository.save(empleado);

        responseObserver.onNext(
                convertirAProto(empleadoActualizado)
        );

        responseObserver.onCompleted();
    }

    @Override
    public void eliminarEmpleado(
            EmpleadoRequest request,
            StreamObserver<EliminarResponse> responseObserver
    ) {

        long id = request.getId();

        if (!repository.existsById(id)) {

            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription(
                                    "Empleado con ID "
                                            + id
                                            + " no encontrado"
                            )
                            .asRuntimeException()
            );

            return;
        }

        repository.deleteById(id);

        EliminarResponse response =
                EliminarResponse.newBuilder()
                        .setEliminado(true)
                        .setMensaje(
                                "Empleado eliminado correctamente"
                        )
                        .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private EmpleadoMessage convertirAProto(
            Empleado empleado
    ) {

        EmpleadoMessage.Builder builder =
                EmpleadoMessage.newBuilder();

        if (empleado.getId() != null) {
            builder.setId(
                    empleado.getId()
            );
        }

        if (empleado.getNombre() != null) {
            builder.setNombre(
                    empleado.getNombre()
            );
        }

        if (empleado.getApellido() != null) {
            builder.setApellido(
                    empleado.getApellido()
            );
        }

        if (empleado.getCargo() != null) {
            builder.setCargo(
                    empleado.getCargo()
            );
        }

        if (empleado.getSalario() != null) {
            builder.setSalario(
                    empleado.getSalario()
            );
        }

        if (empleado.getEmail() != null) {
            builder.setEmail(
                    empleado.getEmail()
            );
        }

        return builder.build();
    }
}
