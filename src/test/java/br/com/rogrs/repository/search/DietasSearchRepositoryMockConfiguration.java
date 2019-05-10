package br.com.rogrs.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link DietasSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class DietasSearchRepositoryMockConfiguration {

    @MockBean
    private DietasSearchRepository mockDietasSearchRepository;

}
