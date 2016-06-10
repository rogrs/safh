package br.com.rogrs.safh.web.rest;

import br.com.rogrs.safh.SafhApp;
import br.com.rogrs.safh.domain.InternacoesDetalhes;
import br.com.rogrs.safh.repository.InternacoesDetalhesRepository;
import br.com.rogrs.safh.service.InternacoesDetalhesService;
import br.com.rogrs.safh.repository.search.InternacoesDetalhesSearchRepository;

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
 * Test class for the InternacoesDetalhesResource REST controller.
 *
 * @see InternacoesDetalhesResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SafhApp.class)
@WebAppConfiguration
@IntegrationTest
public class InternacoesDetalhesResourceIntTest {


    private static final LocalDate DEFAULT_DATA_DETALHE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_DETALHE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_HORARIO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_HORARIO = LocalDate.now(ZoneId.systemDefault());

    private static final Float DEFAULT_QTD = 1F;
    private static final Float UPDATED_QTD = 2F;

    @Inject
    private InternacoesDetalhesRepository internacoesDetalhesRepository;

    @Inject
    private InternacoesDetalhesService internacoesDetalhesService;

    @Inject
    private InternacoesDetalhesSearchRepository internacoesDetalhesSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restInternacoesDetalhesMockMvc;

    private InternacoesDetalhes internacoesDetalhes;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InternacoesDetalhesResource internacoesDetalhesResource = new InternacoesDetalhesResource();
        ReflectionTestUtils.setField(internacoesDetalhesResource, "internacoesDetalhesService", internacoesDetalhesService);
        this.restInternacoesDetalhesMockMvc = MockMvcBuilders.standaloneSetup(internacoesDetalhesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        internacoesDetalhesSearchRepository.deleteAll();
        internacoesDetalhes = new InternacoesDetalhes();
        internacoesDetalhes.setDataDetalhe(DEFAULT_DATA_DETALHE);
        internacoesDetalhes.setHorario(DEFAULT_HORARIO);
        internacoesDetalhes.setQtd(DEFAULT_QTD);
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
        List<InternacoesDetalhes> internacoesDetalhes = internacoesDetalhesRepository.findAll();
        assertThat(internacoesDetalhes).hasSize(databaseSizeBeforeCreate + 1);
        InternacoesDetalhes testInternacoesDetalhes = internacoesDetalhes.get(internacoesDetalhes.size() - 1);
        assertThat(testInternacoesDetalhes.getDataDetalhe()).isEqualTo(DEFAULT_DATA_DETALHE);
        assertThat(testInternacoesDetalhes.getHorario()).isEqualTo(DEFAULT_HORARIO);
        assertThat(testInternacoesDetalhes.getQtd()).isEqualTo(DEFAULT_QTD);

        // Validate the InternacoesDetalhes in ElasticSearch
        InternacoesDetalhes internacoesDetalhesEs = internacoesDetalhesSearchRepository.findOne(testInternacoesDetalhes.getId());
        assertThat(internacoesDetalhesEs).isEqualToComparingFieldByField(testInternacoesDetalhes);
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

        List<InternacoesDetalhes> internacoesDetalhes = internacoesDetalhesRepository.findAll();
        assertThat(internacoesDetalhes).hasSize(databaseSizeBeforeTest);
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

        List<InternacoesDetalhes> internacoesDetalhes = internacoesDetalhesRepository.findAll();
        assertThat(internacoesDetalhes).hasSize(databaseSizeBeforeTest);
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

        List<InternacoesDetalhes> internacoesDetalhes = internacoesDetalhesRepository.findAll();
        assertThat(internacoesDetalhes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInternacoesDetalhes() throws Exception {
        // Initialize the database
        internacoesDetalhesRepository.saveAndFlush(internacoesDetalhes);

        // Get all the internacoesDetalhes
        restInternacoesDetalhesMockMvc.perform(get("/api/internacoes-detalhes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
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
        InternacoesDetalhes updatedInternacoesDetalhes = new InternacoesDetalhes();
        updatedInternacoesDetalhes.setId(internacoesDetalhes.getId());
        updatedInternacoesDetalhes.setDataDetalhe(UPDATED_DATA_DETALHE);
        updatedInternacoesDetalhes.setHorario(UPDATED_HORARIO);
        updatedInternacoesDetalhes.setQtd(UPDATED_QTD);

        restInternacoesDetalhesMockMvc.perform(put("/api/internacoes-detalhes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedInternacoesDetalhes)))
                .andExpect(status().isOk());

        // Validate the InternacoesDetalhes in the database
        List<InternacoesDetalhes> internacoesDetalhes = internacoesDetalhesRepository.findAll();
        assertThat(internacoesDetalhes).hasSize(databaseSizeBeforeUpdate);
        InternacoesDetalhes testInternacoesDetalhes = internacoesDetalhes.get(internacoesDetalhes.size() - 1);
        assertThat(testInternacoesDetalhes.getDataDetalhe()).isEqualTo(UPDATED_DATA_DETALHE);
        assertThat(testInternacoesDetalhes.getHorario()).isEqualTo(UPDATED_HORARIO);
        assertThat(testInternacoesDetalhes.getQtd()).isEqualTo(UPDATED_QTD);

        // Validate the InternacoesDetalhes in ElasticSearch
        InternacoesDetalhes internacoesDetalhesEs = internacoesDetalhesSearchRepository.findOne(testInternacoesDetalhes.getId());
        assertThat(internacoesDetalhesEs).isEqualToComparingFieldByField(testInternacoesDetalhes);
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

        // Validate ElasticSearch is empty
        boolean internacoesDetalhesExistsInEs = internacoesDetalhesSearchRepository.exists(internacoesDetalhes.getId());
        assertThat(internacoesDetalhesExistsInEs).isFalse();

        // Validate the database is empty
        List<InternacoesDetalhes> internacoesDetalhes = internacoesDetalhesRepository.findAll();
        assertThat(internacoesDetalhes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchInternacoesDetalhes() throws Exception {
        // Initialize the database
        internacoesDetalhesService.save(internacoesDetalhes);

        // Search the internacoesDetalhes
        restInternacoesDetalhesMockMvc.perform(get("/api/_search/internacoes-detalhes?query=id:" + internacoesDetalhes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(internacoesDetalhes.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataDetalhe").value(hasItem(DEFAULT_DATA_DETALHE.toString())))
            .andExpect(jsonPath("$.[*].horario").value(hasItem(DEFAULT_HORARIO.toString())))
            .andExpect(jsonPath("$.[*].qtd").value(hasItem(DEFAULT_QTD.doubleValue())));
    }
}
