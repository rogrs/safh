package br.com.rogrs.safh.repository;

import br.com.rogrs.safh.domain.Posologias;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Posologias entity.
 */
@SuppressWarnings("unused")
public interface PosologiasRepository extends JpaRepository<Posologias,Long> {

}
