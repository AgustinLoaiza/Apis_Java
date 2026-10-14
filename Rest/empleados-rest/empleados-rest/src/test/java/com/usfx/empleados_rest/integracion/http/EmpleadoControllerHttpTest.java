package com.usfx.empleados_rest.integracion.http;

import com.usfx.empleados_rest.controller.EmpleadoController;
import com.usfx.empleados_rest.model.Empleado;
import com.usfx.empleados_rest.service.EmpleadoService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmpleadoController.class)
@Import(EmpleadoControllerHttpTest.Mocks.class)
class EmpleadoControllerHttpTest {
    @Autowired MockMvc mvc;
    @Autowired EmpleadoService service;

    @Test
    void post_creaEmpleado_yResponde201ConElRecurso() throws Exception {
        when(service.crear(any())).thenAnswer(invocation -> invocation.getArgument(0));

        mvc.perform(post("/api/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Ana\",\"apellido\":\"Paz\",\"cargo\":\"Analista\",\"salario\":5000,\"email\":\"ana@test.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Ana"))
                .andExpect(jsonPath("$.email").value("ana@test.com"));
    }

    @Test
    void getPorId_cuandoNoExiste_responde404SinLevantarServidor() throws Exception {
        when(service.obtener(404L)).thenThrow(new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.NOT_FOUND, "Empleado no encontrado"));

        mvc.perform(get("/api/empleados/404"))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_eliminaEmpleado_yResponde204() throws Exception {
        mvc.perform(delete("/api/empleados/3"))
                .andExpect(status().isNoContent());
        verify(service).eliminar(3L);
    }

    @TestConfiguration
    static class Mocks {
        @Bean EmpleadoService empleadoService() { return mock(EmpleadoService.class); }
    }
}
