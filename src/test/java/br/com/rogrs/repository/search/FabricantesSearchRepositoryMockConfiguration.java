package br.com.rogrs.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link FabricantesSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class FabricantesSearchRepositoryMockConfiguration {

    @MockBean
    private FabricantesSearchRepository mockFabricantesSearchRepository;

}
