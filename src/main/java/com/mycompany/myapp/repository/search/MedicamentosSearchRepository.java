package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Medicamentos;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Medicamentos entity.
 */
public interface MedicamentosSearchRepository extends ElasticsearchRepository<Medicamentos, Long> {
}
