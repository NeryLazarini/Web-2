package ar.edu.unvime.api_blank.web;

import ar.edu.unvime.api_blank.dto.LoginInputDTO;
import ar.edu.unvime.api_blank.dto.LoginOutputDTO;
import ar.edu.unvime.api_blank.dto.RegistroInputDTO;
import ar.edu.unvime.api_blank.dto.UsuarioOutputDTO;
import ar.edu.unvime.api_blank.service.AuthService;
import ar.edu.unvime.api_blank.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Autenticacion", description = "Endpoints publicos para registro de usuarios e inicio de sesion con JWT")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;
    private final AuthService authService;

    public AuthController(UsuarioService usuarioService, AuthService authService) {
        this.usuarioService = usuarioService;
        this.authService = authService;
    }

    @Operation(summary = "Registrar usuario", description = "Crea un nuevo usuario con rol USER y devuelve sus datos basicos.")
    @PostMapping("/registro")
    public ResponseEntity<UsuarioOutputDTO> registrar(@Valid @RequestBody RegistroInputDTO dto) {
        UsuarioOutputDTO creado = usuarioService.registrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(summary = "Iniciar sesion", description = "Valida credenciales y devuelve un token JWT para usar en el header Authorization: Bearer <token>.")
    @PostMapping("/login")
    public ResponseEntity<LoginOutputDTO> login(@Valid @RequestBody LoginInputDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }
}