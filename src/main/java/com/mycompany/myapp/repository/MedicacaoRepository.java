package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Medicacao;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Medicacao entity.
 */
@SuppressWarnings("unused")
public interface MedicacaoRepository extends JpaRepository<Medicacao,Long> {

}
