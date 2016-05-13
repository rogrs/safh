package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Dietas;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Dietas entity.
 */
@SuppressWarnings("unused")
public interface DietasRepository extends JpaRepository<Dietas,Long> {

}
