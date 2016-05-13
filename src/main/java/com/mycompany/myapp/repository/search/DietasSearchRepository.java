package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Dietas;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Dietas entity.
 */
public interface DietasSearchRepository extends ElasticsearchRepository<Dietas, Long> {
}
