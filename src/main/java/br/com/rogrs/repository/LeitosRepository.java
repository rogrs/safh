package br.com.rogrs.repository;
import br.com.rogrs.domain.Leitos;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the Leitos entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LeitosRepository extends MongoRepository<Leitos, String> {

}
