package com.usfx.empleados_rest;

import com.usfx.empleados_rest.controller.EmpleadoController;
import com.usfx.empleados_rest.service.EmpleadoService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import static org.mockito.Mockito.mock;

@WebMvcTest(EmpleadoController.class)
@Import(EmpleadosRestApplicationTests.Mocks.class)
class EmpleadosRestApplicationTests {

	@Test
	void contextLoads() {
	}

	@TestConfiguration
	static class Mocks {
		@Bean EmpleadoService empleadoService() { return mock(EmpleadoService.class); }
	}

}
