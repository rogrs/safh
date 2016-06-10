package br.com.rogrs.safh.repository.search;

import br.com.rogrs.safh.domain.Medicamentos;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Medicamentos entity.
 */
public interface MedicamentosSearchRepository extends ElasticsearchRepository<Medicamentos, Long> {
}
