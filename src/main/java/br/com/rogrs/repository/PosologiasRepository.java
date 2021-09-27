package br.com.rogrs.repository;

import br.com.rogrs.domain.Posologias;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Posologias entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PosologiasRepository extends JpaRepository<Posologias, Long> {}
