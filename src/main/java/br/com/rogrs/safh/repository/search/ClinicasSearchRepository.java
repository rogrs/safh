package br.com.rogrs.safh.repository.search;

import br.com.rogrs.safh.domain.Clinicas;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Clinicas entity.
 */
public interface ClinicasSearchRepository extends ElasticsearchRepository<Clinicas, Long> {
}
