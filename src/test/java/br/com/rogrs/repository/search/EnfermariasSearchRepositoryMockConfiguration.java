package br.com.rogrs.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link EnfermariasSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class EnfermariasSearchRepositoryMockConfiguration {

    @MockBean
    private EnfermariasSearchRepository mockEnfermariasSearchRepository;

}
