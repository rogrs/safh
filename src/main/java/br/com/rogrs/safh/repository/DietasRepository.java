package br.com.rogrs.safh.repository;

import br.com.rogrs.safh.domain.Dietas;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Dietas entity.
 */
@SuppressWarnings("unused")
public interface DietasRepository extends JpaRepository<Dietas,Long> {

}
