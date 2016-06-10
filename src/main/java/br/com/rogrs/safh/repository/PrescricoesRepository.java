package br.com.rogrs.safh.repository;

import br.com.rogrs.safh.domain.Prescricoes;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Prescricoes entity.
 */
@SuppressWarnings("unused")
public interface PrescricoesRepository extends JpaRepository<Prescricoes,Long> {

}
