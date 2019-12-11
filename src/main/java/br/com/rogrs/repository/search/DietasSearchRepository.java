package br.com.rogrs.repository.search;
import br.com.rogrs.domain.Dietas;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Dietas} entity.
 */
public interface DietasSearchRepository extends ElasticsearchRepository<Dietas, String> {
}
