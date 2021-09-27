package br.com.rogrs.repository;

import br.com.rogrs.domain.Dietas;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Dietas entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DietasRepository extends JpaRepository<Dietas, Long> {}
