package br.com.rogrs.repository;
import br.com.rogrs.domain.Fabricantes;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the Fabricantes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FabricantesRepository extends MongoRepository<Fabricantes, String> {

}
