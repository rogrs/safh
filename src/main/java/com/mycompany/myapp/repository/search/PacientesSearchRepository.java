package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Pacientes;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Pacientes entity.
 */
public interface PacientesSearchRepository extends ElasticsearchRepository<Pacientes, Long> {
}
