package br.com.rogrs.repository;
import br.com.rogrs.domain.Medicamentos;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the Medicamentos entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MedicamentosRepository extends MongoRepository<Medicamentos, String> {

}
