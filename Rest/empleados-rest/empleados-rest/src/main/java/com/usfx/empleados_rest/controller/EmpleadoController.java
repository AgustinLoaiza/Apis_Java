package com.usfx.empleados_rest.controller;

import com.usfx.empleados_rest.model.Empleado;
import com.usfx.empleados_rest.service.EmpleadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empleados")
@Tag(name = "Empleados",
        description = "API REST para la gestión de empleados")
public class EmpleadoController {
    private final EmpleadoService service;

    public EmpleadoController(EmpleadoService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar todos los empleados")
    public List<Empleado> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener empleado por ID")
    public Empleado obtener(@PathVariable Long id) {
        return service.obtener(id);
    }

    @PostMapping
    @Operation(summary = "Crear empleado")
    public ResponseEntity<Empleado> crear(
            @RequestBody Empleado empleado) {

        Empleado creado = service.crear(empleado);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(creado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar empleado")
    public Empleado actualizar(
            @PathVariable Long id,
            @RequestBody Empleado empleado) {

        return service.actualizar(id, empleado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar empleado")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        service.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}
