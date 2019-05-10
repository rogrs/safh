package br.com.rogrs.repository.search;

import br.com.rogrs.domain.Prescricoes;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Prescricoes} entity.
 */
public interface PrescricoesSearchRepository extends ElasticsearchRepository<Prescricoes, Long> {
}
