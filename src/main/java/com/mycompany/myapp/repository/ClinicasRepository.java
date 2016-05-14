package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Clinicas;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Clinicas entity.
 */
@SuppressWarnings("unused")
public interface ClinicasRepository extends JpaRepository<Clinicas,Long> {

}
