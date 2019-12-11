package br.com.rogrs.repository.search;
import br.com.rogrs.domain.Pacientes;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Pacientes} entity.
 */
public interface PacientesSearchRepository extends ElasticsearchRepository<Pacientes, String> {
}
