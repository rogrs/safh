package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Posologias;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Posologias entity.
 */
public interface PosologiasSearchRepository extends ElasticsearchRepository<Posologias, Long> {
}
