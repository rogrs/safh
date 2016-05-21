package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.EvolucaoPaciente;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the EvolucaoPaciente entity.
 */
public interface EvolucaoPacienteSearchRepository extends ElasticsearchRepository<EvolucaoPaciente, Long> {
}
