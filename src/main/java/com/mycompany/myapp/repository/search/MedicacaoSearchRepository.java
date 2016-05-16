package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Medicacao;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Medicacao entity.
 */
public interface MedicacaoSearchRepository extends ElasticsearchRepository<Medicacao, Long> {
}
