package br.com.rogrs.safh.repository;

import br.com.rogrs.safh.domain.Enfermarias;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Enfermarias entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EnfermariasRepository extends JpaRepository<Enfermarias, Long> {

}
