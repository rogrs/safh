package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Fabricantes;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Fabricantes entity.
 */
public interface FabricantesSearchRepository extends ElasticsearchRepository<Fabricantes, Long> {
}
