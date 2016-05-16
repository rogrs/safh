package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Medicamentos;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Medicamentos entity.
 */
@SuppressWarnings("unused")
public interface MedicamentosRepository extends JpaRepository<Medicamentos,Long> {

}
