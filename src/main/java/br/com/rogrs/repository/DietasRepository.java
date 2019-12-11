package br.com.rogrs.repository;
import br.com.rogrs.domain.Dietas;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the Dietas entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DietasRepository extends MongoRepository<Dietas, String> {

}
