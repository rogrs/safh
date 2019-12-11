package br.com.rogrs.repository.search;
import br.com.rogrs.domain.Posologias;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Posologias} entity.
 */
public interface PosologiasSearchRepository extends ElasticsearchRepository<Posologias, String> {
}
