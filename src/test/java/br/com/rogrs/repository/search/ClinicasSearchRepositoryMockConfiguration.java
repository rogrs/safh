package br.com.rogrs.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link ClinicasSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ClinicasSearchRepositoryMockConfiguration {

    @MockBean
    private ClinicasSearchRepository mockClinicasSearchRepository;

}
