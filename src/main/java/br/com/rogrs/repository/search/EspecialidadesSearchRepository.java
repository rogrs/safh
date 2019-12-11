package br.com.rogrs.repository.search;
import br.com.rogrs.domain.Especialidades;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Especialidades} entity.
 */
public interface EspecialidadesSearchRepository extends ElasticsearchRepository<Especialidades, String> {
}
