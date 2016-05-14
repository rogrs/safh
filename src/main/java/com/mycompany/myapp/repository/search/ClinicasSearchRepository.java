package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Clinicas;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Clinicas entity.
 */
public interface ClinicasSearchRepository extends ElasticsearchRepository<Clinicas, Long> {
}
