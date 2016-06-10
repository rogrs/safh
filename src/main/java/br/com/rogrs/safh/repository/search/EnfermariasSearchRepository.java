package br.com.rogrs.safh.repository.search;

import br.com.rogrs.safh.domain.Enfermarias;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Enfermarias entity.
 */
public interface EnfermariasSearchRepository extends ElasticsearchRepository<Enfermarias, Long> {
}
