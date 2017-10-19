package br.com.rogrs.safh.repository;

import br.com.rogrs.safh.domain.Posologias;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Posologias entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PosologiasRepository extends JpaRepository<Posologias, Long> {

}
