package ar.edu.unvime.api_blank.web;

import ar.edu.unvime.api_blank.domain.Rol;
import ar.edu.unvime.api_blank.repository.UsuarioEntity;
import ar.edu.unvime.api_blank.repository.UsuarioJpaRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class ListaControllerIT {

    @Container
    static PostgreSQLContainer postgres =
            new PostgreSQLContainer(DockerImageName.parse("postgres:17"));

    @DynamicPropertySource
    static void propiedadesDeConexion(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioJpaRepository usuarioJpaRepository;

    @Test
    void login_credencialesValidas_devuelve200ConToken() throws Exception {
        registrar("valido@test.com", "12345678");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email": "valido@test.com", "password": "12345678"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void login_credencialesInvalidas_devuelve401() throws Exception {
        registrar("invalido@test.com", "12345678");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email": "invalido@test.com", "password": "clave-mal"}
                                """))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void listarListas_sinToken_devuelve401() throws Exception {
        mockMvc.perform(get("/api/listas"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void obtenerLista_propia_devuelve200() throws Exception {
        String tokenAna = registrarYLoguear("ana.propia@test.com", "12345678");
        long listaId = crearLista(tokenAna, "Lista de Ana");

        mockMvc.perform(get("/api/listas/{id}", listaId)
                        .header("Authorization", "Bearer " + tokenAna))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Lista de Ana"));
    }

    @Test
    void obtenerLista_ajena_devuelve403() throws Exception {
        String tokenAna = registrarYLoguear("ana.duena@test.com", "12345678");
        long listaId = crearLista(tokenAna, "Lista privada");
        String tokenBob = registrarYLoguear("bob.intruso@test.com", "12345678");

        mockMvc.perform(get("/api/listas/{id}", listaId)
                        .header("Authorization", "Bearer " + tokenBob))
                .andExpect(status().isForbidden());
    }

    @Test
    void obtenerLista_adminSobreListaAjena_devuelve200() throws Exception {
        String tokenAna = registrarYLoguear("ana.usuario@test.com", "12345678");
        long listaId = crearLista(tokenAna, "Lista de Ana para Admin");

        // Registramos al admin, lo promovemos en la base y nos logueamos para obtener el JWT con rol ADMIN
        registrar("jefe.admin@test.com", "12345678");
        UsuarioEntity admin = usuarioJpaRepository.findByEmail("jefe.admin@test.com").orElseThrow();
        admin.setRol(Rol.ADMIN);
        usuarioJpaRepository.save(admin);

        String tokenAdmin = loguear("jefe.admin@test.com", "12345678");

        mockMvc.perform(get("/api/listas/{id}", listaId)
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Lista de Ana para Admin"));
    }

    // --- Metodos auxiliares ---

    private void registrar(String email, String password) throws Exception {
        mockMvc.perform(post("/api/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email": "%s", "password": "%s"}
                                """.formatted(email, password)))
                .andExpect(status().isCreated());
    }

    private String loguear(String email, String password) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email": "%s", "password": "%s"}
                                """.formatted(email, password)))
                .andExpect(status().isOk())
                .andReturn();

        return JsonPath.read(result.getResponse().getContentAsString(), "$.token");
    }

    private String registrarYLoguear(String email, String password) throws Exception {
        registrar(email, password);
        return loguear(email, password);
    }

    private long crearLista(String token, String nombre) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/listas")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombre": "%s"}
                                """.formatted(nombre)))
                .andExpect(status().isCreated())
                .andReturn();

        Number id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
        return id.longValue();
    }
}