package br.com.rogrs.safh.web.rest;

import br.com.rogrs.safh.SafhApp;

import br.com.rogrs.safh.domain.Clinicas;
import br.com.rogrs.safh.repository.ClinicasRepository;
import br.com.rogrs.safh.repository.search.ClinicasSearchRepository;
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
 * Test class for the ClinicasResource REST controller.
 *
 * @see ClinicasResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SafhApp.class)
public class ClinicasResourceIntTest {

    private static final String DEFAULT_CLINICA = "AAAAAAAAAA";
    private static final String UPDATED_CLINICA = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    @Autowired
    private ClinicasRepository clinicasRepository;

    @Autowired
    private ClinicasSearchRepository clinicasSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restClinicasMockMvc;

    private Clinicas clinicas;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ClinicasResource clinicasResource = new ClinicasResource(clinicasRepository, clinicasSearchRepository);
        this.restClinicasMockMvc = MockMvcBuilders.standaloneSetup(clinicasResource)
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
    public static Clinicas createEntity(EntityManager em) {
        Clinicas clinicas = new Clinicas()
            .clinica(DEFAULT_CLINICA)
            .descricao(DEFAULT_DESCRICAO);
        return clinicas;
    }

    @Before
    public void initTest() {
        clinicasSearchRepository.deleteAll();
        clinicas = createEntity(em);
    }

    @Test
    @Transactional
    public void createClinicas() throws Exception {
        int databaseSizeBeforeCreate = clinicasRepository.findAll().size();

        // Create the Clinicas
        restClinicasMockMvc.perform(post("/api/clinicas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clinicas)))
            .andExpect(status().isCreated());

        // Validate the Clinicas in the database
        List<Clinicas> clinicasList = clinicasRepository.findAll();
        assertThat(clinicasList).hasSize(databaseSizeBeforeCreate + 1);
        Clinicas testClinicas = clinicasList.get(clinicasList.size() - 1);
        assertThat(testClinicas.getClinica()).isEqualTo(DEFAULT_CLINICA);
        assertThat(testClinicas.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);

        // Validate the Clinicas in Elasticsearch
        Clinicas clinicasEs = clinicasSearchRepository.findOne(testClinicas.getId());
        assertThat(clinicasEs).isEqualToComparingFieldByField(testClinicas);
    }

    @Test
    @Transactional
    public void createClinicasWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = clinicasRepository.findAll().size();

        // Create the Clinicas with an existing ID
        clinicas.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClinicasMockMvc.perform(post("/api/clinicas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clinicas)))
            .andExpect(status().isBadRequest());

        // Validate the Clinicas in the database
        List<Clinicas> clinicasList = clinicasRepository.findAll();
        assertThat(clinicasList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkClinicaIsRequired() throws Exception {
        int databaseSizeBeforeTest = clinicasRepository.findAll().size();
        // set the field null
        clinicas.setClinica(null);

        // Create the Clinicas, which fails.

        restClinicasMockMvc.perform(post("/api/clinicas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clinicas)))
            .andExpect(status().isBadRequest());

        List<Clinicas> clinicasList = clinicasRepository.findAll();
        assertThat(clinicasList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllClinicas() throws Exception {
        // Initialize the database
        clinicasRepository.saveAndFlush(clinicas);

        // Get all the clinicasList
        restClinicasMockMvc.perform(get("/api/clinicas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clinicas.getId().intValue())))
            .andExpect(jsonPath("$.[*].clinica").value(hasItem(DEFAULT_CLINICA.toString())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())));
    }

    @Test
    @Transactional
    public void getClinicas() throws Exception {
        // Initialize the database
        clinicasRepository.saveAndFlush(clinicas);

        // Get the clinicas
        restClinicasMockMvc.perform(get("/api/clinicas/{id}", clinicas.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(clinicas.getId().intValue()))
            .andExpect(jsonPath("$.clinica").value(DEFAULT_CLINICA.toString()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingClinicas() throws Exception {
        // Get the clinicas
        restClinicasMockMvc.perform(get("/api/clinicas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClinicas() throws Exception {
        // Initialize the database
        clinicasRepository.saveAndFlush(clinicas);
        clinicasSearchRepository.save(clinicas);
        int databaseSizeBeforeUpdate = clinicasRepository.findAll().size();

        // Update the clinicas
        Clinicas updatedClinicas = clinicasRepository.findOne(clinicas.getId());
        updatedClinicas
            .clinica(UPDATED_CLINICA)
            .descricao(UPDATED_DESCRICAO);

        restClinicasMockMvc.perform(put("/api/clinicas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedClinicas)))
            .andExpect(status().isOk());

        // Validate the Clinicas in the database
        List<Clinicas> clinicasList = clinicasRepository.findAll();
        assertThat(clinicasList).hasSize(databaseSizeBeforeUpdate);
        Clinicas testClinicas = clinicasList.get(clinicasList.size() - 1);
        assertThat(testClinicas.getClinica()).isEqualTo(UPDATED_CLINICA);
        assertThat(testClinicas.getDescricao()).isEqualTo(UPDATED_DESCRICAO);

        // Validate the Clinicas in Elasticsearch
        Clinicas clinicasEs = clinicasSearchRepository.findOne(testClinicas.getId());
        assertThat(clinicasEs).isEqualToComparingFieldByField(testClinicas);
    }

    @Test
    @Transactional
    public void updateNonExistingClinicas() throws Exception {
        int databaseSizeBeforeUpdate = clinicasRepository.findAll().size();

        // Create the Clinicas

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restClinicasMockMvc.perform(put("/api/clinicas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clinicas)))
            .andExpect(status().isCreated());

        // Validate the Clinicas in the database
        List<Clinicas> clinicasList = clinicasRepository.findAll();
        assertThat(clinicasList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteClinicas() throws Exception {
        // Initialize the database
        clinicasRepository.saveAndFlush(clinicas);
        clinicasSearchRepository.save(clinicas);
        int databaseSizeBeforeDelete = clinicasRepository.findAll().size();

        // Get the clinicas
        restClinicasMockMvc.perform(delete("/api/clinicas/{id}", clinicas.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean clinicasExistsInEs = clinicasSearchRepository.exists(clinicas.getId());
        assertThat(clinicasExistsInEs).isFalse();

        // Validate the database is empty
        List<Clinicas> clinicasList = clinicasRepository.findAll();
        assertThat(clinicasList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchClinicas() throws Exception {
        // Initialize the database
        clinicasRepository.saveAndFlush(clinicas);
        clinicasSearchRepository.save(clinicas);

        // Search the clinicas
        restClinicasMockMvc.perform(get("/api/_search/clinicas?query=id:" + clinicas.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clinicas.getId().intValue())))
            .andExpect(jsonPath("$.[*].clinica").value(hasItem(DEFAULT_CLINICA.toString())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Clinicas.class);
        Clinicas clinicas1 = new Clinicas();
        clinicas1.setId(1L);
        Clinicas clinicas2 = new Clinicas();
        clinicas2.setId(clinicas1.getId());
        assertThat(clinicas1).isEqualTo(clinicas2);
        clinicas2.setId(2L);
        assertThat(clinicas1).isNotEqualTo(clinicas2);
        clinicas1.setId(null);
        assertThat(clinicas1).isNotEqualTo(clinicas2);
    }
}
