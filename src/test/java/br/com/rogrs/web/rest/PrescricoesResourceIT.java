package br.com.rogrs.web.rest;

import br.com.rogrs.SafhApp;
import br.com.rogrs.domain.Prescricoes;
import br.com.rogrs.repository.PrescricoesRepository;
import br.com.rogrs.repository.search.PrescricoesSearchRepository;
import br.com.rogrs.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
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
 * Integration tests for the {@Link PrescricoesResource} REST controller.
 */
@SpringBootTest(classes = SafhApp.class)
public class PrescricoesResourceIT {

    private static final String DEFAULT_PRESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_PRESCRICAO = "BBBBBBBBBB";

    @Autowired
    private PrescricoesRepository prescricoesRepository;

    /**
     * This repository is mocked in the br.com.rogrs.repository.search test package.
     *
     * @see br.com.rogrs.repository.search.PrescricoesSearchRepositoryMockConfiguration
     */
    @Autowired
    private PrescricoesSearchRepository mockPrescricoesSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restPrescricoesMockMvc;

    private Prescricoes prescricoes;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PrescricoesResource prescricoesResource = new PrescricoesResource(prescricoesRepository, mockPrescricoesSearchRepository);
        this.restPrescricoesMockMvc = MockMvcBuilders.standaloneSetup(prescricoesResource)
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
    public static Prescricoes createEntity(EntityManager em) {
        Prescricoes prescricoes = new Prescricoes()
            .prescricao(DEFAULT_PRESCRICAO);
        return prescricoes;
    }

    @BeforeEach
    public void initTest() {
        prescricoes = createEntity(em);
    }

    @Test
    @Transactional
    public void createPrescricoes() throws Exception {
        int databaseSizeBeforeCreate = prescricoesRepository.findAll().size();

        // Create the Prescricoes
        restPrescricoesMockMvc.perform(post("/api/prescricoes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prescricoes)))
            .andExpect(status().isCreated());

        // Validate the Prescricoes in the database
        List<Prescricoes> prescricoesList = prescricoesRepository.findAll();
        assertThat(prescricoesList).hasSize(databaseSizeBeforeCreate + 1);
        Prescricoes testPrescricoes = prescricoesList.get(prescricoesList.size() - 1);
        assertThat(testPrescricoes.getPrescricao()).isEqualTo(DEFAULT_PRESCRICAO);

        // Validate the Prescricoes in Elasticsearch
        verify(mockPrescricoesSearchRepository, times(1)).save(testPrescricoes);
    }

    @Test
    @Transactional
    public void createPrescricoesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = prescricoesRepository.findAll().size();

        // Create the Prescricoes with an existing ID
        prescricoes.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrescricoesMockMvc.perform(post("/api/prescricoes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prescricoes)))
            .andExpect(status().isBadRequest());

        // Validate the Prescricoes in the database
        List<Prescricoes> prescricoesList = prescricoesRepository.findAll();
        assertThat(prescricoesList).hasSize(databaseSizeBeforeCreate);

        // Validate the Prescricoes in Elasticsearch
        verify(mockPrescricoesSearchRepository, times(0)).save(prescricoes);
    }


    @Test
    @Transactional
    public void checkPrescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = prescricoesRepository.findAll().size();
        // set the field null
        prescricoes.setPrescricao(null);

        // Create the Prescricoes, which fails.

        restPrescricoesMockMvc.perform(post("/api/prescricoes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prescricoes)))
            .andExpect(status().isBadRequest());

        List<Prescricoes> prescricoesList = prescricoesRepository.findAll();
        assertThat(prescricoesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPrescricoes() throws Exception {
        // Initialize the database
        prescricoesRepository.saveAndFlush(prescricoes);

        // Get all the prescricoesList
        restPrescricoesMockMvc.perform(get("/api/prescricoes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prescricoes.getId().intValue())))
            .andExpect(jsonPath("$.[*].prescricao").value(hasItem(DEFAULT_PRESCRICAO.toString())));
    }
    
    @Test
    @Transactional
    public void getPrescricoes() throws Exception {
        // Initialize the database
        prescricoesRepository.saveAndFlush(prescricoes);

        // Get the prescricoes
        restPrescricoesMockMvc.perform(get("/api/prescricoes/{id}", prescricoes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(prescricoes.getId().intValue()))
            .andExpect(jsonPath("$.prescricao").value(DEFAULT_PRESCRICAO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPrescricoes() throws Exception {
        // Get the prescricoes
        restPrescricoesMockMvc.perform(get("/api/prescricoes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePrescricoes() throws Exception {
        // Initialize the database
        prescricoesRepository.saveAndFlush(prescricoes);

        int databaseSizeBeforeUpdate = prescricoesRepository.findAll().size();

        // Update the prescricoes
        Prescricoes updatedPrescricoes = prescricoesRepository.findById(prescricoes.getId()).get();
        // Disconnect from session so that the updates on updatedPrescricoes are not directly saved in db
        em.detach(updatedPrescricoes);
        updatedPrescricoes
            .prescricao(UPDATED_PRESCRICAO);

        restPrescricoesMockMvc.perform(put("/api/prescricoes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPrescricoes)))
            .andExpect(status().isOk());

        // Validate the Prescricoes in the database
        List<Prescricoes> prescricoesList = prescricoesRepository.findAll();
        assertThat(prescricoesList).hasSize(databaseSizeBeforeUpdate);
        Prescricoes testPrescricoes = prescricoesList.get(prescricoesList.size() - 1);
        assertThat(testPrescricoes.getPrescricao()).isEqualTo(UPDATED_PRESCRICAO);

        // Validate the Prescricoes in Elasticsearch
        verify(mockPrescricoesSearchRepository, times(1)).save(testPrescricoes);
    }

    @Test
    @Transactional
    public void updateNonExistingPrescricoes() throws Exception {
        int databaseSizeBeforeUpdate = prescricoesRepository.findAll().size();

        // Create the Prescricoes

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrescricoesMockMvc.perform(put("/api/prescricoes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prescricoes)))
            .andExpect(status().isBadRequest());

        // Validate the Prescricoes in the database
        List<Prescricoes> prescricoesList = prescricoesRepository.findAll();
        assertThat(prescricoesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Prescricoes in Elasticsearch
        verify(mockPrescricoesSearchRepository, times(0)).save(prescricoes);
    }

    @Test
    @Transactional
    public void deletePrescricoes() throws Exception {
        // Initialize the database
        prescricoesRepository.saveAndFlush(prescricoes);

        int databaseSizeBeforeDelete = prescricoesRepository.findAll().size();

        // Delete the prescricoes
        restPrescricoesMockMvc.perform(delete("/api/prescricoes/{id}", prescricoes.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Prescricoes> prescricoesList = prescricoesRepository.findAll();
        assertThat(prescricoesList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Prescricoes in Elasticsearch
        verify(mockPrescricoesSearchRepository, times(1)).deleteById(prescricoes.getId());
    }

    @Test
    @Transactional
    public void searchPrescricoes() throws Exception {
        // Initialize the database
        prescricoesRepository.saveAndFlush(prescricoes);
        when(mockPrescricoesSearchRepository.search(queryStringQuery("id:" + prescricoes.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(prescricoes), PageRequest.of(0, 1), 1));
        // Search the prescricoes
        restPrescricoesMockMvc.perform(get("/api/_search/prescricoes?query=id:" + prescricoes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prescricoes.getId().intValue())))
            .andExpect(jsonPath("$.[*].prescricao").value(hasItem(DEFAULT_PRESCRICAO)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Prescricoes.class);
        Prescricoes prescricoes1 = new Prescricoes();
        prescricoes1.setId(1L);
        Prescricoes prescricoes2 = new Prescricoes();
        prescricoes2.setId(prescricoes1.getId());
        assertThat(prescricoes1).isEqualTo(prescricoes2);
        prescricoes2.setId(2L);
        assertThat(prescricoes1).isNotEqualTo(prescricoes2);
        prescricoes1.setId(null);
        assertThat(prescricoes1).isNotEqualTo(prescricoes2);
    }
}
