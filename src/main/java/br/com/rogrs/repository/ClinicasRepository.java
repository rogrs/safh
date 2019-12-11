package br.com.rogrs.repository;
import br.com.rogrs.domain.Clinicas;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the Clinicas entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClinicasRepository extends MongoRepository<Clinicas, String> {

}
