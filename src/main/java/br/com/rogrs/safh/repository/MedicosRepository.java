package br.com.rogrs.safh.repository;

import br.com.rogrs.safh.domain.Medicos;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Medicos entity.
 */
@SuppressWarnings("unused")
public interface MedicosRepository extends JpaRepository<Medicos,Long> {

}
