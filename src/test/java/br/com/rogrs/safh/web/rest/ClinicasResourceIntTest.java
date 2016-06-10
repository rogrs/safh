package br.com.rogrs.safh.web.rest;

import br.com.rogrs.safh.SafhApp;
import br.com.rogrs.safh.domain.Clinicas;
import br.com.rogrs.safh.repository.ClinicasRepository;
import br.com.rogrs.safh.repository.search.ClinicasSearchRepository;

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
 * Test class for the ClinicasResource REST controller.
 *
 * @see ClinicasResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SafhApp.class)
@WebAppConfiguration
@IntegrationTest
public class ClinicasResourceIntTest {

    private static final String DEFAULT_CLINICA = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_CLINICA = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private ClinicasRepository clinicasRepository;

    @Inject
    private ClinicasSearchRepository clinicasSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restClinicasMockMvc;

    private Clinicas clinicas;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ClinicasResource clinicasResource = new ClinicasResource();
        ReflectionTestUtils.setField(clinicasResource, "clinicasSearchRepository", clinicasSearchRepository);
        ReflectionTestUtils.setField(clinicasResource, "clinicasRepository", clinicasRepository);
        this.restClinicasMockMvc = MockMvcBuilders.standaloneSetup(clinicasResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        clinicasSearchRepository.deleteAll();
        clinicas = new Clinicas();
        clinicas.setClinica(DEFAULT_CLINICA);
        clinicas.setDescricao(DEFAULT_DESCRICAO);
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
        List<Clinicas> clinicas = clinicasRepository.findAll();
        assertThat(clinicas).hasSize(databaseSizeBeforeCreate + 1);
        Clinicas testClinicas = clinicas.get(clinicas.size() - 1);
        assertThat(testClinicas.getClinica()).isEqualTo(DEFAULT_CLINICA);
        assertThat(testClinicas.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);

        // Validate the Clinicas in ElasticSearch
        Clinicas clinicasEs = clinicasSearchRepository.findOne(testClinicas.getId());
        assertThat(clinicasEs).isEqualToComparingFieldByField(testClinicas);
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

        List<Clinicas> clinicas = clinicasRepository.findAll();
        assertThat(clinicas).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllClinicas() throws Exception {
        // Initialize the database
        clinicasRepository.saveAndFlush(clinicas);

        // Get all the clinicas
        restClinicasMockMvc.perform(get("/api/clinicas?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
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
        Clinicas updatedClinicas = new Clinicas();
        updatedClinicas.setId(clinicas.getId());
        updatedClinicas.setClinica(UPDATED_CLINICA);
        updatedClinicas.setDescricao(UPDATED_DESCRICAO);

        restClinicasMockMvc.perform(put("/api/clinicas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedClinicas)))
                .andExpect(status().isOk());

        // Validate the Clinicas in the database
        List<Clinicas> clinicas = clinicasRepository.findAll();
        assertThat(clinicas).hasSize(databaseSizeBeforeUpdate);
        Clinicas testClinicas = clinicas.get(clinicas.size() - 1);
        assertThat(testClinicas.getClinica()).isEqualTo(UPDATED_CLINICA);
        assertThat(testClinicas.getDescricao()).isEqualTo(UPDATED_DESCRICAO);

        // Validate the Clinicas in ElasticSearch
        Clinicas clinicasEs = clinicasSearchRepository.findOne(testClinicas.getId());
        assertThat(clinicasEs).isEqualToComparingFieldByField(testClinicas);
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

        // Validate ElasticSearch is empty
        boolean clinicasExistsInEs = clinicasSearchRepository.exists(clinicas.getId());
        assertThat(clinicasExistsInEs).isFalse();

        // Validate the database is empty
        List<Clinicas> clinicas = clinicasRepository.findAll();
        assertThat(clinicas).hasSize(databaseSizeBeforeDelete - 1);
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clinicas.getId().intValue())))
            .andExpect(jsonPath("$.[*].clinica").value(hasItem(DEFAULT_CLINICA.toString())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())));
    }
}
