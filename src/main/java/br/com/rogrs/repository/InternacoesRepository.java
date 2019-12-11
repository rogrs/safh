package br.com.rogrs.repository;
import br.com.rogrs.domain.Internacoes;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the Internacoes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InternacoesRepository extends MongoRepository<Internacoes, String> {

}
