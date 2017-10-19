package br.com.rogrs.safh.repository;

import br.com.rogrs.safh.domain.Pacientes;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Pacientes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PacientesRepository extends JpaRepository<Pacientes, Long> {

}
