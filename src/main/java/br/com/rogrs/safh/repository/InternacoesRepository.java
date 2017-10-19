package br.com.rogrs.safh.repository;

import br.com.rogrs.safh.domain.Internacoes;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Internacoes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InternacoesRepository extends JpaRepository<Internacoes, Long> {

}
