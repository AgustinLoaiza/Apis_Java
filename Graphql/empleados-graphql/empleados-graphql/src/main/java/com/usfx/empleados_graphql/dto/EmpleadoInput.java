package com.usfx.empleados_graphql.dto;

public record EmpleadoInput(
        String nombre,
        String apellido,
        String cargo,
        Double salario,
        String email
) {
}
