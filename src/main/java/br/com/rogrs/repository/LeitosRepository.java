package br.com.rogrs.repository;

import br.com.rogrs.domain.Leitos;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Leitos entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LeitosRepository extends JpaRepository<Leitos, Long> {

}
