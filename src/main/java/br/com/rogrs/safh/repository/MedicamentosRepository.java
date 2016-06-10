package br.com.rogrs.safh.repository;

import br.com.rogrs.safh.domain.Medicamentos;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Medicamentos entity.
 */
@SuppressWarnings("unused")
public interface MedicamentosRepository extends JpaRepository<Medicamentos,Long> {

}
