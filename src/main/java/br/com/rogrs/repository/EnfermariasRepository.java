package br.com.rogrs.repository;
import br.com.rogrs.domain.Enfermarias;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the Enfermarias entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EnfermariasRepository extends MongoRepository<Enfermarias, String> {

}
