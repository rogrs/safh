package br.com.rogrs.repository;

import br.com.rogrs.domain.Internacoes;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Internacoes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InternacoesRepository extends JpaRepository<Internacoes, Long> {}
