package br.com.rogrs.safh.web.rest;

import br.com.rogrs.safh.SafhApp;
import br.com.rogrs.safh.domain.Especialidades;
import br.com.rogrs.safh.repository.EspecialidadesRepository;
import br.com.rogrs.safh.repository.search.EspecialidadesSearchRepository;

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
 * Test class for the EspecialidadesResource REST controller.
 *
 * @see EspecialidadesResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SafhApp.class)
@WebAppConfiguration
@IntegrationTest
public class EspecialidadesResourceIntTest {

    private static final String DEFAULT_ESPECIALIDADE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ESPECIALIDADE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private EspecialidadesRepository especialidadesRepository;

    @Inject
    private EspecialidadesSearchRepository especialidadesSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restEspecialidadesMockMvc;

    private Especialidades especialidades;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EspecialidadesResource especialidadesResource = new EspecialidadesResource();
        ReflectionTestUtils.setField(especialidadesResource, "especialidadesSearchRepository", especialidadesSearchRepository);
        ReflectionTestUtils.setField(especialidadesResource, "especialidadesRepository", especialidadesRepository);
        this.restEspecialidadesMockMvc = MockMvcBuilders.standaloneSetup(especialidadesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        especialidadesSearchRepository.deleteAll();
        especialidades = new Especialidades();
        especialidades.setEspecialidade(DEFAULT_ESPECIALIDADE);
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
        List<Especialidades> especialidades = especialidadesRepository.findAll();
        assertThat(especialidades).hasSize(databaseSizeBeforeCreate + 1);
        Especialidades testEspecialidades = especialidades.get(especialidades.size() - 1);
        assertThat(testEspecialidades.getEspecialidade()).isEqualTo(DEFAULT_ESPECIALIDADE);

        // Validate the Especialidades in ElasticSearch
        Especialidades especialidadesEs = especialidadesSearchRepository.findOne(testEspecialidades.getId());
        assertThat(especialidadesEs).isEqualToComparingFieldByField(testEspecialidades);
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

        List<Especialidades> especialidades = especialidadesRepository.findAll();
        assertThat(especialidades).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEspecialidades() throws Exception {
        // Initialize the database
        especialidadesRepository.saveAndFlush(especialidades);

        // Get all the especialidades
        restEspecialidadesMockMvc.perform(get("/api/especialidades?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
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
        Especialidades updatedEspecialidades = new Especialidades();
        updatedEspecialidades.setId(especialidades.getId());
        updatedEspecialidades.setEspecialidade(UPDATED_ESPECIALIDADE);

        restEspecialidadesMockMvc.perform(put("/api/especialidades")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEspecialidades)))
                .andExpect(status().isOk());

        // Validate the Especialidades in the database
        List<Especialidades> especialidades = especialidadesRepository.findAll();
        assertThat(especialidades).hasSize(databaseSizeBeforeUpdate);
        Especialidades testEspecialidades = especialidades.get(especialidades.size() - 1);
        assertThat(testEspecialidades.getEspecialidade()).isEqualTo(UPDATED_ESPECIALIDADE);

        // Validate the Especialidades in ElasticSearch
        Especialidades especialidadesEs = especialidadesSearchRepository.findOne(testEspecialidades.getId());
        assertThat(especialidadesEs).isEqualToComparingFieldByField(testEspecialidades);
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

        // Validate ElasticSearch is empty
        boolean especialidadesExistsInEs = especialidadesSearchRepository.exists(especialidades.getId());
        assertThat(especialidadesExistsInEs).isFalse();

        // Validate the database is empty
        List<Especialidades> especialidades = especialidadesRepository.findAll();
        assertThat(especialidades).hasSize(databaseSizeBeforeDelete - 1);
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(especialidades.getId().intValue())))
            .andExpect(jsonPath("$.[*].especialidade").value(hasItem(DEFAULT_ESPECIALIDADE.toString())));
    }
}
