package br.com.rogrs.safh.web.rest;

import br.com.rogrs.safh.SafhApp;

import br.com.rogrs.safh.domain.Especialidades;
import br.com.rogrs.safh.repository.EspecialidadesRepository;
import br.com.rogrs.safh.repository.search.EspecialidadesSearchRepository;
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
 * Test class for the EspecialidadesResource REST controller.
 *
 * @see EspecialidadesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SafhApp.class)
public class EspecialidadesResourceIntTest {

    private static final String DEFAULT_ESPECIALIDADE = "AAAAAAAAAA";
    private static final String UPDATED_ESPECIALIDADE = "BBBBBBBBBB";

    @Autowired
    private EspecialidadesRepository especialidadesRepository;

    @Autowired
    private EspecialidadesSearchRepository especialidadesSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEspecialidadesMockMvc;

    private Especialidades especialidades;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EspecialidadesResource especialidadesResource = new EspecialidadesResource(especialidadesRepository, especialidadesSearchRepository);
        this.restEspecialidadesMockMvc = MockMvcBuilders.standaloneSetup(especialidadesResource)
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
    public static Especialidades createEntity(EntityManager em) {
        Especialidades especialidades = new Especialidades()
            .especialidade(DEFAULT_ESPECIALIDADE);
        return especialidades;
    }

    @Before
    public void initTest() {
        especialidadesSearchRepository.deleteAll();
        especialidades = createEntity(em);
    }

    @Test
    @Transactional
    public void createEspecialidades() throws Exception {
        int databaseSizeBeforeCreate = especialidadesRepository.findAll().size();

        // Create the Especialidades
        restEspecialidadesMockMvc.perform(post("/api/especialidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(especialidades)))
            .andExpect(status().isCreated());

        // Validate the Especialidades in the database
        List<Especialidades> especialidadesList = especialidadesRepository.findAll();
        assertThat(especialidadesList).hasSize(databaseSizeBeforeCreate + 1);
        Especialidades testEspecialidades = especialidadesList.get(especialidadesList.size() - 1);
        assertThat(testEspecialidades.getEspecialidade()).isEqualTo(DEFAULT_ESPECIALIDADE);

        // Validate the Especialidades in Elasticsearch
        Especialidades especialidadesEs = especialidadesSearchRepository.findOne(testEspecialidades.getId());
        assertThat(especialidadesEs).isEqualToComparingFieldByField(testEspecialidades);
    }

    @Test
    @Transactional
    public void createEspecialidadesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = especialidadesRepository.findAll().size();

        // Create the Especialidades with an existing ID
        especialidades.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEspecialidadesMockMvc.perform(post("/api/especialidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(especialidades)))
            .andExpect(status().isBadRequest());

        // Validate the Especialidades in the database
        List<Especialidades> especialidadesList = especialidadesRepository.findAll();
        assertThat(especialidadesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkEspecialidadeIsRequired() throws Exception {
        int databaseSizeBeforeTest = especialidadesRepository.findAll().size();
        // set the field null
        especialidades.setEspecialidade(null);

        // Create the Especialidades, which fails.

        restEspecialidadesMockMvc.perform(post("/api/especialidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(especialidades)))
            .andExpect(status().isBadRequest());

        List<Especialidades> especialidadesList = especialidadesRepository.findAll();
        assertThat(especialidadesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEspecialidades() throws Exception {
        // Initialize the database
        especialidadesRepository.saveAndFlush(especialidades);

        // Get all the especialidadesList
        restEspecialidadesMockMvc.perform(get("/api/especialidades?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(especialidades.getId().intValue())))
            .andExpect(jsonPath("$.[*].especialidade").value(hasItem(DEFAULT_ESPECIALIDADE.toString())));
    }

    @Test
    @Transactional
    public void getEspecialidades() throws Exception {
        // Initialize the database
        especialidadesRepository.saveAndFlush(especialidades);

        // Get the especialidades
        restEspecialidadesMockMvc.perform(get("/api/especialidades/{id}", especialidades.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(especialidades.getId().intValue()))
            .andExpect(jsonPath("$.especialidade").value(DEFAULT_ESPECIALIDADE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEspecialidades() throws Exception {
        // Get the especialidades
        restEspecialidadesMockMvc.perform(get("/api/especialidades/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEspecialidades() throws Exception {
        // Initialize the database
        especialidadesRepository.saveAndFlush(especialidades);
        especialidadesSearchRepository.save(especialidades);
        int databaseSizeBeforeUpdate = especialidadesRepository.findAll().size();

        // Update the especialidades
        Especialidades updatedEspecialidades = especialidadesRepository.findOne(especialidades.getId());
        updatedEspecialidades
            .especialidade(UPDATED_ESPECIALIDADE);

        restEspecialidadesMockMvc.perform(put("/api/especialidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEspecialidades)))
            .andExpect(status().isOk());

        // Validate the Especialidades in the database
        List<Especialidades> especialidadesList = especialidadesRepository.findAll();
        assertThat(especialidadesList).hasSize(databaseSizeBeforeUpdate);
        Especialidades testEspecialidades = especialidadesList.get(especialidadesList.size() - 1);
        assertThat(testEspecialidades.getEspecialidade()).isEqualTo(UPDATED_ESPECIALIDADE);

        // Validate the Especialidades in Elasticsearch
        Especialidades especialidadesEs = especialidadesSearchRepository.findOne(testEspecialidades.getId());
        assertThat(especialidadesEs).isEqualToComparingFieldByField(testEspecialidades);
    }

    @Test
    @Transactional
    public void updateNonExistingEspecialidades() throws Exception {
        int databaseSizeBeforeUpdate = especialidadesRepository.findAll().size();

        // Create the Especialidades

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEspecialidadesMockMvc.perform(put("/api/especialidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(especialidades)))
            .andExpect(status().isCreated());

        // Validate the Especialidades in the database
        List<Especialidades> especialidadesList = especialidadesRepository.findAll();
        assertThat(especialidadesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEspecialidades() throws Exception {
        // Initialize the database
        especialidadesRepository.saveAndFlush(especialidades);
        especialidadesSearchRepository.save(especialidades);
        int databaseSizeBeforeDelete = especialidadesRepository.findAll().size();

        // Get the especialidades
        restEspecialidadesMockMvc.perform(delete("/api/especialidades/{id}", especialidades.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean especialidadesExistsInEs = especialidadesSearchRepository.exists(especialidades.getId());
        assertThat(especialidadesExistsInEs).isFalse();

        // Validate the database is empty
        List<Especialidades> especialidadesList = especialidadesRepository.findAll();
        assertThat(especialidadesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEspecialidades() throws Exception {
        // Initialize the database
        especialidadesRepository.saveAndFlush(especialidades);
        especialidadesSearchRepository.save(especialidades);

        // Search the especialidades
        restEspecialidadesMockMvc.perform(get("/api/_search/especialidades?query=id:" + especialidades.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(especialidades.getId().intValue())))
            .andExpect(jsonPath("$.[*].especialidade").value(hasItem(DEFAULT_ESPECIALIDADE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Especialidades.class);
        Especialidades especialidades1 = new Especialidades();
        especialidades1.setId(1L);
        Especialidades especialidades2 = new Especialidades();
        especialidades2.setId(especialidades1.getId());
        assertThat(especialidades1).isEqualTo(especialidades2);
        especialidades2.setId(2L);
        assertThat(especialidades1).isNotEqualTo(especialidades2);
        especialidades1.setId(null);
        assertThat(especialidades1).isNotEqualTo(especialidades2);
    }
}
