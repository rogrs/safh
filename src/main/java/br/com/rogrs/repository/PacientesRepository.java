package br.com.rogrs.repository;

import br.com.rogrs.domain.Pacientes;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Pacientes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PacientesRepository extends JpaRepository<Pacientes, Long> {

}
