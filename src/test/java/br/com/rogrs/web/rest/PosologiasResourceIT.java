package br.com.rogrs.web.rest;

import br.com.rogrs.SafhApp;
import br.com.rogrs.domain.Posologias;
import br.com.rogrs.repository.PosologiasRepository;
import br.com.rogrs.repository.search.PosologiasSearchRepository;
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
 * Integration tests for the {@link PosologiasResource} REST controller.
 */
@SpringBootTest(classes = SafhApp.class)
public class PosologiasResourceIT {

    private static final String DEFAULT_POSOLOGIA = "AAAAAAAAAA";
    private static final String UPDATED_POSOLOGIA = "BBBBBBBBBB";

    @Autowired
    private PosologiasRepository posologiasRepository;

    /**
     * This repository is mocked in the br.com.rogrs.repository.search test package.
     *
     * @see br.com.rogrs.repository.search.PosologiasSearchRepositoryMockConfiguration
     */
    @Autowired
    private PosologiasSearchRepository mockPosologiasSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restPosologiasMockMvc;

    private Posologias posologias;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PosologiasResource posologiasResource = new PosologiasResource(posologiasRepository, mockPosologiasSearchRepository);
        this.restPosologiasMockMvc = MockMvcBuilders.standaloneSetup(posologiasResource)
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
    public static Posologias createEntity() {
        Posologias posologias = new Posologias()
            .posologia(DEFAULT_POSOLOGIA);
        return posologias;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Posologias createUpdatedEntity() {
        Posologias posologias = new Posologias()
            .posologia(UPDATED_POSOLOGIA);
        return posologias;
    }

    @BeforeEach
    public void initTest() {
        posologiasRepository.deleteAll();
        posologias = createEntity();
    }

    @Test
    public void createPosologias() throws Exception {
        int databaseSizeBeforeCreate = posologiasRepository.findAll().size();

        // Create the Posologias
        restPosologiasMockMvc.perform(post("/api/posologias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(posologias)))
            .andExpect(status().isCreated());

        // Validate the Posologias in the database
        List<Posologias> posologiasList = posologiasRepository.findAll();
        assertThat(posologiasList).hasSize(databaseSizeBeforeCreate + 1);
        Posologias testPosologias = posologiasList.get(posologiasList.size() - 1);
        assertThat(testPosologias.getPosologia()).isEqualTo(DEFAULT_POSOLOGIA);

        // Validate the Posologias in Elasticsearch
        verify(mockPosologiasSearchRepository, times(1)).save(testPosologias);
    }

    @Test
    public void createPosologiasWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = posologiasRepository.findAll().size();

        // Create the Posologias with an existing ID
        posologias.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restPosologiasMockMvc.perform(post("/api/posologias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(posologias)))
            .andExpect(status().isBadRequest());

        // Validate the Posologias in the database
        List<Posologias> posologiasList = posologiasRepository.findAll();
        assertThat(posologiasList).hasSize(databaseSizeBeforeCreate);

        // Validate the Posologias in Elasticsearch
        verify(mockPosologiasSearchRepository, times(0)).save(posologias);
    }


    @Test
    public void checkPosologiaIsRequired() throws Exception {
        int databaseSizeBeforeTest = posologiasRepository.findAll().size();
        // set the field null
        posologias.setPosologia(null);

        // Create the Posologias, which fails.

        restPosologiasMockMvc.perform(post("/api/posologias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(posologias)))
            .andExpect(status().isBadRequest());

        List<Posologias> posologiasList = posologiasRepository.findAll();
        assertThat(posologiasList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllPosologias() throws Exception {
        // Initialize the database
        posologiasRepository.save(posologias);

        // Get all the posologiasList
        restPosologiasMockMvc.perform(get("/api/posologias?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(posologias.getId())))
            .andExpect(jsonPath("$.[*].posologia").value(hasItem(DEFAULT_POSOLOGIA)));
    }
    
    @Test
    public void getPosologias() throws Exception {
        // Initialize the database
        posologiasRepository.save(posologias);

        // Get the posologias
        restPosologiasMockMvc.perform(get("/api/posologias/{id}", posologias.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(posologias.getId()))
            .andExpect(jsonPath("$.posologia").value(DEFAULT_POSOLOGIA));
    }

    @Test
    public void getNonExistingPosologias() throws Exception {
        // Get the posologias
        restPosologiasMockMvc.perform(get("/api/posologias/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updatePosologias() throws Exception {
        // Initialize the database
        posologiasRepository.save(posologias);

        int databaseSizeBeforeUpdate = posologiasRepository.findAll().size();

        // Update the posologias
        Posologias updatedPosologias = posologiasRepository.findById(posologias.getId()).get();
        updatedPosologias
            .posologia(UPDATED_POSOLOGIA);

        restPosologiasMockMvc.perform(put("/api/posologias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPosologias)))
            .andExpect(status().isOk());

        // Validate the Posologias in the database
        List<Posologias> posologiasList = posologiasRepository.findAll();
        assertThat(posologiasList).hasSize(databaseSizeBeforeUpdate);
        Posologias testPosologias = posologiasList.get(posologiasList.size() - 1);
        assertThat(testPosologias.getPosologia()).isEqualTo(UPDATED_POSOLOGIA);

        // Validate the Posologias in Elasticsearch
        verify(mockPosologiasSearchRepository, times(1)).save(testPosologias);
    }

    @Test
    public void updateNonExistingPosologias() throws Exception {
        int databaseSizeBeforeUpdate = posologiasRepository.findAll().size();

        // Create the Posologias

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPosologiasMockMvc.perform(put("/api/posologias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(posologias)))
            .andExpect(status().isBadRequest());

        // Validate the Posologias in the database
        List<Posologias> posologiasList = posologiasRepository.findAll();
        assertThat(posologiasList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Posologias in Elasticsearch
        verify(mockPosologiasSearchRepository, times(0)).save(posologias);
    }

    @Test
    public void deletePosologias() throws Exception {
        // Initialize the database
        posologiasRepository.save(posologias);

        int databaseSizeBeforeDelete = posologiasRepository.findAll().size();

        // Delete the posologias
        restPosologiasMockMvc.perform(delete("/api/posologias/{id}", posologias.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Posologias> posologiasList = posologiasRepository.findAll();
        assertThat(posologiasList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Posologias in Elasticsearch
        verify(mockPosologiasSearchRepository, times(1)).deleteById(posologias.getId());
    }

    @Test
    public void searchPosologias() throws Exception {
        // Initialize the database
        posologiasRepository.save(posologias);
        when(mockPosologiasSearchRepository.search(queryStringQuery("id:" + posologias.getId())))
            .thenReturn(Collections.singletonList(posologias));
        // Search the posologias
        restPosologiasMockMvc.perform(get("/api/_search/posologias?query=id:" + posologias.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(posologias.getId())))
            .andExpect(jsonPath("$.[*].posologia").value(hasItem(DEFAULT_POSOLOGIA)));
    }
}
