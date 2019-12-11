package br.com.rogrs.repository.search;
import br.com.rogrs.domain.Leitos;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Leitos} entity.
 */
public interface LeitosSearchRepository extends ElasticsearchRepository<Leitos, String> {
}
