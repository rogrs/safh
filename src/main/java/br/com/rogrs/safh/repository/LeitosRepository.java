package br.com.rogrs.safh.repository;

import br.com.rogrs.safh.domain.Leitos;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Leitos entity.
 */
@SuppressWarnings("unused")
public interface LeitosRepository extends JpaRepository<Leitos,Long> {

}
