package br.com.rogrs.safh.repository;

import br.com.rogrs.safh.domain.Medicamentos;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Medicamentos entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MedicamentosRepository extends JpaRepository<Medicamentos, Long> {

}
