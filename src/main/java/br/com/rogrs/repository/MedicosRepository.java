package br.com.rogrs.repository;
import br.com.rogrs.domain.Medicos;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the Medicos entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MedicosRepository extends MongoRepository<Medicos, String> {

}
