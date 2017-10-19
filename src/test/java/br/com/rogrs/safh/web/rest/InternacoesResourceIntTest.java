package br.com.rogrs.safh.web.rest;

import br.com.rogrs.safh.SafhApp;

import br.com.rogrs.safh.domain.Internacoes;
import br.com.rogrs.safh.repository.InternacoesRepository;
import br.com.rogrs.safh.repository.search.InternacoesSearchRepository;
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
 * Test class for the InternacoesResource REST controller.
 *
 * @see InternacoesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SafhApp.class)
public class InternacoesResourceIntTest {

    private static final LocalDate DEFAULT_DATA_INTERNACAO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_INTERNACAO = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    @Autowired
    private InternacoesRepository internacoesRepository;

    @Autowired
    private InternacoesSearchRepository internacoesSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restInternacoesMockMvc;

    private Internacoes internacoes;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InternacoesResource internacoesResource = new InternacoesResource(internacoesRepository, internacoesSearchRepository);
        this.restInternacoesMockMvc = MockMvcBuilders.standaloneSetup(internacoesResource)
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
    public static Internacoes createEntity(EntityManager em) {
        Internacoes internacoes = new Internacoes()
            .dataInternacao(DEFAULT_DATA_INTERNACAO)
            .descricao(DEFAULT_DESCRICAO);
        return internacoes;
    }

    @Before
    public void initTest() {
        internacoesSearchRepository.deleteAll();
        internacoes = createEntity(em);
    }

    @Test
    @Transactional
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
        Internacoes internacoesEs = internacoesSearchRepository.findOne(testInternacoes.getId());
        assertThat(internacoesEs).isEqualToComparingFieldByField(testInternacoes);
    }

    @Test
    @Transactional
    public void createInternacoesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = internacoesRepository.findAll().size();

        // Create the Internacoes with an existing ID
        internacoes.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInternacoesMockMvc.perform(post("/api/internacoes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(internacoes)))
            .andExpect(status().isBadRequest());

        // Validate the Internacoes in the database
        List<Internacoes> internacoesList = internacoesRepository.findAll();
        assertThat(internacoesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
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
    @Transactional
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
    @Transactional
    public void getAllInternacoes() throws Exception {
        // Initialize the database
        internacoesRepository.saveAndFlush(internacoes);

        // Get all the internacoesList
        restInternacoesMockMvc.perform(get("/api/internacoes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(internacoes.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataInternacao").value(hasItem(DEFAULT_DATA_INTERNACAO.toString())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())));
    }

    @Test
    @Transactional
    public void getInternacoes() throws Exception {
        // Initialize the database
        internacoesRepository.saveAndFlush(internacoes);

        // Get the internacoes
        restInternacoesMockMvc.perform(get("/api/internacoes/{id}", internacoes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(internacoes.getId().intValue()))
            .andExpect(jsonPath("$.dataInternacao").value(DEFAULT_DATA_INTERNACAO.toString()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInternacoes() throws Exception {
        // Get the internacoes
        restInternacoesMockMvc.perform(get("/api/internacoes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInternacoes() throws Exception {
        // Initialize the database
        internacoesRepository.saveAndFlush(internacoes);
        internacoesSearchRepository.save(internacoes);
        int databaseSizeBeforeUpdate = internacoesRepository.findAll().size();

        // Update the internacoes
        Internacoes updatedInternacoes = internacoesRepository.findOne(internacoes.getId());
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
        Internacoes internacoesEs = internacoesSearchRepository.findOne(testInternacoes.getId());
        assertThat(internacoesEs).isEqualToComparingFieldByField(testInternacoes);
    }

    @Test
    @Transactional
    public void updateNonExistingInternacoes() throws Exception {
        int databaseSizeBeforeUpdate = internacoesRepository.findAll().size();

        // Create the Internacoes

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restInternacoesMockMvc.perform(put("/api/internacoes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(internacoes)))
            .andExpect(status().isCreated());

        // Validate the Internacoes in the database
        List<Internacoes> internacoesList = internacoesRepository.findAll();
        assertThat(internacoesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteInternacoes() throws Exception {
        // Initialize the database
        internacoesRepository.saveAndFlush(internacoes);
        internacoesSearchRepository.save(internacoes);
        int databaseSizeBeforeDelete = internacoesRepository.findAll().size();

        // Get the internacoes
        restInternacoesMockMvc.perform(delete("/api/internacoes/{id}", internacoes.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean internacoesExistsInEs = internacoesSearchRepository.exists(internacoes.getId());
        assertThat(internacoesExistsInEs).isFalse();

        // Validate the database is empty
        List<Internacoes> internacoesList = internacoesRepository.findAll();
        assertThat(internacoesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchInternacoes() throws Exception {
        // Initialize the database
        internacoesRepository.saveAndFlush(internacoes);
        internacoesSearchRepository.save(internacoes);

        // Search the internacoes
        restInternacoesMockMvc.perform(get("/api/_search/internacoes?query=id:" + internacoes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(internacoes.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataInternacao").value(hasItem(DEFAULT_DATA_INTERNACAO.toString())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Internacoes.class);
        Internacoes internacoes1 = new Internacoes();
        internacoes1.setId(1L);
        Internacoes internacoes2 = new Internacoes();
        internacoes2.setId(internacoes1.getId());
        assertThat(internacoes1).isEqualTo(internacoes2);
        internacoes2.setId(2L);
        assertThat(internacoes1).isNotEqualTo(internacoes2);
        internacoes1.setId(null);
        assertThat(internacoes1).isNotEqualTo(internacoes2);
    }
}
