package br.com.rogrs.safh.web.rest;

import br.com.rogrs.safh.SafhApp;

import br.com.rogrs.safh.domain.Posologias;
import br.com.rogrs.safh.repository.PosologiasRepository;
import br.com.rogrs.safh.repository.search.PosologiasSearchRepository;
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
 * Test class for the PosologiasResource REST controller.
 *
 * @see PosologiasResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SafhApp.class)
public class PosologiasResourceIntTest {

    private static final String DEFAULT_POSOLOGIA = "AAAAAAAAAA";
    private static final String UPDATED_POSOLOGIA = "BBBBBBBBBB";

    @Autowired
    private PosologiasRepository posologiasRepository;

    @Autowired
    private PosologiasSearchRepository posologiasSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPosologiasMockMvc;

    private Posologias posologias;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PosologiasResource posologiasResource = new PosologiasResource(posologiasRepository, posologiasSearchRepository);
        this.restPosologiasMockMvc = MockMvcBuilders.standaloneSetup(posologiasResource)
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
    public static Posologias createEntity(EntityManager em) {
        Posologias posologias = new Posologias()
            .posologia(DEFAULT_POSOLOGIA);
        return posologias;
    }

    @Before
    public void initTest() {
        posologiasSearchRepository.deleteAll();
        posologias = createEntity(em);
    }

    @Test
    @Transactional
    public void createPosologias() throws Exception {
        int databaseSizeBeforeCreate = posologiasRepository.findAll().size();

        // Create the Posologias
        restPosologiasMockMvc.perform(post("/api/posologias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(posologias)))
            .andExpect(status().isCreated());

        // Validate the Posologias in the database
        List<Posologias> posologiasList = posologiasRepository.findAll();
        assertThat(posologiasList).hasSize(databaseSizeBeforeCreate + 1);
        Posologias testPosologias = posologiasList.get(posologiasList.size() - 1);
        assertThat(testPosologias.getPosologia()).isEqualTo(DEFAULT_POSOLOGIA);

        // Validate the Posologias in Elasticsearch
        Posologias posologiasEs = posologiasSearchRepository.findOne(testPosologias.getId());
        assertThat(posologiasEs).isEqualToComparingFieldByField(testPosologias);
    }

    @Test
    @Transactional
    public void createPosologiasWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = posologiasRepository.findAll().size();

        // Create the Posologias with an existing ID
        posologias.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPosologiasMockMvc.perform(post("/api/posologias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(posologias)))
            .andExpect(status().isBadRequest());

        // Validate the Posologias in the database
        List<Posologias> posologiasList = posologiasRepository.findAll();
        assertThat(posologiasList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkPosologiaIsRequired() throws Exception {
        int databaseSizeBeforeTest = posologiasRepository.findAll().size();
        // set the field null
        posologias.setPosologia(null);

        // Create the Posologias, which fails.

        restPosologiasMockMvc.perform(post("/api/posologias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(posologias)))
            .andExpect(status().isBadRequest());

        List<Posologias> posologiasList = posologiasRepository.findAll();
        assertThat(posologiasList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPosologias() throws Exception {
        // Initialize the database
        posologiasRepository.saveAndFlush(posologias);

        // Get all the posologiasList
        restPosologiasMockMvc.perform(get("/api/posologias?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(posologias.getId().intValue())))
            .andExpect(jsonPath("$.[*].posologia").value(hasItem(DEFAULT_POSOLOGIA.toString())));
    }

    @Test
    @Transactional
    public void getPosologias() throws Exception {
        // Initialize the database
        posologiasRepository.saveAndFlush(posologias);

        // Get the posologias
        restPosologiasMockMvc.perform(get("/api/posologias/{id}", posologias.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(posologias.getId().intValue()))
            .andExpect(jsonPath("$.posologia").value(DEFAULT_POSOLOGIA.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPosologias() throws Exception {
        // Get the posologias
        restPosologiasMockMvc.perform(get("/api/posologias/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePosologias() throws Exception {
        // Initialize the database
        posologiasRepository.saveAndFlush(posologias);
        posologiasSearchRepository.save(posologias);
        int databaseSizeBeforeUpdate = posologiasRepository.findAll().size();

        // Update the posologias
        Posologias updatedPosologias = posologiasRepository.findOne(posologias.getId());
        updatedPosologias
            .posologia(UPDATED_POSOLOGIA);

        restPosologiasMockMvc.perform(put("/api/posologias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPosologias)))
            .andExpect(status().isOk());

        // Validate the Posologias in the database
        List<Posologias> posologiasList = posologiasRepository.findAll();
        assertThat(posologiasList).hasSize(databaseSizeBeforeUpdate);
        Posologias testPosologias = posologiasList.get(posologiasList.size() - 1);
        assertThat(testPosologias.getPosologia()).isEqualTo(UPDATED_POSOLOGIA);

        // Validate the Posologias in Elasticsearch
        Posologias posologiasEs = posologiasSearchRepository.findOne(testPosologias.getId());
        assertThat(posologiasEs).isEqualToComparingFieldByField(testPosologias);
    }

    @Test
    @Transactional
    public void updateNonExistingPosologias() throws Exception {
        int databaseSizeBeforeUpdate = posologiasRepository.findAll().size();

        // Create the Posologias

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPosologiasMockMvc.perform(put("/api/posologias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(posologias)))
            .andExpect(status().isCreated());

        // Validate the Posologias in the database
        List<Posologias> posologiasList = posologiasRepository.findAll();
        assertThat(posologiasList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePosologias() throws Exception {
        // Initialize the database
        posologiasRepository.saveAndFlush(posologias);
        posologiasSearchRepository.save(posologias);
        int databaseSizeBeforeDelete = posologiasRepository.findAll().size();

        // Get the posologias
        restPosologiasMockMvc.perform(delete("/api/posologias/{id}", posologias.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean posologiasExistsInEs = posologiasSearchRepository.exists(posologias.getId());
        assertThat(posologiasExistsInEs).isFalse();

        // Validate the database is empty
        List<Posologias> posologiasList = posologiasRepository.findAll();
        assertThat(posologiasList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPosologias() throws Exception {
        // Initialize the database
        posologiasRepository.saveAndFlush(posologias);
        posologiasSearchRepository.save(posologias);

        // Search the posologias
        restPosologiasMockMvc.perform(get("/api/_search/posologias?query=id:" + posologias.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(posologias.getId().intValue())))
            .andExpect(jsonPath("$.[*].posologia").value(hasItem(DEFAULT_POSOLOGIA.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Posologias.class);
        Posologias posologias1 = new Posologias();
        posologias1.setId(1L);
        Posologias posologias2 = new Posologias();
        posologias2.setId(posologias1.getId());
        assertThat(posologias1).isEqualTo(posologias2);
        posologias2.setId(2L);
        assertThat(posologias1).isNotEqualTo(posologias2);
        posologias1.setId(null);
        assertThat(posologias1).isNotEqualTo(posologias2);
    }
}
