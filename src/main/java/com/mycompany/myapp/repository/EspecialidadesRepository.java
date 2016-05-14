package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Especialidades;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Especialidades entity.
 */
@SuppressWarnings("unused")
public interface EspecialidadesRepository extends JpaRepository<Especialidades,Long> {

}
