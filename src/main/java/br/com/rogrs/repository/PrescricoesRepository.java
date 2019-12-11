package br.com.rogrs.repository;
import br.com.rogrs.domain.Prescricoes;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the Prescricoes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PrescricoesRepository extends MongoRepository<Prescricoes, String> {

}
