package br.com.rogrs.safh.repository;

import br.com.rogrs.safh.domain.Leitos;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Leitos entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LeitosRepository extends JpaRepository<Leitos, Long> {

}
