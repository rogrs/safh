package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Pacientes;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Pacientes entity.
 */
@SuppressWarnings("unused")
public interface PacientesRepository extends JpaRepository<Pacientes,Long> {

}
