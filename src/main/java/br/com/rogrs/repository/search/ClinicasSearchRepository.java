package br.com.rogrs.repository.search;

import br.com.rogrs.domain.Clinicas;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Clinicas} entity.
 */
public interface ClinicasSearchRepository extends ElasticsearchRepository<Clinicas, Long> {
}
