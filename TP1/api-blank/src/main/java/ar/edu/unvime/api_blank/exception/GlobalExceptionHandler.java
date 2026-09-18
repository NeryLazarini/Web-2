package ar.edu.unvime.api_blank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Caso 1: Favorito inexistente -> Devuelve 404 Not Found
    @ExceptionHandler(FavoritoNoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> manejarFavoritoNoEncontrado(FavoritoNoEncontradoException ex) {
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("error", ex.getMessage());
        return respuesta;
    }

    // Caso 2: Validación fallida -> Devuelve 400 Bad Request y detalla el campo
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> manejarErroresDeValidacion(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();
        // Extrae qué campo falló y el mensaje ("La nota personal no puede estar vacía", etc.)
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errores.put(error.getField(), error.getDefaultMessage());
        }
        return errores;
    }

    // Caso 3: Falla al consumir la API externa (timeout o caída) -> Devuelve 500 Internal Server Error
    @ExceptionHandler(RestClientException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> manejarErrorApiExterna(RestClientException ex) {
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("error", "Ocurrió un problema al comunicarse con el catálogo de productos.");
        return respuesta;
    }
}