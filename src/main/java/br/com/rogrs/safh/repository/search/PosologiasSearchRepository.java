package br.com.rogrs.safh.repository.search;

import br.com.rogrs.safh.domain.Posologias;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Posologias entity.
 */
public interface PosologiasSearchRepository extends ElasticsearchRepository<Posologias, Long> {
}
