package br.com.rogrs.repository;
import br.com.rogrs.domain.InternacoesDetalhes;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the InternacoesDetalhes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InternacoesDetalhesRepository extends MongoRepository<InternacoesDetalhes, String> {

}
