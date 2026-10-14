package com.usfx.empleados_rest.contrato;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usfx.empleados_rest.controller.EmpleadoController;
import com.usfx.empleados_rest.service.EmpleadoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Contrato del consumidor: documenta lo mínimo que el proveedor debe conservar. */
@WebMvcTest(EmpleadoController.class)
@Import(EmpleadosApiContractTest.Mocks.class)
class EmpleadosApiContractTest {
    @Autowired MockMvc mvc;
    @Autowired EmpleadoService service;
    private final ObjectMapper json = new ObjectMapper();

    @Test
    void proveedorMantieneElContratoDeConsultaPorId() throws Exception {
        when(service.obtener(1L)).thenReturn(new com.usfx.empleados_rest.model.Empleado(
                "Ana", "Paz", "Analista", 5000.0, "ana@empresa.test"));

        String response = mvc.perform(get("/api/empleados/1"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JsonNode actual = json.readTree(response);
        JsonNode contrato = leerContrato();
        assertThat(actual.fieldNames()).toIterable().containsAll(contrato.get("respuesta").get("camposObligatorios").findValuesAsText("nombre"));
        for (JsonNode campo : contrato.get("respuesta").get("camposObligatorios")) {
            assertThat(actual.has(campo.asText())).as("campo contractual: " + campo).isTrue();
        }
    }

    private JsonNode leerContrato() throws Exception {
        try (InputStream stream = getClass().getResourceAsStream("/contracts/empleados-api-v1.json")) {
            return json.readTree(stream);
        }
    }

    @TestConfiguration
    static class Mocks {
        @Bean EmpleadoService empleadoService() { return mock(EmpleadoService.class); }
    }
}
