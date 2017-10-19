package br.com.rogrs.safh.web.rest;

import br.com.rogrs.safh.SafhApp;

import br.com.rogrs.safh.domain.InternacoesDetalhes;
import br.com.rogrs.safh.repository.InternacoesDetalhesRepository;
import br.com.rogrs.safh.service.InternacoesDetalhesService;
import br.com.rogrs.safh.repository.search.InternacoesDetalhesSearchRepository;
import br.com.rogrs.safh.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the InternacoesDetalhesResource REST controller.
 *
 * @see InternacoesDetalhesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SafhApp.class)
public class InternacoesDetalhesResourceIntTest {

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

    @Autowired
    private InternacoesDetalhesSearchRepository internacoesDetalhesSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restInternacoesDetalhesMockMvc;

    private InternacoesDetalhes internacoesDetalhes;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InternacoesDetalhesResource internacoesDetalhesResource = new InternacoesDetalhesResource(internacoesDetalhesService);
        this.restInternacoesDetalhesMockMvc = MockMvcBuilders.standaloneSetup(internacoesDetalhesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InternacoesDetalhes createEntity(EntityManager em) {
        InternacoesDetalhes internacoesDetalhes = new InternacoesDetalhes()
            .dataDetalhe(DEFAULT_DATA_DETALHE)
            .horario(DEFAULT_HORARIO)
            .qtd(DEFAULT_QTD);
        return internacoesDetalhes;
    }

    @Before
    public void initTest() {
        internacoesDetalhesSearchRepository.deleteAll();
        internacoesDetalhes = createEntity(em);
    }

    @Test
    @Transactional
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
        InternacoesDetalhes internacoesDetalhesEs = internacoesDetalhesSearchRepository.findOne(testInternacoesDetalhes.getId());
        assertThat(internacoesDetalhesEs).isEqualToComparingFieldByField(testInternacoesDetalhes);
    }

    @Test
    @Transactional
    public void createInternacoesDetalhesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = internacoesDetalhesRepository.findAll().size();

        // Create the InternacoesDetalhes with an existing ID
        internacoesDetalhes.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInternacoesDetalhesMockMvc.perform(post("/api/internacoes-detalhes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(internacoesDetalhes)))
            .andExpect(status().isBadRequest());

        // Validate the InternacoesDetalhes in the database
        List<InternacoesDetalhes> internacoesDetalhesList = internacoesDetalhesRepository.findAll();
        assertThat(internacoesDetalhesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
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
    @Transactional
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
    @Transactional
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
    @Transactional
    public void getAllInternacoesDetalhes() throws Exception {
        // Initialize the database
        internacoesDetalhesRepository.saveAndFlush(internacoesDetalhes);

        // Get all the internacoesDetalhesList
        restInternacoesDetalhesMockMvc.perform(get("/api/internacoes-detalhes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(internacoesDetalhes.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataDetalhe").value(hasItem(DEFAULT_DATA_DETALHE.toString())))
            .andExpect(jsonPath("$.[*].horario").value(hasItem(DEFAULT_HORARIO.toString())))
            .andExpect(jsonPath("$.[*].qtd").value(hasItem(DEFAULT_QTD.doubleValue())));
    }

    @Test
    @Transactional
    public void getInternacoesDetalhes() throws Exception {
        // Initialize the database
        internacoesDetalhesRepository.saveAndFlush(internacoesDetalhes);

        // Get the internacoesDetalhes
        restInternacoesDetalhesMockMvc.perform(get("/api/internacoes-detalhes/{id}", internacoesDetalhes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(internacoesDetalhes.getId().intValue()))
            .andExpect(jsonPath("$.dataDetalhe").value(DEFAULT_DATA_DETALHE.toString()))
            .andExpect(jsonPath("$.horario").value(DEFAULT_HORARIO.toString()))
            .andExpect(jsonPath("$.qtd").value(DEFAULT_QTD.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingInternacoesDetalhes() throws Exception {
        // Get the internacoesDetalhes
        restInternacoesDetalhesMockMvc.perform(get("/api/internacoes-detalhes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInternacoesDetalhes() throws Exception {
        // Initialize the database
        internacoesDetalhesService.save(internacoesDetalhes);

        int databaseSizeBeforeUpdate = internacoesDetalhesRepository.findAll().size();

        // Update the internacoesDetalhes
        InternacoesDetalhes updatedInternacoesDetalhes = internacoesDetalhesRepository.findOne(internacoesDetalhes.getId());
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
        InternacoesDetalhes internacoesDetalhesEs = internacoesDetalhesSearchRepository.findOne(testInternacoesDetalhes.getId());
        assertThat(internacoesDetalhesEs).isEqualToComparingFieldByField(testInternacoesDetalhes);
    }

    @Test
    @Transactional
    public void updateNonExistingInternacoesDetalhes() throws Exception {
        int databaseSizeBeforeUpdate = internacoesDetalhesRepository.findAll().size();

        // Create the InternacoesDetalhes

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restInternacoesDetalhesMockMvc.perform(put("/api/internacoes-detalhes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(internacoesDetalhes)))
            .andExpect(status().isCreated());

        // Validate the InternacoesDetalhes in the database
        List<InternacoesDetalhes> internacoesDetalhesList = internacoesDetalhesRepository.findAll();
        assertThat(internacoesDetalhesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteInternacoesDetalhes() throws Exception {
        // Initialize the database
        internacoesDetalhesService.save(internacoesDetalhes);

        int databaseSizeBeforeDelete = internacoesDetalhesRepository.findAll().size();

        // Get the internacoesDetalhes
        restInternacoesDetalhesMockMvc.perform(delete("/api/internacoes-detalhes/{id}", internacoesDetalhes.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean internacoesDetalhesExistsInEs = internacoesDetalhesSearchRepository.exists(internacoesDetalhes.getId());
        assertThat(internacoesDetalhesExistsInEs).isFalse();

        // Validate the database is empty
        List<InternacoesDetalhes> internacoesDetalhesList = internacoesDetalhesRepository.findAll();
        assertThat(internacoesDetalhesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchInternacoesDetalhes() throws Exception {
        // Initialize the database
        internacoesDetalhesService.save(internacoesDetalhes);

        // Search the internacoesDetalhes
        restInternacoesDetalhesMockMvc.perform(get("/api/_search/internacoes-detalhes?query=id:" + internacoesDetalhes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(internacoesDetalhes.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataDetalhe").value(hasItem(DEFAULT_DATA_DETALHE.toString())))
            .andExpect(jsonPath("$.[*].horario").value(hasItem(DEFAULT_HORARIO.toString())))
            .andExpect(jsonPath("$.[*].qtd").value(hasItem(DEFAULT_QTD.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InternacoesDetalhes.class);
        InternacoesDetalhes internacoesDetalhes1 = new InternacoesDetalhes();
        internacoesDetalhes1.setId(1L);
        InternacoesDetalhes internacoesDetalhes2 = new InternacoesDetalhes();
        internacoesDetalhes2.setId(internacoesDetalhes1.getId());
        assertThat(internacoesDetalhes1).isEqualTo(internacoesDetalhes2);
        internacoesDetalhes2.setId(2L);
        assertThat(internacoesDetalhes1).isNotEqualTo(internacoesDetalhes2);
        internacoesDetalhes1.setId(null);
        assertThat(internacoesDetalhes1).isNotEqualTo(internacoesDetalhes2);
    }
}
