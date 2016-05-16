package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Posologias;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Posologias entity.
 */
@SuppressWarnings("unused")
public interface PosologiasRepository extends JpaRepository<Posologias,Long> {

}
