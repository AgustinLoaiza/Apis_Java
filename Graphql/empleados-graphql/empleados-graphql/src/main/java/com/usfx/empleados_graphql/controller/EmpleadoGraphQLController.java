package com.usfx.empleados_graphql.controller;

import com.usfx.empleados_graphql.dto.EmpleadoInput;
import com.usfx.empleados_graphql.model.Empleado;
import com.usfx.empleados_graphql.repository.EmpleadoRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class EmpleadoGraphQLController {
    private final EmpleadoRepository repository;

    public EmpleadoGraphQLController(
            EmpleadoRepository repository) {

        this.repository = repository;
    }

    @QueryMapping
    public List<Empleado> empleados() {
        return repository.findAll();
    }

    @QueryMapping
    public Empleado empleado(@Argument Long id) {
        return repository.findById(id)
                .orElse(null);
    }

    @MutationMapping
    public Empleado crearEmpleado(
            @Argument EmpleadoInput input) {

        Empleado empleado = new Empleado();

        empleado.setNombre(input.nombre());
        empleado.setApellido(input.apellido());
        empleado.setCargo(input.cargo());
        empleado.setSalario(input.salario());
        empleado.setEmail(input.email());

        return repository.save(empleado);
    }

    @MutationMapping
    public Empleado actualizarEmpleado(
            @Argument Long id,
            @Argument EmpleadoInput input) {

        Empleado empleado = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Empleado no encontrado"
                        ));

        empleado.setNombre(input.nombre());
        empleado.setApellido(input.apellido());
        empleado.setCargo(input.cargo());
        empleado.setSalario(input.salario());
        empleado.setEmail(input.email());

        return repository.save(empleado);
    }

    @MutationMapping
    public Boolean eliminarEmpleado(
            @Argument Long id) {

        if (!repository.existsById(id)) {
            return false;
        }

        repository.deleteById(id);

        return true;
    }
}
