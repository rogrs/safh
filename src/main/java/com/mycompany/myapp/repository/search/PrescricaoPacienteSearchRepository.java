package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.PrescricaoPaciente;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the PrescricaoPaciente entity.
 */
public interface PrescricaoPacienteSearchRepository extends ElasticsearchRepository<PrescricaoPaciente, Long> {
}
