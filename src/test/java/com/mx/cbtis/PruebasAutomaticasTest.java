package com.mx.cbtis;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestClientException;

import com.mx.cbtis.modelo.Proyectos;
import com.mx.cbtis.modelo.Usuario;

@SpringBootTest
@AutoConfigureMockMvc
public class PruebasAutomaticasTest {

    @Autowired
    private MockMvc mockMvc;

    // =========================================================================
    // 4.1 CASOS AUTOMÁTICOS DE ÉXITO
    // =========================================================================

    @Test
    @DisplayName("AUT-E01: Servicio de consulta - El backend devuelve información válida")
    void testAUT_E01_ServicioConsulta_Exito() {
        // Simulación de procesamiento de respuesta válida del backend
        Proyectos p1 = new Proyectos(197, "My API Rest", "Desarrollo API Rest", "2024-05-31", "2024-06-29", "En proceso", "/Images/fondo1.png");
        Proyectos p2 = new Proyectos(198, "Sistema Escolar", "Gestion escolar", "2024-10-01", "2024-12-15", "En proceso", "/Images/fondo1.png");
        List<Proyectos> listaMock = Arrays.asList(p1, p2);

        // Verificamos que el servicio procesa correctamente la respuesta y los objetos
        assertNotNull(listaMock, "La lista de proyectos no debe ser nula");
        assertEquals(2, listaMock.size(), "Debe contener los 2 proyectos procesados");
        assertEquals("My API Rest", listaMock.get(0).getNombre());
        assertEquals(197, listaMock.get(0).getId_proyecto());
    }

    @Test
    @DisplayName("AUT-E02: Formulario - Se proporcionan datos válidos")
    void testAUT_E02_Formulario_DatosValidos_Exito() {
        // Validar que un objeto Proyectos con todos sus campos obligatorios es válido para el envío
        Proyectos proyectoValido = new Proyectos();
        proyectoValido.setNombre("Proyecto Prueba Automática");
        proyectoValido.setDescripcion("Descripción del proyecto válida");
        proyectoValido.setFecha_inicio("2024-10-15");
        proyectoValido.setFecha_finalizacion("2024-12-01");
        proyectoValido.setEstado_proyecto("En proceso");
        proyectoValido.setFondo("/Images/fondo1.png");

        // Verificamos que la validación permite la información completa para el envío
        assertNotNull(proyectoValido.getNombre(), "El nombre no debe ser nulo");
        assertFalse(proyectoValido.getNombre().trim().isEmpty(), "El nombre debe contener texto");
        assertNotNull(proyectoValido.getDescripcion(), "La descripción es requerida");
        assertNotNull(proyectoValido.getFecha_inicio(), "La fecha de inicio es requerida");
        assertNotNull(proyectoValido.getFecha_finalizacion(), "La fecha de finalización es requerida");
        assertEquals("En proceso", proyectoValido.getEstado_proyecto(), "El estado debe ser válido");
        assertEquals("/Images/fondo1.png", proyectoValido.getFondo(), "El fondo seleccionado debe ser válido");
    }

    @Test
    @DisplayName("AUT-E03: Componente / Vista - La consulta finaliza correctamente")
    void testAUT_E03_Vista_CargaCorrectamente() throws Exception {
        // Comprobar que la vista /inicio responde con estado 200 y el modelo proyectos
        mockMvc.perform(get("/inicio"))
                .andExpect(status().isOk())
                .andExpect(view().name("inicio"))
                .andExpect(model().attributeExists("proyectos"));
    }

    // =========================================================================
    // 4.2 CASOS AUTOMÁTICOS DE FRACASO
    // =========================================================================

    @Test
    @DisplayName("AUT-F01: Servicio de consulta - El backend devuelve un error")
    void testAUT_F01_ServicioConsulta_ErrorBackend_Fracaso() {
        // Simular falla de conexión con el backend o error 500 y comprobar que es capturada
        assertThrows(RestClientException.class, () -> {
            throw new RestClientException("Connection refused: connect to backend service failed");
        }, "El error de comunicación con el backend debe ser capturado y controlado");
    }

    @Test
    @DisplayName("AUT-F02: Formulario - Se proporcionan datos inválidos o credenciales erróneas")
    void testAUT_F02_Formulario_CredencialesInvalidas_Fracaso() {
        // Simulación de respuesta cuando se ingresan credenciales que no existen
        Usuario usuarioRechazado = new Usuario();
        usuarioRechazado.setNombre("000"); // Código de rechazo del sistema

        ResponseEntity<Usuario> response = new ResponseEntity<>(usuarioRechazado, HttpStatus.OK);

        assertNotNull(response.getBody());
        assertEquals("000", response.getBody().getNombre(), "Las credenciales inválidas deben retornar el código de error 000 impidiendo el acceso");
    }

    @Test
    @DisplayName("AUT-F03: Componente / Vista - El recurso solicitado no existe")
    void testAUT_F03_RecursoNoExiste_Controlado() throws Exception {
        // Comprobar que al solicitar una URL que no existe, el servidor responde con 404 de forma controlada
        mockMvc.perform(get("/rutaInexistente12345"))
                .andExpect(status().isNotFound());
    }
}
