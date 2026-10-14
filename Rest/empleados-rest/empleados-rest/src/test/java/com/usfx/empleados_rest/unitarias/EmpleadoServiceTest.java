package com.usfx.empleados_rest.unitarias;

import com.usfx.empleados_rest.model.Empleado;
import com.usfx.empleados_rest.repository.EmpleadoRepository;
import com.usfx.empleados_rest.service.EmpleadoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmpleadoServiceTest {
    @Mock EmpleadoRepository repository;
    @InjectMocks EmpleadoService service;

    @Test
    void listar_devuelveTodosLosEmpleados() {
        Empleado ana = empleado("Ana", "Paz");
        when(repository.findAll()).thenReturn(List.of(ana));

        List<Empleado> resultado = service.listar();

        assertThat(resultado).containsExactly(ana);
        verify(repository).findAll();
    }

    @Test
    void obtener_cuandoExiste_devuelveElEmpleado() {
        Empleado empleado = empleado("Luis", "Rojas");
        when(repository.findById(7L)).thenReturn(Optional.of(empleado));

        assertThat(service.obtener(7L)).isSameAs(empleado);
    }

    @Test
    void obtener_cuandoNoExiste_lanza404() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.obtener(99L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Empleado no encontrado")
                .extracting("statusCode.value").isEqualTo(404);
    }

    @Test
    void crear_ignoraElIdRecibido_yGuardaUnNuevoEmpleado() {
        Empleado entrada = empleado("Marta", "Vega");
        entrada.setId(123L);
        when(repository.save(entrada)).thenReturn(entrada);

        Empleado resultado = service.crear(entrada);

        assertThat(entrada.getId()).isNull();
        assertThat(resultado).isSameAs(entrada);
        verify(repository).save(entrada);
    }

    @Test
    void actualizar_copiaTodosLosCampos_yPersisteElEmpleadoExistente() {
        Empleado actual = empleado("Ana", "Paz");
        Empleado cambios = new Empleado("Ana María", "Paz", "Jefa", 9000.0, "ana@empresa.test");
        when(repository.findById(1L)).thenReturn(Optional.of(actual));
        when(repository.save(actual)).thenReturn(actual);

        Empleado resultado = service.actualizar(1L, cambios);

        assertThat(resultado).isSameAs(actual);
        assertThat(actual.getNombre()).isEqualTo("Ana María");
        assertThat(actual.getCargo()).isEqualTo("Jefa");
        assertThat(actual.getSalario()).isEqualTo(9000.0);
        assertThat(actual.getEmail()).isEqualTo("ana@empresa.test");
    }

    @Test
    void eliminar_cuandoExiste_borraElEmpleado() {
        Empleado empleado = empleado("Luis", "Rojas");
        when(repository.findById(2L)).thenReturn(Optional.of(empleado));

        service.eliminar(2L);

        verify(repository).delete(empleado);
    }

    @Test
    void eliminar_cuandoNoExiste_noIntentaBorrar() {
        when(repository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.eliminar(2L)).isInstanceOf(ResponseStatusException.class);
        verify(repository, never()).delete(any());
    }

    private Empleado empleado(String nombre, String apellido) {
        return new Empleado(nombre, apellido, "Analista", 5000.0, nombre.toLowerCase() + "@test.com");
    }
}
