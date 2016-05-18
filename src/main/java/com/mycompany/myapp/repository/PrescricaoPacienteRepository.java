package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.PrescricaoPaciente;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PrescricaoPaciente entity.
 */
@SuppressWarnings("unused")
public interface PrescricaoPacienteRepository extends JpaRepository<PrescricaoPaciente,Long> {

}
