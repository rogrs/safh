package br.com.rogrs.safh.repository.search;

import br.com.rogrs.safh.domain.Fabricantes;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Fabricantes entity.
 */
public interface FabricantesSearchRepository extends ElasticsearchRepository<Fabricantes, Long> {
}
