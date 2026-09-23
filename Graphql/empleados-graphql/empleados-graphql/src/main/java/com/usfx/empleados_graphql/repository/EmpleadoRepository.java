package com.usfx.empleados_graphql.repository;

import com.usfx.empleados_graphql.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
}
