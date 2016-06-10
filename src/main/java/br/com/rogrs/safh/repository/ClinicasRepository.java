package br.com.rogrs.safh.repository;

import br.com.rogrs.safh.domain.Clinicas;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Clinicas entity.
 */
@SuppressWarnings("unused")
public interface ClinicasRepository extends JpaRepository<Clinicas,Long> {

}
