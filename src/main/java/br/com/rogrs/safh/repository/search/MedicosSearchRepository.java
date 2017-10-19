package br.com.rogrs.safh.repository.search;

import br.com.rogrs.safh.domain.Medicos;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Medicos entity.
 */
public interface MedicosSearchRepository extends ElasticsearchRepository<Medicos, Long> {
}
