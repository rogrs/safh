package br.com.rogrs.safh.repository;

import br.com.rogrs.safh.domain.Pacientes;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Pacientes entity.
 */
@SuppressWarnings("unused")
public interface PacientesRepository extends JpaRepository<Pacientes,Long> {

}
