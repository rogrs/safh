package br.com.rogrs.safh.repository.search;

import br.com.rogrs.safh.domain.Especialidades;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Especialidades entity.
 */
public interface EspecialidadesSearchRepository extends ElasticsearchRepository<Especialidades, Long> {
}
