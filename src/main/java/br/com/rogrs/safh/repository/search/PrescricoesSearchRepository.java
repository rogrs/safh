package br.com.rogrs.safh.repository.search;

import br.com.rogrs.safh.domain.Prescricoes;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Prescricoes entity.
 */
public interface PrescricoesSearchRepository extends ElasticsearchRepository<Prescricoes, Long> {
}
