package br.com.rogrs.repository.search;
import br.com.rogrs.domain.Medicamentos;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Medicamentos} entity.
 */
public interface MedicamentosSearchRepository extends ElasticsearchRepository<Medicamentos, String> {
}
