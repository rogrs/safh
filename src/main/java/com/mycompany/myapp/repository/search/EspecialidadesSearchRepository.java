package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Especialidades;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Especialidades entity.
 */
public interface EspecialidadesSearchRepository extends ElasticsearchRepository<Especialidades, Long> {
}
