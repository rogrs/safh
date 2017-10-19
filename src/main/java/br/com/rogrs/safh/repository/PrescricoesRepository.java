package br.com.rogrs.safh.repository;

import br.com.rogrs.safh.domain.Prescricoes;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Prescricoes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PrescricoesRepository extends JpaRepository<Prescricoes, Long> {

}
