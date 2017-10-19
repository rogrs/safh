package br.com.rogrs.safh.repository.search;

import br.com.rogrs.safh.domain.InternacoesDetalhes;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the InternacoesDetalhes entity.
 */
public interface InternacoesDetalhesSearchRepository extends ElasticsearchRepository<InternacoesDetalhes, Long> {
}
