package ar.edu.unvime.api_blank.mapper;

import ar.edu.unvime.api_blank.domain.Favorito;
import ar.edu.unvime.api_blank.dto.FavoritoInputDTO;
import ar.edu.unvime.api_blank.dto.FavoritoOutputDTO;
import org.springframework.stereotype.Component;

@Component
public class FavoritoMapper {
    public Favorito toEntity(FavoritoInputDTO input) {
        Favorito favorito = new Favorito();
        favorito.setProductoId(input.getProductoId());
        favorito.setNotaPersonal(input.getNotaPersonal());
        return favorito;
    }
    public FavoritoOutputDTO toOutputDTO(Favorito favorito) {
        FavoritoOutputDTO dto = new FavoritoOutputDTO();
        dto.setId(favorito.getId());
        dto.setProductoId(favorito.getProductoId());
        dto.setNotaPersonal(favorito.getNotaPersonal());
        dto.setFechaAgregado(favorito.getFechaAgregado());
        return dto;
    }
}