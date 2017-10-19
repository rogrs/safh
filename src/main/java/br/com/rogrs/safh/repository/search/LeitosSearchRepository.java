package br.com.rogrs.safh.repository.search;

import br.com.rogrs.safh.domain.Leitos;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Leitos entity.
 */
public interface LeitosSearchRepository extends ElasticsearchRepository<Leitos, Long> {
}
