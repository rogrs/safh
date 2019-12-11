package br.com.rogrs.repository;
import br.com.rogrs.domain.Posologias;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the Posologias entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PosologiasRepository extends MongoRepository<Posologias, String> {

}
