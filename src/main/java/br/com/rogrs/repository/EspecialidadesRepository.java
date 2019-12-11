package br.com.rogrs.repository;
import br.com.rogrs.domain.Especialidades;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the Especialidades entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EspecialidadesRepository extends MongoRepository<Especialidades, String> {

}
