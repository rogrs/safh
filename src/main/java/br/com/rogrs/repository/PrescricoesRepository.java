package br.com.rogrs.repository;

import br.com.rogrs.domain.Prescricoes;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Prescricoes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PrescricoesRepository extends JpaRepository<Prescricoes, Long> {

}
