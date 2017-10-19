package br.com.rogrs.safh.repository;

import br.com.rogrs.safh.domain.Dietas;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Dietas entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DietasRepository extends JpaRepository<Dietas, Long> {

}
