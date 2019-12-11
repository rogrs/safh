package br.com.rogrs.repository.search;
import br.com.rogrs.domain.Medicos;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Medicos} entity.
 */
public interface MedicosSearchRepository extends ElasticsearchRepository<Medicos, String> {
}
