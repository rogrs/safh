package br.com.rogrs.repository;

import br.com.rogrs.domain.Medicos;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Medicos entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MedicosRepository extends JpaRepository<Medicos, Long> {}
