package br.com.rogrs.repository;

import br.com.rogrs.domain.InternacoesDetalhes;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the InternacoesDetalhes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InternacoesDetalhesRepository extends JpaRepository<InternacoesDetalhes, Long> {

}
