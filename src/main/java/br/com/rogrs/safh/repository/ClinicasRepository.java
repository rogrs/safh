package br.com.rogrs.safh.repository;

import br.com.rogrs.safh.domain.Clinicas;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Clinicas entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClinicasRepository extends JpaRepository<Clinicas, Long> {

}
