package com.usfx.empleados_rest.repository;

import com.usfx.empleados_rest.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpleadoRepository  extends JpaRepository<Empleado, Long> {
}
