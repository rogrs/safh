package br.com.rogrs.web.rest;

import br.com.rogrs.SafhApp;
import br.com.rogrs.domain.Fabricantes;
import br.com.rogrs.repository.FabricantesRepository;
import br.com.rogrs.repository.search.FabricantesSearchRepository;
import br.com.rogrs.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;


import java.util.Collections;
import java.util.List;

import static br.com.rogrs.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link FabricantesResource} REST controller.
 */
@SpringBootTest(classes = SafhApp.class)
public class FabricantesResourceIT {

    private static final String DEFAULT_FABRICANTE = "AAAAAAAAAA";
    private static final String UPDATED_FABRICANTE = "BBBBBBBBBB";

    @Autowired
    private FabricantesRepository fabricantesRepository;

    /**
     * This repository is mocked in the br.com.rogrs.repository.search test package.
     *
     * @see br.com.rogrs.repository.search.FabricantesSearchRepositoryMockConfiguration
     */
    @Autowired
    private FabricantesSearchRepository mockFabricantesSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restFabricantesMockMvc;

    private Fabricantes fabricantes;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FabricantesResource fabricantesResource = new FabricantesResource(fabricantesRepository, mockFabricantesSearchRepository);
        this.restFabricantesMockMvc = MockMvcBuilders.standaloneSetup(fabricantesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fabricantes createEntity() {
        Fabricantes fabricantes = new Fabricantes()
            .fabricante(DEFAULT_FABRICANTE);
        return fabricantes;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fabricantes createUpdatedEntity() {
        Fabricantes fabricantes = new Fabricantes()
            .fabricante(UPDATED_FABRICANTE);
        return fabricantes;
    }

    @BeforeEach
    public void initTest() {
        fabricantesRepository.deleteAll();
        fabricantes = createEntity();
    }

    @Test
    public void createFabricantes() throws Exception {
        int databaseSizeBeforeCreate = fabricantesRepository.findAll().size();

        // Create the Fabricantes
        restFabricantesMockMvc.perform(post("/api/fabricantes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fabricantes)))
            .andExpect(status().isCreated());

        // Validate the Fabricantes in the database
        List<Fabricantes> fabricantesList = fabricantesRepository.findAll();
        assertThat(fabricantesList).hasSize(databaseSizeBeforeCreate + 1);
        Fabricantes testFabricantes = fabricantesList.get(fabricantesList.size() - 1);
        assertThat(testFabricantes.getFabricante()).isEqualTo(DEFAULT_FABRICANTE);

        // Validate the Fabricantes in Elasticsearch
        verify(mockFabricantesSearchRepository, times(1)).save(testFabricantes);
    }

    @Test
    public void createFabricantesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fabricantesRepository.findAll().size();

        // Create the Fabricantes with an existing ID
        fabricantes.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restFabricantesMockMvc.perform(post("/api/fabricantes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fabricantes)))
            .andExpect(status().isBadRequest());

        // Validate the Fabricantes in the database
        List<Fabricantes> fabricantesList = fabricantesRepository.findAll();
        assertThat(fabricantesList).hasSize(databaseSizeBeforeCreate);

        // Validate the Fabricantes in Elasticsearch
        verify(mockFabricantesSearchRepository, times(0)).save(fabricantes);
    }


    @Test
    public void checkFabricanteIsRequired() throws Exception {
        int databaseSizeBeforeTest = fabricantesRepository.findAll().size();
        // set the field null
        fabricantes.setFabricante(null);

        // Create the Fabricantes, which fails.

        restFabricantesMockMvc.perform(post("/api/fabricantes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fabricantes)))
            .andExpect(status().isBadRequest());

        List<Fabricantes> fabricantesList = fabricantesRepository.findAll();
        assertThat(fabricantesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllFabricantes() throws Exception {
        // Initialize the database
        fabricantesRepository.save(fabricantes);

        // Get all the fabricantesList
        restFabricantesMockMvc.perform(get("/api/fabricantes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fabricantes.getId())))
            .andExpect(jsonPath("$.[*].fabricante").value(hasItem(DEFAULT_FABRICANTE)));
    }
    
    @Test
    public void getFabricantes() throws Exception {
        // Initialize the database
        fabricantesRepository.save(fabricantes);

        // Get the fabricantes
        restFabricantesMockMvc.perform(get("/api/fabricantes/{id}", fabricantes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(fabricantes.getId()))
            .andExpect(jsonPath("$.fabricante").value(DEFAULT_FABRICANTE));
    }

    @Test
    public void getNonExistingFabricantes() throws Exception {
        // Get the fabricantes
        restFabricantesMockMvc.perform(get("/api/fabricantes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateFabricantes() throws Exception {
        // Initialize the database
        fabricantesRepository.save(fabricantes);

        int databaseSizeBeforeUpdate = fabricantesRepository.findAll().size();

        // Update the fabricantes
        Fabricantes updatedFabricantes = fabricantesRepository.findById(fabricantes.getId()).get();
        updatedFabricantes
            .fabricante(UPDATED_FABRICANTE);

        restFabricantesMockMvc.perform(put("/api/fabricantes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFabricantes)))
            .andExpect(status().isOk());

        // Validate the Fabricantes in the database
        List<Fabricantes> fabricantesList = fabricantesRepository.findAll();
        assertThat(fabricantesList).hasSize(databaseSizeBeforeUpdate);
        Fabricantes testFabricantes = fabricantesList.get(fabricantesList.size() - 1);
        assertThat(testFabricantes.getFabricante()).isEqualTo(UPDATED_FABRICANTE);

        // Validate the Fabricantes in Elasticsearch
        verify(mockFabricantesSearchRepository, times(1)).save(testFabricantes);
    }

    @Test
    public void updateNonExistingFabricantes() throws Exception {
        int databaseSizeBeforeUpdate = fabricantesRepository.findAll().size();

        // Create the Fabricantes

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFabricantesMockMvc.perform(put("/api/fabricantes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fabricantes)))
            .andExpect(status().isBadRequest());

        // Validate the Fabricantes in the database
        List<Fabricantes> fabricantesList = fabricantesRepository.findAll();
        assertThat(fabricantesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Fabricantes in Elasticsearch
        verify(mockFabricantesSearchRepository, times(0)).save(fabricantes);
    }

    @Test
    public void deleteFabricantes() throws Exception {
        // Initialize the database
        fabricantesRepository.save(fabricantes);

        int databaseSizeBeforeDelete = fabricantesRepository.findAll().size();

        // Delete the fabricantes
        restFabricantesMockMvc.perform(delete("/api/fabricantes/{id}", fabricantes.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Fabricantes> fabricantesList = fabricantesRepository.findAll();
        assertThat(fabricantesList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Fabricantes in Elasticsearch
        verify(mockFabricantesSearchRepository, times(1)).deleteById(fabricantes.getId());
    }

    @Test
    public void searchFabricantes() throws Exception {
        // Initialize the database
        fabricantesRepository.save(fabricantes);
        when(mockFabricantesSearchRepository.search(queryStringQuery("id:" + fabricantes.getId())))
            .thenReturn(Collections.singletonList(fabricantes));
        // Search the fabricantes
        restFabricantesMockMvc.perform(get("/api/_search/fabricantes?query=id:" + fabricantes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fabricantes.getId())))
            .andExpect(jsonPath("$.[*].fabricante").value(hasItem(DEFAULT_FABRICANTE)));
    }
}
