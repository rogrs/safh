package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Fabricantes;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Fabricantes entity.
 */
@SuppressWarnings("unused")
public interface FabricantesRepository extends JpaRepository<Fabricantes,Long> {

}
