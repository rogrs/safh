package br.com.rogrs.safh.repository;

import br.com.rogrs.safh.domain.Especialidades;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Especialidades entity.
 */
@SuppressWarnings("unused")
public interface EspecialidadesRepository extends JpaRepository<Especialidades,Long> {

}
