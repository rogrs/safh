package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.InternacaoPaciente;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the InternacaoPaciente entity.
 */
public interface InternacaoPacienteSearchRepository extends ElasticsearchRepository<InternacaoPaciente, Long> {
}
