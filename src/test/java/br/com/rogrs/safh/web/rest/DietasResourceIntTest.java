package br.com.rogrs.safh.web.rest;

import br.com.rogrs.safh.SafhApp;

import br.com.rogrs.safh.domain.Dietas;
import br.com.rogrs.safh.repository.DietasRepository;
import br.com.rogrs.safh.repository.search.DietasSearchRepository;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DietasResource REST controller.
 *
 * @see DietasResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SafhApp.class)
public class DietasResourceIntTest {

    private static final String DEFAULT_DIETA = "AAAAAAAAAA";
    private static final String UPDATED_DIETA = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    @Autowired
    private DietasRepository dietasRepository;

    @Autowired
    private DietasSearchRepository dietasSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDietasMockMvc;

    private Dietas dietas;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DietasResource dietasResource = new DietasResource(dietasRepository, dietasSearchRepository);
        this.restDietasMockMvc = MockMvcBuilders.standaloneSetup(dietasResource)
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
    public static Dietas createEntity(EntityManager em) {
        Dietas dietas = new Dietas()
            .dieta(DEFAULT_DIETA)
            .descricao(DEFAULT_DESCRICAO);
        return dietas;
    }

    @Before
    public void initTest() {
        dietasSearchRepository.deleteAll();
        dietas = createEntity(em);
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
        List<Dietas> dietasList = dietasRepository.findAll();
        assertThat(dietasList).hasSize(databaseSizeBeforeCreate + 1);
        Dietas testDietas = dietasList.get(dietasList.size() - 1);
        assertThat(testDietas.getDieta()).isEqualTo(DEFAULT_DIETA);
        assertThat(testDietas.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);

        // Validate the Dietas in Elasticsearch
        Dietas dietasEs = dietasSearchRepository.findOne(testDietas.getId());
        assertThat(dietasEs).isEqualToComparingFieldByField(testDietas);
    }

    @Test
    @Transactional
    public void createDietasWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dietasRepository.findAll().size();

        // Create the Dietas with an existing ID
        dietas.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDietasMockMvc.perform(post("/api/dietas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dietas)))
            .andExpect(status().isBadRequest());

        // Validate the Dietas in the database
        List<Dietas> dietasList = dietasRepository.findAll();
        assertThat(dietasList).hasSize(databaseSizeBeforeCreate);
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

        List<Dietas> dietasList = dietasRepository.findAll();
        assertThat(dietasList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDietas() throws Exception {
        // Initialize the database
        dietasRepository.saveAndFlush(dietas);

        // Get all the dietasList
        restDietasMockMvc.perform(get("/api/dietas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
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
        Dietas updatedDietas = dietasRepository.findOne(dietas.getId());
        updatedDietas
            .dieta(UPDATED_DIETA)
            .descricao(UPDATED_DESCRICAO);

        restDietasMockMvc.perform(put("/api/dietas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDietas)))
            .andExpect(status().isOk());

        // Validate the Dietas in the database
        List<Dietas> dietasList = dietasRepository.findAll();
        assertThat(dietasList).hasSize(databaseSizeBeforeUpdate);
        Dietas testDietas = dietasList.get(dietasList.size() - 1);
        assertThat(testDietas.getDieta()).isEqualTo(UPDATED_DIETA);
        assertThat(testDietas.getDescricao()).isEqualTo(UPDATED_DESCRICAO);

        // Validate the Dietas in Elasticsearch
        Dietas dietasEs = dietasSearchRepository.findOne(testDietas.getId());
        assertThat(dietasEs).isEqualToComparingFieldByField(testDietas);
    }

    @Test
    @Transactional
    public void updateNonExistingDietas() throws Exception {
        int databaseSizeBeforeUpdate = dietasRepository.findAll().size();

        // Create the Dietas

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDietasMockMvc.perform(put("/api/dietas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dietas)))
            .andExpect(status().isCreated());

        // Validate the Dietas in the database
        List<Dietas> dietasList = dietasRepository.findAll();
        assertThat(dietasList).hasSize(databaseSizeBeforeUpdate + 1);
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

        // Validate Elasticsearch is empty
        boolean dietasExistsInEs = dietasSearchRepository.exists(dietas.getId());
        assertThat(dietasExistsInEs).isFalse();

        // Validate the database is empty
        List<Dietas> dietasList = dietasRepository.findAll();
        assertThat(dietasList).hasSize(databaseSizeBeforeDelete - 1);
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dietas.getId().intValue())))
            .andExpect(jsonPath("$.[*].dieta").value(hasItem(DEFAULT_DIETA.toString())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dietas.class);
        Dietas dietas1 = new Dietas();
        dietas1.setId(1L);
        Dietas dietas2 = new Dietas();
        dietas2.setId(dietas1.getId());
        assertThat(dietas1).isEqualTo(dietas2);
        dietas2.setId(2L);
        assertThat(dietas1).isNotEqualTo(dietas2);
        dietas1.setId(null);
        assertThat(dietas1).isNotEqualTo(dietas2);
    }
}
