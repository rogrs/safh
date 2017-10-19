package br.com.rogrs.safh.web.rest;

import br.com.rogrs.safh.SafhApp;

import br.com.rogrs.safh.domain.Fabricantes;
import br.com.rogrs.safh.repository.FabricantesRepository;
import br.com.rogrs.safh.repository.search.FabricantesSearchRepository;
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
 * Test class for the FabricantesResource REST controller.
 *
 * @see FabricantesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SafhApp.class)
public class FabricantesResourceIntTest {

    private static final String DEFAULT_FABRICANTE = "AAAAAAAAAA";
    private static final String UPDATED_FABRICANTE = "BBBBBBBBBB";

    @Autowired
    private FabricantesRepository fabricantesRepository;

    @Autowired
    private FabricantesSearchRepository fabricantesSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFabricantesMockMvc;

    private Fabricantes fabricantes;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FabricantesResource fabricantesResource = new FabricantesResource(fabricantesRepository, fabricantesSearchRepository);
        this.restFabricantesMockMvc = MockMvcBuilders.standaloneSetup(fabricantesResource)
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
    public static Fabricantes createEntity(EntityManager em) {
        Fabricantes fabricantes = new Fabricantes()
            .fabricante(DEFAULT_FABRICANTE);
        return fabricantes;
    }

    @Before
    public void initTest() {
        fabricantesSearchRepository.deleteAll();
        fabricantes = createEntity(em);
    }

    @Test
    @Transactional
    public void createFabricantes() throws Exception {
        int databaseSizeBeforeCreate = fabricantesRepository.findAll().size();

        // Create the Fabricantes
        restFabricantesMockMvc.perform(post("/api/fabricantes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fabricantes)))
            .andExpect(status().isCreated());

        // Validate the Fabricantes in the database
        List<Fabricantes> fabricantesList = fabricantesRepository.findAll();
        assertThat(fabricantesList).hasSize(databaseSizeBeforeCreate + 1);
        Fabricantes testFabricantes = fabricantesList.get(fabricantesList.size() - 1);
        assertThat(testFabricantes.getFabricante()).isEqualTo(DEFAULT_FABRICANTE);

        // Validate the Fabricantes in Elasticsearch
        Fabricantes fabricantesEs = fabricantesSearchRepository.findOne(testFabricantes.getId());
        assertThat(fabricantesEs).isEqualToComparingFieldByField(testFabricantes);
    }

    @Test
    @Transactional
    public void createFabricantesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fabricantesRepository.findAll().size();

        // Create the Fabricantes with an existing ID
        fabricantes.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFabricantesMockMvc.perform(post("/api/fabricantes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fabricantes)))
            .andExpect(status().isBadRequest());

        // Validate the Fabricantes in the database
        List<Fabricantes> fabricantesList = fabricantesRepository.findAll();
        assertThat(fabricantesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkFabricanteIsRequired() throws Exception {
        int databaseSizeBeforeTest = fabricantesRepository.findAll().size();
        // set the field null
        fabricantes.setFabricante(null);

        // Create the Fabricantes, which fails.

        restFabricantesMockMvc.perform(post("/api/fabricantes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fabricantes)))
            .andExpect(status().isBadRequest());

        List<Fabricantes> fabricantesList = fabricantesRepository.findAll();
        assertThat(fabricantesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFabricantes() throws Exception {
        // Initialize the database
        fabricantesRepository.saveAndFlush(fabricantes);

        // Get all the fabricantesList
        restFabricantesMockMvc.perform(get("/api/fabricantes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fabricantes.getId().intValue())))
            .andExpect(jsonPath("$.[*].fabricante").value(hasItem(DEFAULT_FABRICANTE.toString())));
    }

    @Test
    @Transactional
    public void getFabricantes() throws Exception {
        // Initialize the database
        fabricantesRepository.saveAndFlush(fabricantes);

        // Get the fabricantes
        restFabricantesMockMvc.perform(get("/api/fabricantes/{id}", fabricantes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(fabricantes.getId().intValue()))
            .andExpect(jsonPath("$.fabricante").value(DEFAULT_FABRICANTE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFabricantes() throws Exception {
        // Get the fabricantes
        restFabricantesMockMvc.perform(get("/api/fabricantes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFabricantes() throws Exception {
        // Initialize the database
        fabricantesRepository.saveAndFlush(fabricantes);
        fabricantesSearchRepository.save(fabricantes);
        int databaseSizeBeforeUpdate = fabricantesRepository.findAll().size();

        // Update the fabricantes
        Fabricantes updatedFabricantes = fabricantesRepository.findOne(fabricantes.getId());
        updatedFabricantes
            .fabricante(UPDATED_FABRICANTE);

        restFabricantesMockMvc.perform(put("/api/fabricantes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFabricantes)))
            .andExpect(status().isOk());

        // Validate the Fabricantes in the database
        List<Fabricantes> fabricantesList = fabricantesRepository.findAll();
        assertThat(fabricantesList).hasSize(databaseSizeBeforeUpdate);
        Fabricantes testFabricantes = fabricantesList.get(fabricantesList.size() - 1);
        assertThat(testFabricantes.getFabricante()).isEqualTo(UPDATED_FABRICANTE);

        // Validate the Fabricantes in Elasticsearch
        Fabricantes fabricantesEs = fabricantesSearchRepository.findOne(testFabricantes.getId());
        assertThat(fabricantesEs).isEqualToComparingFieldByField(testFabricantes);
    }

    @Test
    @Transactional
    public void updateNonExistingFabricantes() throws Exception {
        int databaseSizeBeforeUpdate = fabricantesRepository.findAll().size();

        // Create the Fabricantes

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFabricantesMockMvc.perform(put("/api/fabricantes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fabricantes)))
            .andExpect(status().isCreated());

        // Validate the Fabricantes in the database
        List<Fabricantes> fabricantesList = fabricantesRepository.findAll();
        assertThat(fabricantesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFabricantes() throws Exception {
        // Initialize the database
        fabricantesRepository.saveAndFlush(fabricantes);
        fabricantesSearchRepository.save(fabricantes);
        int databaseSizeBeforeDelete = fabricantesRepository.findAll().size();

        // Get the fabricantes
        restFabricantesMockMvc.perform(delete("/api/fabricantes/{id}", fabricantes.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean fabricantesExistsInEs = fabricantesSearchRepository.exists(fabricantes.getId());
        assertThat(fabricantesExistsInEs).isFalse();

        // Validate the database is empty
        List<Fabricantes> fabricantesList = fabricantesRepository.findAll();
        assertThat(fabricantesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchFabricantes() throws Exception {
        // Initialize the database
        fabricantesRepository.saveAndFlush(fabricantes);
        fabricantesSearchRepository.save(fabricantes);

        // Search the fabricantes
        restFabricantesMockMvc.perform(get("/api/_search/fabricantes?query=id:" + fabricantes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fabricantes.getId().intValue())))
            .andExpect(jsonPath("$.[*].fabricante").value(hasItem(DEFAULT_FABRICANTE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Fabricantes.class);
        Fabricantes fabricantes1 = new Fabricantes();
        fabricantes1.setId(1L);
        Fabricantes fabricantes2 = new Fabricantes();
        fabricantes2.setId(fabricantes1.getId());
        assertThat(fabricantes1).isEqualTo(fabricantes2);
        fabricantes2.setId(2L);
        assertThat(fabricantes1).isNotEqualTo(fabricantes2);
        fabricantes1.setId(null);
        assertThat(fabricantes1).isNotEqualTo(fabricantes2);
    }
}
