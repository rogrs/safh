package br.com.rogrs.safh.repository.search;

import br.com.rogrs.safh.domain.Dietas;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Dietas entity.
 */
public interface DietasSearchRepository extends ElasticsearchRepository<Dietas, Long> {
}
