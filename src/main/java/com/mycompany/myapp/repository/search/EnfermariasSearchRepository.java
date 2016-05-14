package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Enfermarias;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Enfermarias entity.
 */
public interface EnfermariasSearchRepository extends ElasticsearchRepository<Enfermarias, Long> {
}
