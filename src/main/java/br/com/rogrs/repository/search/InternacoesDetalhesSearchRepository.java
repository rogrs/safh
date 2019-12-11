package br.com.rogrs.repository.search;
import br.com.rogrs.domain.InternacoesDetalhes;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link InternacoesDetalhes} entity.
 */
public interface InternacoesDetalhesSearchRepository extends ElasticsearchRepository<InternacoesDetalhes, String> {
}
