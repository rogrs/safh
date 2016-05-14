package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Leitos;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Leitos entity.
 */
@SuppressWarnings("unused")
public interface LeitosRepository extends JpaRepository<Leitos,Long> {

}
