package br.com.rogrs.web.rest;

import br.com.rogrs.SafhApp;
import br.com.rogrs.domain.Enfermarias;
import br.com.rogrs.repository.EnfermariasRepository;
import br.com.rogrs.repository.search.EnfermariasSearchRepository;
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
 * Integration tests for the {@link EnfermariasResource} REST controller.
 */
@SpringBootTest(classes = SafhApp.class)
public class EnfermariasResourceIT {

    private static final String DEFAULT_ENFERMARIA = "AAAAAAAAAA";
    private static final String UPDATED_ENFERMARIA = "BBBBBBBBBB";

    @Autowired
    private EnfermariasRepository enfermariasRepository;

    /**
     * This repository is mocked in the br.com.rogrs.repository.search test package.
     *
     * @see br.com.rogrs.repository.search.EnfermariasSearchRepositoryMockConfiguration
     */
    @Autowired
    private EnfermariasSearchRepository mockEnfermariasSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restEnfermariasMockMvc;

    private Enfermarias enfermarias;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EnfermariasResource enfermariasResource = new EnfermariasResource(enfermariasRepository, mockEnfermariasSearchRepository);
        this.restEnfermariasMockMvc = MockMvcBuilders.standaloneSetup(enfermariasResource)
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
    public static Enfermarias createEntity() {
        Enfermarias enfermarias = new Enfermarias()
            .enfermaria(DEFAULT_ENFERMARIA);
        return enfermarias;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Enfermarias createUpdatedEntity() {
        Enfermarias enfermarias = new Enfermarias()
            .enfermaria(UPDATED_ENFERMARIA);
        return enfermarias;
    }

    @BeforeEach
    public void initTest() {
        enfermariasRepository.deleteAll();
        enfermarias = createEntity();
    }

    @Test
    public void createEnfermarias() throws Exception {
        int databaseSizeBeforeCreate = enfermariasRepository.findAll().size();

        // Create the Enfermarias
        restEnfermariasMockMvc.perform(post("/api/enfermarias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(enfermarias)))
            .andExpect(status().isCreated());

        // Validate the Enfermarias in the database
        List<Enfermarias> enfermariasList = enfermariasRepository.findAll();
        assertThat(enfermariasList).hasSize(databaseSizeBeforeCreate + 1);
        Enfermarias testEnfermarias = enfermariasList.get(enfermariasList.size() - 1);
        assertThat(testEnfermarias.getEnfermaria()).isEqualTo(DEFAULT_ENFERMARIA);

        // Validate the Enfermarias in Elasticsearch
        verify(mockEnfermariasSearchRepository, times(1)).save(testEnfermarias);
    }

    @Test
    public void createEnfermariasWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = enfermariasRepository.findAll().size();

        // Create the Enfermarias with an existing ID
        enfermarias.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restEnfermariasMockMvc.perform(post("/api/enfermarias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(enfermarias)))
            .andExpect(status().isBadRequest());

        // Validate the Enfermarias in the database
        List<Enfermarias> enfermariasList = enfermariasRepository.findAll();
        assertThat(enfermariasList).hasSize(databaseSizeBeforeCreate);

        // Validate the Enfermarias in Elasticsearch
        verify(mockEnfermariasSearchRepository, times(0)).save(enfermarias);
    }


    @Test
    public void checkEnfermariaIsRequired() throws Exception {
        int databaseSizeBeforeTest = enfermariasRepository.findAll().size();
        // set the field null
        enfermarias.setEnfermaria(null);

        // Create the Enfermarias, which fails.

        restEnfermariasMockMvc.perform(post("/api/enfermarias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(enfermarias)))
            .andExpect(status().isBadRequest());

        List<Enfermarias> enfermariasList = enfermariasRepository.findAll();
        assertThat(enfermariasList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllEnfermarias() throws Exception {
        // Initialize the database
        enfermariasRepository.save(enfermarias);

        // Get all the enfermariasList
        restEnfermariasMockMvc.perform(get("/api/enfermarias?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(enfermarias.getId())))
            .andExpect(jsonPath("$.[*].enfermaria").value(hasItem(DEFAULT_ENFERMARIA)));
    }
    
    @Test
    public void getEnfermarias() throws Exception {
        // Initialize the database
        enfermariasRepository.save(enfermarias);

        // Get the enfermarias
        restEnfermariasMockMvc.perform(get("/api/enfermarias/{id}", enfermarias.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(enfermarias.getId()))
            .andExpect(jsonPath("$.enfermaria").value(DEFAULT_ENFERMARIA));
    }

    @Test
    public void getNonExistingEnfermarias() throws Exception {
        // Get the enfermarias
        restEnfermariasMockMvc.perform(get("/api/enfermarias/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateEnfermarias() throws Exception {
        // Initialize the database
        enfermariasRepository.save(enfermarias);

        int databaseSizeBeforeUpdate = enfermariasRepository.findAll().size();

        // Update the enfermarias
        Enfermarias updatedEnfermarias = enfermariasRepository.findById(enfermarias.getId()).get();
        updatedEnfermarias
            .enfermaria(UPDATED_ENFERMARIA);

        restEnfermariasMockMvc.perform(put("/api/enfermarias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEnfermarias)))
            .andExpect(status().isOk());

        // Validate the Enfermarias in the database
        List<Enfermarias> enfermariasList = enfermariasRepository.findAll();
        assertThat(enfermariasList).hasSize(databaseSizeBeforeUpdate);
        Enfermarias testEnfermarias = enfermariasList.get(enfermariasList.size() - 1);
        assertThat(testEnfermarias.getEnfermaria()).isEqualTo(UPDATED_ENFERMARIA);

        // Validate the Enfermarias in Elasticsearch
        verify(mockEnfermariasSearchRepository, times(1)).save(testEnfermarias);
    }

    @Test
    public void updateNonExistingEnfermarias() throws Exception {
        int databaseSizeBeforeUpdate = enfermariasRepository.findAll().size();

        // Create the Enfermarias

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnfermariasMockMvc.perform(put("/api/enfermarias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(enfermarias)))
            .andExpect(status().isBadRequest());

        // Validate the Enfermarias in the database
        List<Enfermarias> enfermariasList = enfermariasRepository.findAll();
        assertThat(enfermariasList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Enfermarias in Elasticsearch
        verify(mockEnfermariasSearchRepository, times(0)).save(enfermarias);
    }

    @Test
    public void deleteEnfermarias() throws Exception {
        // Initialize the database
        enfermariasRepository.save(enfermarias);

        int databaseSizeBeforeDelete = enfermariasRepository.findAll().size();

        // Delete the enfermarias
        restEnfermariasMockMvc.perform(delete("/api/enfermarias/{id}", enfermarias.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Enfermarias> enfermariasList = enfermariasRepository.findAll();
        assertThat(enfermariasList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Enfermarias in Elasticsearch
        verify(mockEnfermariasSearchRepository, times(1)).deleteById(enfermarias.getId());
    }

    @Test
    public void searchEnfermarias() throws Exception {
        // Initialize the database
        enfermariasRepository.save(enfermarias);
        when(mockEnfermariasSearchRepository.search(queryStringQuery("id:" + enfermarias.getId())))
            .thenReturn(Collections.singletonList(enfermarias));
        // Search the enfermarias
        restEnfermariasMockMvc.perform(get("/api/_search/enfermarias?query=id:" + enfermarias.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(enfermarias.getId())))
            .andExpect(jsonPath("$.[*].enfermaria").value(hasItem(DEFAULT_ENFERMARIA)));
    }
}
