package br.com.rogrs.safh.repository;

import br.com.rogrs.safh.domain.InternacoesDetalhes;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the InternacoesDetalhes entity.
 */
@SuppressWarnings("unused")
public interface InternacoesDetalhesRepository extends JpaRepository<InternacoesDetalhes,Long> {

}
