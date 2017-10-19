package br.com.rogrs.safh.repository;

import br.com.rogrs.safh.domain.InternacoesDetalhes;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the InternacoesDetalhes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InternacoesDetalhesRepository extends JpaRepository<InternacoesDetalhes, Long> {

}
