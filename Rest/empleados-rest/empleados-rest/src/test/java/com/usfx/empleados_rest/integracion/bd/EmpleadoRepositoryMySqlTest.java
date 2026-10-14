package com.usfx.empleados_rest.integracion.bd;

import com.usfx.empleados_rest.model.Empleado;
import com.usfx.empleados_rest.repository.EmpleadoRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("integracion-bd")
@DataJpaTest
@Testcontainers
class EmpleadoRepositoryMySqlTest {
    @Container
    static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.4")
            .withDatabaseName("empleados")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void datasource(DynamicPropertyRegistry properties) {
        properties.add("spring.datasource.url", mysql::getJdbcUrl);
        properties.add("spring.datasource.username", mysql::getUsername);
        properties.add("spring.datasource.password", mysql::getPassword);
        properties.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired EmpleadoRepository repository;

    @Test
    void guardaYConsultaUnEmpleadoEnMySqlReal() {
        Empleado guardado = repository.saveAndFlush(
                new Empleado("Lucía", "Gómez", "Contadora", 6200.0, "lucia@empresa.test"));

        Empleado encontrado = repository.findById(guardado.getId()).orElseThrow();

        assertThat(encontrado.getNombre()).isEqualTo("Lucía");
        assertThat(encontrado.getSalario()).isEqualTo(6200.0);
    }

    @Test
    void eliminaUnEmpleadoPersistido() {
        Empleado guardado = repository.saveAndFlush(
                new Empleado("Pedro", "Lima", "Soporte", 3000.0, "pedro@empresa.test"));

        repository.deleteById(guardado.getId());
        repository.flush();

        assertThat(repository.existsById(guardado.getId())).isFalse();
    }
}
