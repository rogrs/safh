package br.com.rogrs.repository.search;

import br.com.rogrs.domain.Internacoes;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Internacoes} entity.
 */
public interface InternacoesSearchRepository extends ElasticsearchRepository<Internacoes, Long> {
}
