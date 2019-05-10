package br.com.rogrs.repository;

import br.com.rogrs.domain.Especialidades;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Especialidades entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EspecialidadesRepository extends JpaRepository<Especialidades, Long> {

}
