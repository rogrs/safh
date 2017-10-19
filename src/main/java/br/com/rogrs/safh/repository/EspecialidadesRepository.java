package br.com.rogrs.safh.repository;

import br.com.rogrs.safh.domain.Especialidades;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Especialidades entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EspecialidadesRepository extends JpaRepository<Especialidades, Long> {

}
