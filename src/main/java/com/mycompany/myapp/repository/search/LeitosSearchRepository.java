package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Leitos;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Leitos entity.
 */
public interface LeitosSearchRepository extends ElasticsearchRepository<Leitos, Long> {
}
