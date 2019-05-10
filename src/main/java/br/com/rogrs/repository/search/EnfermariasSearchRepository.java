package br.com.rogrs.repository.search;

import br.com.rogrs.domain.Enfermarias;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Enfermarias} entity.
 */
public interface EnfermariasSearchRepository extends ElasticsearchRepository<Enfermarias, Long> {
}
