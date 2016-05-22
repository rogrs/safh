package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.InternacaoPaciente;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the InternacaoPaciente entity.
 */
@SuppressWarnings("unused")
public interface InternacaoPacienteRepository extends JpaRepository<InternacaoPaciente,Long> {

}
