package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Medicos;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Medicos entity.
 */
public interface MedicosSearchRepository extends ElasticsearchRepository<Medicos, Long> {
}
