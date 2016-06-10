package br.com.rogrs.safh.web.rest;

import br.com.rogrs.safh.SafhApp;
import br.com.rogrs.safh.domain.Prescricoes;
import br.com.rogrs.safh.repository.PrescricoesRepository;
import br.com.rogrs.safh.repository.search.PrescricoesSearchRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the PrescricoesResource REST controller.
 *
 * @see PrescricoesResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SafhApp.class)
@WebAppConfiguration
@IntegrationTest
public class PrescricoesResourceIntTest {

    private static final String DEFAULT_PRESCRICAO = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_PRESCRICAO = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private PrescricoesRepository prescricoesRepository;

    @Inject
    private PrescricoesSearchRepository prescricoesSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPrescricoesMockMvc;

    private Prescricoes prescricoes;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PrescricoesResource prescricoesResource = new PrescricoesResource();
        ReflectionTestUtils.setField(prescricoesResource, "prescricoesSearchRepository", prescricoesSearchRepository);
        ReflectionTestUtils.setField(prescricoesResource, "prescricoesRepository", prescricoesRepository);
        this.restPrescricoesMockMvc = MockMvcBuilders.standaloneSetup(prescricoesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        prescricoesSearchRepository.deleteAll();
        prescricoes = new Prescricoes();
        prescricoes.setPrescricao(DEFAULT_PRESCRICAO);
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
        List<Prescricoes> prescricoes = prescricoesRepository.findAll();
        assertThat(prescricoes).hasSize(databaseSizeBeforeCreate + 1);
        Prescricoes testPrescricoes = prescricoes.get(prescricoes.size() - 1);
        assertThat(testPrescricoes.getPrescricao()).isEqualTo(DEFAULT_PRESCRICAO);

        // Validate the Prescricoes in ElasticSearch
        Prescricoes prescricoesEs = prescricoesSearchRepository.findOne(testPrescricoes.getId());
        assertThat(prescricoesEs).isEqualToComparingFieldByField(testPrescricoes);
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

        List<Prescricoes> prescricoes = prescricoesRepository.findAll();
        assertThat(prescricoes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPrescricoes() throws Exception {
        // Initialize the database
        prescricoesRepository.saveAndFlush(prescricoes);

        // Get all the prescricoes
        restPrescricoesMockMvc.perform(get("/api/prescricoes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
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
        prescricoesSearchRepository.save(prescricoes);
        int databaseSizeBeforeUpdate = prescricoesRepository.findAll().size();

        // Update the prescricoes
        Prescricoes updatedPrescricoes = new Prescricoes();
        updatedPrescricoes.setId(prescricoes.getId());
        updatedPrescricoes.setPrescricao(UPDATED_PRESCRICAO);

        restPrescricoesMockMvc.perform(put("/api/prescricoes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPrescricoes)))
                .andExpect(status().isOk());

        // Validate the Prescricoes in the database
        List<Prescricoes> prescricoes = prescricoesRepository.findAll();
        assertThat(prescricoes).hasSize(databaseSizeBeforeUpdate);
        Prescricoes testPrescricoes = prescricoes.get(prescricoes.size() - 1);
        assertThat(testPrescricoes.getPrescricao()).isEqualTo(UPDATED_PRESCRICAO);

        // Validate the Prescricoes in ElasticSearch
        Prescricoes prescricoesEs = prescricoesSearchRepository.findOne(testPrescricoes.getId());
        assertThat(prescricoesEs).isEqualToComparingFieldByField(testPrescricoes);
    }

    @Test
    @Transactional
    public void deletePrescricoes() throws Exception {
        // Initialize the database
        prescricoesRepository.saveAndFlush(prescricoes);
        prescricoesSearchRepository.save(prescricoes);
        int databaseSizeBeforeDelete = prescricoesRepository.findAll().size();

        // Get the prescricoes
        restPrescricoesMockMvc.perform(delete("/api/prescricoes/{id}", prescricoes.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean prescricoesExistsInEs = prescricoesSearchRepository.exists(prescricoes.getId());
        assertThat(prescricoesExistsInEs).isFalse();

        // Validate the database is empty
        List<Prescricoes> prescricoes = prescricoesRepository.findAll();
        assertThat(prescricoes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPrescricoes() throws Exception {
        // Initialize the database
        prescricoesRepository.saveAndFlush(prescricoes);
        prescricoesSearchRepository.save(prescricoes);

        // Search the prescricoes
        restPrescricoesMockMvc.perform(get("/api/_search/prescricoes?query=id:" + prescricoes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prescricoes.getId().intValue())))
            .andExpect(jsonPath("$.[*].prescricao").value(hasItem(DEFAULT_PRESCRICAO.toString())));
    }
}
