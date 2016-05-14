package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Enfermarias;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Enfermarias entity.
 */
@SuppressWarnings("unused")
public interface EnfermariasRepository extends JpaRepository<Enfermarias,Long> {

}
