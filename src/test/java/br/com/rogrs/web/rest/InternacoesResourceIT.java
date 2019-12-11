package br.com.rogrs.web.rest;

import br.com.rogrs.SafhApp;
import br.com.rogrs.domain.Internacoes;
import br.com.rogrs.repository.InternacoesRepository;
import br.com.rogrs.repository.search.InternacoesSearchRepository;
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


import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link InternacoesResource} REST controller.
 */
@SpringBootTest(classes = SafhApp.class)
public class InternacoesResourceIT {

    private static final LocalDate DEFAULT_DATA_INTERNACAO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_INTERNACAO = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    @Autowired
    private InternacoesRepository internacoesRepository;

    /**
     * This repository is mocked in the br.com.rogrs.repository.search test package.
     *
     * @see br.com.rogrs.repository.search.InternacoesSearchRepositoryMockConfiguration
     */
    @Autowired
    private InternacoesSearchRepository mockInternacoesSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restInternacoesMockMvc;

    private Internacoes internacoes;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InternacoesResource internacoesResource = new InternacoesResource(internacoesRepository, mockInternacoesSearchRepository);
        this.restInternacoesMockMvc = MockMvcBuilders.standaloneSetup(internacoesResource)
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
    public static Internacoes createEntity() {
        Internacoes internacoes = new Internacoes()
            .dataInternacao(DEFAULT_DATA_INTERNACAO)
            .descricao(DEFAULT_DESCRICAO);
        return internacoes;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Internacoes createUpdatedEntity() {
        Internacoes internacoes = new Internacoes()
            .dataInternacao(UPDATED_DATA_INTERNACAO)
            .descricao(UPDATED_DESCRICAO);
        return internacoes;
    }

    @BeforeEach
    public void initTest() {
        internacoesRepository.deleteAll();
        internacoes = createEntity();
    }

    @Test
    public void createInternacoes() throws Exception {
        int databaseSizeBeforeCreate = internacoesRepository.findAll().size();

        // Create the Internacoes
        restInternacoesMockMvc.perform(post("/api/internacoes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(internacoes)))
            .andExpect(status().isCreated());

        // Validate the Internacoes in the database
        List<Internacoes> internacoesList = internacoesRepository.findAll();
        assertThat(internacoesList).hasSize(databaseSizeBeforeCreate + 1);
        Internacoes testInternacoes = internacoesList.get(internacoesList.size() - 1);
        assertThat(testInternacoes.getDataInternacao()).isEqualTo(DEFAULT_DATA_INTERNACAO);
        assertThat(testInternacoes.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);

        // Validate the Internacoes in Elasticsearch
        verify(mockInternacoesSearchRepository, times(1)).save(testInternacoes);
    }

    @Test
    public void createInternacoesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = internacoesRepository.findAll().size();

        // Create the Internacoes with an existing ID
        internacoes.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restInternacoesMockMvc.perform(post("/api/internacoes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(internacoes)))
            .andExpect(status().isBadRequest());

        // Validate the Internacoes in the database
        List<Internacoes> internacoesList = internacoesRepository.findAll();
        assertThat(internacoesList).hasSize(databaseSizeBeforeCreate);

        // Validate the Internacoes in Elasticsearch
        verify(mockInternacoesSearchRepository, times(0)).save(internacoes);
    }


    @Test
    public void checkDataInternacaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = internacoesRepository.findAll().size();
        // set the field null
        internacoes.setDataInternacao(null);

        // Create the Internacoes, which fails.

        restInternacoesMockMvc.perform(post("/api/internacoes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(internacoes)))
            .andExpect(status().isBadRequest());

        List<Internacoes> internacoesList = internacoesRepository.findAll();
        assertThat(internacoesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = internacoesRepository.findAll().size();
        // set the field null
        internacoes.setDescricao(null);

        // Create the Internacoes, which fails.

        restInternacoesMockMvc.perform(post("/api/internacoes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(internacoes)))
            .andExpect(status().isBadRequest());

        List<Internacoes> internacoesList = internacoesRepository.findAll();
        assertThat(internacoesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllInternacoes() throws Exception {
        // Initialize the database
        internacoesRepository.save(internacoes);

        // Get all the internacoesList
        restInternacoesMockMvc.perform(get("/api/internacoes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(internacoes.getId())))
            .andExpect(jsonPath("$.[*].dataInternacao").value(hasItem(DEFAULT_DATA_INTERNACAO.toString())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));
    }
    
    @Test
    public void getInternacoes() throws Exception {
        // Initialize the database
        internacoesRepository.save(internacoes);

        // Get the internacoes
        restInternacoesMockMvc.perform(get("/api/internacoes/{id}", internacoes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(internacoes.getId()))
            .andExpect(jsonPath("$.dataInternacao").value(DEFAULT_DATA_INTERNACAO.toString()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO));
    }

    @Test
    public void getNonExistingInternacoes() throws Exception {
        // Get the internacoes
        restInternacoesMockMvc.perform(get("/api/internacoes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateInternacoes() throws Exception {
        // Initialize the database
        internacoesRepository.save(internacoes);

        int databaseSizeBeforeUpdate = internacoesRepository.findAll().size();

        // Update the internacoes
        Internacoes updatedInternacoes = internacoesRepository.findById(internacoes.getId()).get();
        updatedInternacoes
            .dataInternacao(UPDATED_DATA_INTERNACAO)
            .descricao(UPDATED_DESCRICAO);

        restInternacoesMockMvc.perform(put("/api/internacoes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedInternacoes)))
            .andExpect(status().isOk());

        // Validate the Internacoes in the database
        List<Internacoes> internacoesList = internacoesRepository.findAll();
        assertThat(internacoesList).hasSize(databaseSizeBeforeUpdate);
        Internacoes testInternacoes = internacoesList.get(internacoesList.size() - 1);
        assertThat(testInternacoes.getDataInternacao()).isEqualTo(UPDATED_DATA_INTERNACAO);
        assertThat(testInternacoes.getDescricao()).isEqualTo(UPDATED_DESCRICAO);

        // Validate the Internacoes in Elasticsearch
        verify(mockInternacoesSearchRepository, times(1)).save(testInternacoes);
    }

    @Test
    public void updateNonExistingInternacoes() throws Exception {
        int databaseSizeBeforeUpdate = internacoesRepository.findAll().size();

        // Create the Internacoes

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInternacoesMockMvc.perform(put("/api/internacoes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(internacoes)))
            .andExpect(status().isBadRequest());

        // Validate the Internacoes in the database
        List<Internacoes> internacoesList = internacoesRepository.findAll();
        assertThat(internacoesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Internacoes in Elasticsearch
        verify(mockInternacoesSearchRepository, times(0)).save(internacoes);
    }

    @Test
    public void deleteInternacoes() throws Exception {
        // Initialize the database
        internacoesRepository.save(internacoes);

        int databaseSizeBeforeDelete = internacoesRepository.findAll().size();

        // Delete the internacoes
        restInternacoesMockMvc.perform(delete("/api/internacoes/{id}", internacoes.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Internacoes> internacoesList = internacoesRepository.findAll();
        assertThat(internacoesList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Internacoes in Elasticsearch
        verify(mockInternacoesSearchRepository, times(1)).deleteById(internacoes.getId());
    }

    @Test
    public void searchInternacoes() throws Exception {
        // Initialize the database
        internacoesRepository.save(internacoes);
        when(mockInternacoesSearchRepository.search(queryStringQuery("id:" + internacoes.getId())))
            .thenReturn(Collections.singletonList(internacoes));
        // Search the internacoes
        restInternacoesMockMvc.perform(get("/api/_search/internacoes?query=id:" + internacoes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(internacoes.getId())))
            .andExpect(jsonPath("$.[*].dataInternacao").value(hasItem(DEFAULT_DATA_INTERNACAO.toString())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));
    }
}
