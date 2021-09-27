package br.com.rogrs.repository;

import br.com.rogrs.domain.Enfermarias;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Enfermarias entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EnfermariasRepository extends JpaRepository<Enfermarias, Long> {}
