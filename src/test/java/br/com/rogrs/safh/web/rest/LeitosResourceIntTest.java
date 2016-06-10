package br.com.rogrs.safh.web.rest;

import br.com.rogrs.safh.SafhApp;
import br.com.rogrs.safh.domain.Leitos;
import br.com.rogrs.safh.repository.LeitosRepository;
import br.com.rogrs.safh.repository.search.LeitosSearchRepository;

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
 * Test class for the LeitosResource REST controller.
 *
 * @see LeitosResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SafhApp.class)
@WebAppConfiguration
@IntegrationTest
public class LeitosResourceIntTest {

    private static final String DEFAULT_LEITO = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LEITO = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_TIPO = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_TIPO = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private LeitosRepository leitosRepository;

    @Inject
    private LeitosSearchRepository leitosSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restLeitosMockMvc;

    private Leitos leitos;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LeitosResource leitosResource = new LeitosResource();
        ReflectionTestUtils.setField(leitosResource, "leitosSearchRepository", leitosSearchRepository);
        ReflectionTestUtils.setField(leitosResource, "leitosRepository", leitosRepository);
        this.restLeitosMockMvc = MockMvcBuilders.standaloneSetup(leitosResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        leitosSearchRepository.deleteAll();
        leitos = new Leitos();
        leitos.setLeito(DEFAULT_LEITO);
        leitos.setTipo(DEFAULT_TIPO);
    }

    @Test
    @Transactional
    public void createLeitos() throws Exception {
        int databaseSizeBeforeCreate = leitosRepository.findAll().size();

        // Create the Leitos

        restLeitosMockMvc.perform(post("/api/leitos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(leitos)))
                .andExpect(status().isCreated());

        // Validate the Leitos in the database
        List<Leitos> leitos = leitosRepository.findAll();
        assertThat(leitos).hasSize(databaseSizeBeforeCreate + 1);
        Leitos testLeitos = leitos.get(leitos.size() - 1);
        assertThat(testLeitos.getLeito()).isEqualTo(DEFAULT_LEITO);
        assertThat(testLeitos.getTipo()).isEqualTo(DEFAULT_TIPO);

        // Validate the Leitos in ElasticSearch
        Leitos leitosEs = leitosSearchRepository.findOne(testLeitos.getId());
        assertThat(leitosEs).isEqualToComparingFieldByField(testLeitos);
    }

    @Test
    @Transactional
    public void checkLeitoIsRequired() throws Exception {
        int databaseSizeBeforeTest = leitosRepository.findAll().size();
        // set the field null
        leitos.setLeito(null);

        // Create the Leitos, which fails.

        restLeitosMockMvc.perform(post("/api/leitos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(leitos)))
                .andExpect(status().isBadRequest());

        List<Leitos> leitos = leitosRepository.findAll();
        assertThat(leitos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLeitos() throws Exception {
        // Initialize the database
        leitosRepository.saveAndFlush(leitos);

        // Get all the leitos
        restLeitosMockMvc.perform(get("/api/leitos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(leitos.getId().intValue())))
                .andExpect(jsonPath("$.[*].leito").value(hasItem(DEFAULT_LEITO.toString())))
                .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())));
    }

    @Test
    @Transactional
    public void getLeitos() throws Exception {
        // Initialize the database
        leitosRepository.saveAndFlush(leitos);

        // Get the leitos
        restLeitosMockMvc.perform(get("/api/leitos/{id}", leitos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(leitos.getId().intValue()))
            .andExpect(jsonPath("$.leito").value(DEFAULT_LEITO.toString()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLeitos() throws Exception {
        // Get the leitos
        restLeitosMockMvc.perform(get("/api/leitos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLeitos() throws Exception {
        // Initialize the database
        leitosRepository.saveAndFlush(leitos);
        leitosSearchRepository.save(leitos);
        int databaseSizeBeforeUpdate = leitosRepository.findAll().size();

        // Update the leitos
        Leitos updatedLeitos = new Leitos();
        updatedLeitos.setId(leitos.getId());
        updatedLeitos.setLeito(UPDATED_LEITO);
        updatedLeitos.setTipo(UPDATED_TIPO);

        restLeitosMockMvc.perform(put("/api/leitos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLeitos)))
                .andExpect(status().isOk());

        // Validate the Leitos in the database
        List<Leitos> leitos = leitosRepository.findAll();
        assertThat(leitos).hasSize(databaseSizeBeforeUpdate);
        Leitos testLeitos = leitos.get(leitos.size() - 1);
        assertThat(testLeitos.getLeito()).isEqualTo(UPDATED_LEITO);
        assertThat(testLeitos.getTipo()).isEqualTo(UPDATED_TIPO);

        // Validate the Leitos in ElasticSearch
        Leitos leitosEs = leitosSearchRepository.findOne(testLeitos.getId());
        assertThat(leitosEs).isEqualToComparingFieldByField(testLeitos);
    }

    @Test
    @Transactional
    public void deleteLeitos() throws Exception {
        // Initialize the database
        leitosRepository.saveAndFlush(leitos);
        leitosSearchRepository.save(leitos);
        int databaseSizeBeforeDelete = leitosRepository.findAll().size();

        // Get the leitos
        restLeitosMockMvc.perform(delete("/api/leitos/{id}", leitos.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean leitosExistsInEs = leitosSearchRepository.exists(leitos.getId());
        assertThat(leitosExistsInEs).isFalse();

        // Validate the database is empty
        List<Leitos> leitos = leitosRepository.findAll();
        assertThat(leitos).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLeitos() throws Exception {
        // Initialize the database
        leitosRepository.saveAndFlush(leitos);
        leitosSearchRepository.save(leitos);

        // Search the leitos
        restLeitosMockMvc.perform(get("/api/_search/leitos?query=id:" + leitos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leitos.getId().intValue())))
            .andExpect(jsonPath("$.[*].leito").value(hasItem(DEFAULT_LEITO.toString())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())));
    }
}
