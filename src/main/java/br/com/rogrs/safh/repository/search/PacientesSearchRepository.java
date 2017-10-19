package br.com.rogrs.safh.repository.search;

import br.com.rogrs.safh.domain.Pacientes;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Pacientes entity.
 */
public interface PacientesSearchRepository extends ElasticsearchRepository<Pacientes, Long> {
}
