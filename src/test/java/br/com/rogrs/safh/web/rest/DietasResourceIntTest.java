package br.com.rogrs.safh.web.rest;

import br.com.rogrs.safh.SafhApp;
import br.com.rogrs.safh.domain.Dietas;
import br.com.rogrs.safh.repository.DietasRepository;
import br.com.rogrs.safh.repository.search.DietasSearchRepository;

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
 * Test class for the DietasResource REST controller.
 *
 * @see DietasResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SafhApp.class)
@WebAppConfiguration
@IntegrationTest
public class DietasResourceIntTest {

    private static final String DEFAULT_DIETA = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_DIETA = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private DietasRepository dietasRepository;

    @Inject
    private DietasSearchRepository dietasSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDietasMockMvc;

    private Dietas dietas;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DietasResource dietasResource = new DietasResource();
        ReflectionTestUtils.setField(dietasResource, "dietasSearchRepository", dietasSearchRepository);
        ReflectionTestUtils.setField(dietasResource, "dietasRepository", dietasRepository);
        this.restDietasMockMvc = MockMvcBuilders.standaloneSetup(dietasResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        dietasSearchRepository.deleteAll();
        dietas = new Dietas();
        dietas.setDieta(DEFAULT_DIETA);
        dietas.setDescricao(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    public void createDietas() throws Exception {
        int databaseSizeBeforeCreate = dietasRepository.findAll().size();

        // Create the Dietas

        restDietasMockMvc.perform(post("/api/dietas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dietas)))
                .andExpect(status().isCreated());

        // Validate the Dietas in the database
        List<Dietas> dietas = dietasRepository.findAll();
        assertThat(dietas).hasSize(databaseSizeBeforeCreate + 1);
        Dietas testDietas = dietas.get(dietas.size() - 1);
        assertThat(testDietas.getDieta()).isEqualTo(DEFAULT_DIETA);
        assertThat(testDietas.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);

        // Validate the Dietas in ElasticSearch
        Dietas dietasEs = dietasSearchRepository.findOne(testDietas.getId());
        assertThat(dietasEs).isEqualToComparingFieldByField(testDietas);
    }

    @Test
    @Transactional
    public void checkDietaIsRequired() throws Exception {
        int databaseSizeBeforeTest = dietasRepository.findAll().size();
        // set the field null
        dietas.setDieta(null);

        // Create the Dietas, which fails.

        restDietasMockMvc.perform(post("/api/dietas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dietas)))
                .andExpect(status().isBadRequest());

        List<Dietas> dietas = dietasRepository.findAll();
        assertThat(dietas).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDietas() throws Exception {
        // Initialize the database
        dietasRepository.saveAndFlush(dietas);

        // Get all the dietas
        restDietasMockMvc.perform(get("/api/dietas?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(dietas.getId().intValue())))
                .andExpect(jsonPath("$.[*].dieta").value(hasItem(DEFAULT_DIETA.toString())))
                .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())));
    }

    @Test
    @Transactional
    public void getDietas() throws Exception {
        // Initialize the database
        dietasRepository.saveAndFlush(dietas);

        // Get the dietas
        restDietasMockMvc.perform(get("/api/dietas/{id}", dietas.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(dietas.getId().intValue()))
            .andExpect(jsonPath("$.dieta").value(DEFAULT_DIETA.toString()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDietas() throws Exception {
        // Get the dietas
        restDietasMockMvc.perform(get("/api/dietas/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDietas() throws Exception {
        // Initialize the database
        dietasRepository.saveAndFlush(dietas);
        dietasSearchRepository.save(dietas);
        int databaseSizeBeforeUpdate = dietasRepository.findAll().size();

        // Update the dietas
        Dietas updatedDietas = new Dietas();
        updatedDietas.setId(dietas.getId());
        updatedDietas.setDieta(UPDATED_DIETA);
        updatedDietas.setDescricao(UPDATED_DESCRICAO);

        restDietasMockMvc.perform(put("/api/dietas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDietas)))
                .andExpect(status().isOk());

        // Validate the Dietas in the database
        List<Dietas> dietas = dietasRepository.findAll();
        assertThat(dietas).hasSize(databaseSizeBeforeUpdate);
        Dietas testDietas = dietas.get(dietas.size() - 1);
        assertThat(testDietas.getDieta()).isEqualTo(UPDATED_DIETA);
        assertThat(testDietas.getDescricao()).isEqualTo(UPDATED_DESCRICAO);

        // Validate the Dietas in ElasticSearch
        Dietas dietasEs = dietasSearchRepository.findOne(testDietas.getId());
        assertThat(dietasEs).isEqualToComparingFieldByField(testDietas);
    }

    @Test
    @Transactional
    public void deleteDietas() throws Exception {
        // Initialize the database
        dietasRepository.saveAndFlush(dietas);
        dietasSearchRepository.save(dietas);
        int databaseSizeBeforeDelete = dietasRepository.findAll().size();

        // Get the dietas
        restDietasMockMvc.perform(delete("/api/dietas/{id}", dietas.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean dietasExistsInEs = dietasSearchRepository.exists(dietas.getId());
        assertThat(dietasExistsInEs).isFalse();

        // Validate the database is empty
        List<Dietas> dietas = dietasRepository.findAll();
        assertThat(dietas).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDietas() throws Exception {
        // Initialize the database
        dietasRepository.saveAndFlush(dietas);
        dietasSearchRepository.save(dietas);

        // Search the dietas
        restDietasMockMvc.perform(get("/api/_search/dietas?query=id:" + dietas.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dietas.getId().intValue())))
            .andExpect(jsonPath("$.[*].dieta").value(hasItem(DEFAULT_DIETA.toString())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())));
    }
}
