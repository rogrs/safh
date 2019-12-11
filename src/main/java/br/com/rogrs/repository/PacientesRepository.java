package br.com.rogrs.repository;
import br.com.rogrs.domain.Pacientes;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the Pacientes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PacientesRepository extends MongoRepository<Pacientes, String> {

}
