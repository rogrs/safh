package br.com.rogrs.web.rest;

import br.com.rogrs.SafhApp;
import br.com.rogrs.domain.InternacoesDetalhes;
import br.com.rogrs.repository.InternacoesDetalhesRepository;
import br.com.rogrs.repository.search.InternacoesDetalhesSearchRepository;
import br.com.rogrs.service.InternacoesDetalhesService;
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
 * Integration tests for the {@link InternacoesDetalhesResource} REST controller.
 */
@SpringBootTest(classes = SafhApp.class)
public class InternacoesDetalhesResourceIT {

    private static final LocalDate DEFAULT_DATA_DETALHE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_DETALHE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_HORARIO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_HORARIO = LocalDate.now(ZoneId.systemDefault());

    private static final Float DEFAULT_QTD = 1F;
    private static final Float UPDATED_QTD = 2F;

    @Autowired
    private InternacoesDetalhesRepository internacoesDetalhesRepository;

    @Autowired
    private InternacoesDetalhesService internacoesDetalhesService;

    /**
     * This repository is mocked in the br.com.rogrs.repository.search test package.
     *
     * @see br.com.rogrs.repository.search.InternacoesDetalhesSearchRepositoryMockConfiguration
     */
    @Autowired
    private InternacoesDetalhesSearchRepository mockInternacoesDetalhesSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restInternacoesDetalhesMockMvc;

    private InternacoesDetalhes internacoesDetalhes;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InternacoesDetalhesResource internacoesDetalhesResource = new InternacoesDetalhesResource(internacoesDetalhesService);
        this.restInternacoesDetalhesMockMvc = MockMvcBuilders.standaloneSetup(internacoesDetalhesResource)
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
    public static InternacoesDetalhes createEntity() {
        InternacoesDetalhes internacoesDetalhes = new InternacoesDetalhes()
            .dataDetalhe(DEFAULT_DATA_DETALHE)
            .horario(DEFAULT_HORARIO)
            .qtd(DEFAULT_QTD);
        return internacoesDetalhes;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InternacoesDetalhes createUpdatedEntity() {
        InternacoesDetalhes internacoesDetalhes = new InternacoesDetalhes()
            .dataDetalhe(UPDATED_DATA_DETALHE)
            .horario(UPDATED_HORARIO)
            .qtd(UPDATED_QTD);
        return internacoesDetalhes;
    }

    @BeforeEach
    public void initTest() {
        internacoesDetalhesRepository.deleteAll();
        internacoesDetalhes = createEntity();
    }

    @Test
    public void createInternacoesDetalhes() throws Exception {
        int databaseSizeBeforeCreate = internacoesDetalhesRepository.findAll().size();

        // Create the InternacoesDetalhes
        restInternacoesDetalhesMockMvc.perform(post("/api/internacoes-detalhes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(internacoesDetalhes)))
            .andExpect(status().isCreated());

        // Validate the InternacoesDetalhes in the database
        List<InternacoesDetalhes> internacoesDetalhesList = internacoesDetalhesRepository.findAll();
        assertThat(internacoesDetalhesList).hasSize(databaseSizeBeforeCreate + 1);
        InternacoesDetalhes testInternacoesDetalhes = internacoesDetalhesList.get(internacoesDetalhesList.size() - 1);
        assertThat(testInternacoesDetalhes.getDataDetalhe()).isEqualTo(DEFAULT_DATA_DETALHE);
        assertThat(testInternacoesDetalhes.getHorario()).isEqualTo(DEFAULT_HORARIO);
        assertThat(testInternacoesDetalhes.getQtd()).isEqualTo(DEFAULT_QTD);

        // Validate the InternacoesDetalhes in Elasticsearch
        verify(mockInternacoesDetalhesSearchRepository, times(1)).save(testInternacoesDetalhes);
    }

    @Test
    public void createInternacoesDetalhesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = internacoesDetalhesRepository.findAll().size();

        // Create the InternacoesDetalhes with an existing ID
        internacoesDetalhes.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restInternacoesDetalhesMockMvc.perform(post("/api/internacoes-detalhes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(internacoesDetalhes)))
            .andExpect(status().isBadRequest());

        // Validate the InternacoesDetalhes in the database
        List<InternacoesDetalhes> internacoesDetalhesList = internacoesDetalhesRepository.findAll();
        assertThat(internacoesDetalhesList).hasSize(databaseSizeBeforeCreate);

        // Validate the InternacoesDetalhes in Elasticsearch
        verify(mockInternacoesDetalhesSearchRepository, times(0)).save(internacoesDetalhes);
    }


    @Test
    public void checkDataDetalheIsRequired() throws Exception {
        int databaseSizeBeforeTest = internacoesDetalhesRepository.findAll().size();
        // set the field null
        internacoesDetalhes.setDataDetalhe(null);

        // Create the InternacoesDetalhes, which fails.

        restInternacoesDetalhesMockMvc.perform(post("/api/internacoes-detalhes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(internacoesDetalhes)))
            .andExpect(status().isBadRequest());

        List<InternacoesDetalhes> internacoesDetalhesList = internacoesDetalhesRepository.findAll();
        assertThat(internacoesDetalhesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkHorarioIsRequired() throws Exception {
        int databaseSizeBeforeTest = internacoesDetalhesRepository.findAll().size();
        // set the field null
        internacoesDetalhes.setHorario(null);

        // Create the InternacoesDetalhes, which fails.

        restInternacoesDetalhesMockMvc.perform(post("/api/internacoes-detalhes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(internacoesDetalhes)))
            .andExpect(status().isBadRequest());

        List<InternacoesDetalhes> internacoesDetalhesList = internacoesDetalhesRepository.findAll();
        assertThat(internacoesDetalhesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkQtdIsRequired() throws Exception {
        int databaseSizeBeforeTest = internacoesDetalhesRepository.findAll().size();
        // set the field null
        internacoesDetalhes.setQtd(null);

        // Create the InternacoesDetalhes, which fails.

        restInternacoesDetalhesMockMvc.perform(post("/api/internacoes-detalhes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(internacoesDetalhes)))
            .andExpect(status().isBadRequest());

        List<InternacoesDetalhes> internacoesDetalhesList = internacoesDetalhesRepository.findAll();
        assertThat(internacoesDetalhesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllInternacoesDetalhes() throws Exception {
        // Initialize the database
        internacoesDetalhesRepository.save(internacoesDetalhes);

        // Get all the internacoesDetalhesList
        restInternacoesDetalhesMockMvc.perform(get("/api/internacoes-detalhes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(internacoesDetalhes.getId())))
            .andExpect(jsonPath("$.[*].dataDetalhe").value(hasItem(DEFAULT_DATA_DETALHE.toString())))
            .andExpect(jsonPath("$.[*].horario").value(hasItem(DEFAULT_HORARIO.toString())))
            .andExpect(jsonPath("$.[*].qtd").value(hasItem(DEFAULT_QTD.doubleValue())));
    }
    
    @Test
    public void getInternacoesDetalhes() throws Exception {
        // Initialize the database
        internacoesDetalhesRepository.save(internacoesDetalhes);

        // Get the internacoesDetalhes
        restInternacoesDetalhesMockMvc.perform(get("/api/internacoes-detalhes/{id}", internacoesDetalhes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(internacoesDetalhes.getId()))
            .andExpect(jsonPath("$.dataDetalhe").value(DEFAULT_DATA_DETALHE.toString()))
            .andExpect(jsonPath("$.horario").value(DEFAULT_HORARIO.toString()))
            .andExpect(jsonPath("$.qtd").value(DEFAULT_QTD.doubleValue()));
    }

    @Test
    public void getNonExistingInternacoesDetalhes() throws Exception {
        // Get the internacoesDetalhes
        restInternacoesDetalhesMockMvc.perform(get("/api/internacoes-detalhes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateInternacoesDetalhes() throws Exception {
        // Initialize the database
        internacoesDetalhesService.save(internacoesDetalhes);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockInternacoesDetalhesSearchRepository);

        int databaseSizeBeforeUpdate = internacoesDetalhesRepository.findAll().size();

        // Update the internacoesDetalhes
        InternacoesDetalhes updatedInternacoesDetalhes = internacoesDetalhesRepository.findById(internacoesDetalhes.getId()).get();
        updatedInternacoesDetalhes
            .dataDetalhe(UPDATED_DATA_DETALHE)
            .horario(UPDATED_HORARIO)
            .qtd(UPDATED_QTD);

        restInternacoesDetalhesMockMvc.perform(put("/api/internacoes-detalhes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedInternacoesDetalhes)))
            .andExpect(status().isOk());

        // Validate the InternacoesDetalhes in the database
        List<InternacoesDetalhes> internacoesDetalhesList = internacoesDetalhesRepository.findAll();
        assertThat(internacoesDetalhesList).hasSize(databaseSizeBeforeUpdate);
        InternacoesDetalhes testInternacoesDetalhes = internacoesDetalhesList.get(internacoesDetalhesList.size() - 1);
        assertThat(testInternacoesDetalhes.getDataDetalhe()).isEqualTo(UPDATED_DATA_DETALHE);
        assertThat(testInternacoesDetalhes.getHorario()).isEqualTo(UPDATED_HORARIO);
        assertThat(testInternacoesDetalhes.getQtd()).isEqualTo(UPDATED_QTD);

        // Validate the InternacoesDetalhes in Elasticsearch
        verify(mockInternacoesDetalhesSearchRepository, times(1)).save(testInternacoesDetalhes);
    }

    @Test
    public void updateNonExistingInternacoesDetalhes() throws Exception {
        int databaseSizeBeforeUpdate = internacoesDetalhesRepository.findAll().size();

        // Create the InternacoesDetalhes

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInternacoesDetalhesMockMvc.perform(put("/api/internacoes-detalhes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(internacoesDetalhes)))
            .andExpect(status().isBadRequest());

        // Validate the InternacoesDetalhes in the database
        List<InternacoesDetalhes> internacoesDetalhesList = internacoesDetalhesRepository.findAll();
        assertThat(internacoesDetalhesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InternacoesDetalhes in Elasticsearch
        verify(mockInternacoesDetalhesSearchRepository, times(0)).save(internacoesDetalhes);
    }

    @Test
    public void deleteInternacoesDetalhes() throws Exception {
        // Initialize the database
        internacoesDetalhesService.save(internacoesDetalhes);

        int databaseSizeBeforeDelete = internacoesDetalhesRepository.findAll().size();

        // Delete the internacoesDetalhes
        restInternacoesDetalhesMockMvc.perform(delete("/api/internacoes-detalhes/{id}", internacoesDetalhes.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InternacoesDetalhes> internacoesDetalhesList = internacoesDetalhesRepository.findAll();
        assertThat(internacoesDetalhesList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the InternacoesDetalhes in Elasticsearch
        verify(mockInternacoesDetalhesSearchRepository, times(1)).deleteById(internacoesDetalhes.getId());
    }

    @Test
    public void searchInternacoesDetalhes() throws Exception {
        // Initialize the database
        internacoesDetalhesService.save(internacoesDetalhes);
        when(mockInternacoesDetalhesSearchRepository.search(queryStringQuery("id:" + internacoesDetalhes.getId())))
            .thenReturn(Collections.singletonList(internacoesDetalhes));
        // Search the internacoesDetalhes
        restInternacoesDetalhesMockMvc.perform(get("/api/_search/internacoes-detalhes?query=id:" + internacoesDetalhes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(internacoesDetalhes.getId())))
            .andExpect(jsonPath("$.[*].dataDetalhe").value(hasItem(DEFAULT_DATA_DETALHE.toString())))
            .andExpect(jsonPath("$.[*].horario").value(hasItem(DEFAULT_HORARIO.toString())))
            .andExpect(jsonPath("$.[*].qtd").value(hasItem(DEFAULT_QTD.doubleValue())));
    }
}
