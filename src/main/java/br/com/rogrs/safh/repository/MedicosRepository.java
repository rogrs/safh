package br.com.rogrs.safh.repository;

import br.com.rogrs.safh.domain.Medicos;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Medicos entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MedicosRepository extends JpaRepository<Medicos, Long> {

}
