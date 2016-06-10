package br.com.rogrs.safh.web.rest;

import br.com.rogrs.safh.SafhApp;
import br.com.rogrs.safh.domain.Internacoes;
import br.com.rogrs.safh.repository.InternacoesRepository;
import br.com.rogrs.safh.repository.search.InternacoesSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the InternacoesResource REST controller.
 *
 * @see InternacoesResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SafhApp.class)
@WebAppConfiguration
@IntegrationTest
public class InternacoesResourceIntTest {


    private static final LocalDate DEFAULT_DATA_INTERNACAO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_INTERNACAO = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private InternacoesRepository internacoesRepository;

    @Inject
    private InternacoesSearchRepository internacoesSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restInternacoesMockMvc;

    private Internacoes internacoes;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InternacoesResource internacoesResource = new InternacoesResource();
        ReflectionTestUtils.setField(internacoesResource, "internacoesSearchRepository", internacoesSearchRepository);
        ReflectionTestUtils.setField(internacoesResource, "internacoesRepository", internacoesRepository);
        this.restInternacoesMockMvc = MockMvcBuilders.standaloneSetup(internacoesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        internacoesSearchRepository.deleteAll();
        internacoes = new Internacoes();
        internacoes.setDataInternacao(DEFAULT_DATA_INTERNACAO);
        internacoes.setDescricao(DEFAULT_DESCRICAO);
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
        List<Internacoes> internacoes = internacoesRepository.findAll();
        assertThat(internacoes).hasSize(databaseSizeBeforeCreate + 1);
        Internacoes testInternacoes = internacoes.get(internacoes.size() - 1);
        assertThat(testInternacoes.getDataInternacao()).isEqualTo(DEFAULT_DATA_INTERNACAO);
        assertThat(testInternacoes.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);

        // Validate the Internacoes in ElasticSearch
        Internacoes internacoesEs = internacoesSearchRepository.findOne(testInternacoes.getId());
        assertThat(internacoesEs).isEqualToComparingFieldByField(testInternacoes);
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

        List<Internacoes> internacoes = internacoesRepository.findAll();
        assertThat(internacoes).hasSize(databaseSizeBeforeTest);
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

        List<Internacoes> internacoes = internacoesRepository.findAll();
        assertThat(internacoes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInternacoes() throws Exception {
        // Initialize the database
        internacoesRepository.saveAndFlush(internacoes);

        // Get all the internacoes
        restInternacoesMockMvc.perform(get("/api/internacoes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
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
        Internacoes updatedInternacoes = new Internacoes();
        updatedInternacoes.setId(internacoes.getId());
        updatedInternacoes.setDataInternacao(UPDATED_DATA_INTERNACAO);
        updatedInternacoes.setDescricao(UPDATED_DESCRICAO);

        restInternacoesMockMvc.perform(put("/api/internacoes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedInternacoes)))
                .andExpect(status().isOk());

        // Validate the Internacoes in the database
        List<Internacoes> internacoes = internacoesRepository.findAll();
        assertThat(internacoes).hasSize(databaseSizeBeforeUpdate);
        Internacoes testInternacoes = internacoes.get(internacoes.size() - 1);
        assertThat(testInternacoes.getDataInternacao()).isEqualTo(UPDATED_DATA_INTERNACAO);
        assertThat(testInternacoes.getDescricao()).isEqualTo(UPDATED_DESCRICAO);

        // Validate the Internacoes in ElasticSearch
        Internacoes internacoesEs = internacoesSearchRepository.findOne(testInternacoes.getId());
        assertThat(internacoesEs).isEqualToComparingFieldByField(testInternacoes);
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

        // Validate ElasticSearch is empty
        boolean internacoesExistsInEs = internacoesSearchRepository.exists(internacoes.getId());
        assertThat(internacoesExistsInEs).isFalse();

        // Validate the database is empty
        List<Internacoes> internacoes = internacoesRepository.findAll();
        assertThat(internacoes).hasSize(databaseSizeBeforeDelete - 1);
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(internacoes.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataInternacao").value(hasItem(DEFAULT_DATA_INTERNACAO.toString())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())));
    }
}
