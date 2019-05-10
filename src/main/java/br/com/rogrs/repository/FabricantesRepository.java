package br.com.rogrs.repository;

import br.com.rogrs.domain.Fabricantes;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Fabricantes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FabricantesRepository extends JpaRepository<Fabricantes, Long> {

}
