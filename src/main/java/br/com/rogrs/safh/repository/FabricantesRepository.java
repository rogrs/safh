package br.com.rogrs.safh.repository;

import br.com.rogrs.safh.domain.Fabricantes;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Fabricantes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FabricantesRepository extends JpaRepository<Fabricantes, Long> {

}
