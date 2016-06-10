package br.com.rogrs.safh.repository;

import br.com.rogrs.safh.domain.Internacoes;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Internacoes entity.
 */
@SuppressWarnings("unused")
public interface InternacoesRepository extends JpaRepository<Internacoes,Long> {

}
