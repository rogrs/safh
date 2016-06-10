package br.com.rogrs.safh.repository;

import br.com.rogrs.safh.domain.Fabricantes;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Fabricantes entity.
 */
@SuppressWarnings("unused")
public interface FabricantesRepository extends JpaRepository<Fabricantes,Long> {

}
