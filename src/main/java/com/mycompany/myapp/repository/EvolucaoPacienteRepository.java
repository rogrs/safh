package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.EvolucaoPaciente;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the EvolucaoPaciente entity.
 */
@SuppressWarnings("unused")
public interface EvolucaoPacienteRepository extends JpaRepository<EvolucaoPaciente,Long> {

}
