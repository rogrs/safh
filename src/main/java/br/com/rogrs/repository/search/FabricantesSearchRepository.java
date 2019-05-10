package br.com.rogrs.repository.search;

import br.com.rogrs.domain.Fabricantes;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Fabricantes} entity.
 */
public interface FabricantesSearchRepository extends ElasticsearchRepository<Fabricantes, Long> {
}
