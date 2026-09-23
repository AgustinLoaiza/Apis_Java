package com.usfx.empleados_rest.service;

import com.usfx.empleados_rest.model.Empleado;
import com.usfx.empleados_rest.repository.EmpleadoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EmpleadoService {
    private final EmpleadoRepository repository;

    public EmpleadoService(EmpleadoRepository repository) {
        this.repository = repository;
    }

    public List<Empleado> listar() {
        return repository.findAll();
    }

    public Empleado obtener(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Empleado no encontrado"
                        ));
    }

    public Empleado crear(Empleado empleado) {
        empleado.setId(null);
        return repository.save(empleado);
    }

    public Empleado actualizar(Long id, Empleado datos) {

        Empleado empleado = obtener(id);

        empleado.setNombre(datos.getNombre());
        empleado.setApellido(datos.getApellido());
        empleado.setCargo(datos.getCargo());
        empleado.setSalario(datos.getSalario());
        empleado.setEmail(datos.getEmail());

        return repository.save(empleado);
    }

    public void eliminar(Long id) {

        Empleado empleado = obtener(id);

        repository.delete(empleado);
    }
}
