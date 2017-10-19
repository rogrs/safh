package br.com.rogrs.safh.repository.search;

import br.com.rogrs.safh.domain.Internacoes;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Internacoes entity.
 */
public interface InternacoesSearchRepository extends ElasticsearchRepository<Internacoes, Long> {
}
