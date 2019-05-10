package br.com.rogrs.repository;

import br.com.rogrs.domain.Clinicas;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Clinicas entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClinicasRepository extends JpaRepository<Clinicas, Long> {

}
