package ar.edu.unvime.api_blank.service;

import ar.edu.unvime.api_blank.domain.Favorito;
import ar.edu.unvime.api_blank.dto.FavoritoInputDTO;
import ar.edu.unvime.api_blank.dto.FavoritoOutputDTO;
import ar.edu.unvime.api_blank.exception.FavoritoNoEncontradoException;
import ar.edu.unvime.api_blank.mapper.FavoritoMapper;
import ar.edu.unvime.api_blank.repository.FavoritoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoritoService {
    
    private final FavoritoRepository repository;
    private final FavoritoMapper mapper;

    public FavoritoService(FavoritoRepository repository, FavoritoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public FavoritoOutputDTO crear(FavoritoInputDTO input) {
        Favorito favorito = mapper.toEntity(input);
        Favorito guardado = repository.save(favorito);
        return mapper.toOutputDTO(guardado);
    }

    public List<FavoritoOutputDTO> listarTodos() {
        return repository.findAll().stream()
                .map(favorito -> mapper.toOutputDTO(favorito))
                .collect(Collectors.toList());
    }

    public FavoritoOutputDTO obtenerPorId(Long id) {
        Favorito favorito = repository.findById(id)
                .orElseThrow(() -> new FavoritoNoEncontradoException("No se encontró el favorito con ID: " + id));
        return mapper.toOutputDTO(favorito);
    }

    public FavoritoOutputDTO actualizar(Long id, FavoritoInputDTO input) {
        Favorito existente = repository.findById(id)
                .orElseThrow(() -> new FavoritoNoEncontradoException("No se encontró el favorito con ID: " + id));
        
        existente.setProductoId(input.getProductoId());
        existente.setNotaPersonal(input.getNotaPersonal());
        
        Favorito actualizado = repository.save(existente);
        return mapper.toOutputDTO(actualizado);
    }

    public void eliminar(Long id) {
        if (!repository.deleteById(id)) {
            throw new FavoritoNoEncontradoException("No se encontró el favorito con ID: " + id);
        }
    }
}