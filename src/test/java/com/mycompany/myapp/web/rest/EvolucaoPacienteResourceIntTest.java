package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.SafhApp;
import com.mycompany.myapp.domain.EvolucaoPaciente;
import com.mycompany.myapp.repository.EvolucaoPacienteRepository;
import com.mycompany.myapp.repository.search.EvolucaoPacienteSearchRepository;

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
 * Test class for the EvolucaoPacienteResource REST controller.
 *
 * @see EvolucaoPacienteResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SafhApp.class)
@WebAppConfiguration
@IntegrationTest
public class EvolucaoPacienteResourceIntTest {


    private static final LocalDate DEFAULT_DATA_EVOLUCAO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_EVOLUCAO = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private EvolucaoPacienteRepository evolucaoPacienteRepository;

    @Inject
    private EvolucaoPacienteSearchRepository evolucaoPacienteSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restEvolucaoPacienteMockMvc;

    private EvolucaoPaciente evolucaoPaciente;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EvolucaoPacienteResource evolucaoPacienteResource = new EvolucaoPacienteResource();
        ReflectionTestUtils.setField(evolucaoPacienteResource, "evolucaoPacienteSearchRepository", evolucaoPacienteSearchRepository);
        ReflectionTestUtils.setField(evolucaoPacienteResource, "evolucaoPacienteRepository", evolucaoPacienteRepository);
        this.restEvolucaoPacienteMockMvc = MockMvcBuilders.standaloneSetup(evolucaoPacienteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        evolucaoPacienteSearchRepository.deleteAll();
        evolucaoPaciente = new EvolucaoPaciente();
        evolucaoPaciente.setDataEvolucao(DEFAULT_DATA_EVOLUCAO);
        evolucaoPaciente.setDescricao(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    public void createEvolucaoPaciente() throws Exception {
        int databaseSizeBeforeCreate = evolucaoPacienteRepository.findAll().size();

        // Create the EvolucaoPaciente

        restEvolucaoPacienteMockMvc.perform(post("/api/evolucao-pacientes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(evolucaoPaciente)))
                .andExpect(status().isCreated());

        // Validate the EvolucaoPaciente in the database
        List<EvolucaoPaciente> evolucaoPacientes = evolucaoPacienteRepository.findAll();
        assertThat(evolucaoPacientes).hasSize(databaseSizeBeforeCreate + 1);
        EvolucaoPaciente testEvolucaoPaciente = evolucaoPacientes.get(evolucaoPacientes.size() - 1);
        assertThat(testEvolucaoPaciente.getDataEvolucao()).isEqualTo(DEFAULT_DATA_EVOLUCAO);
        assertThat(testEvolucaoPaciente.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);

        // Validate the EvolucaoPaciente in ElasticSearch
        EvolucaoPaciente evolucaoPacienteEs = evolucaoPacienteSearchRepository.findOne(testEvolucaoPaciente.getId());
        assertThat(evolucaoPacienteEs).isEqualToComparingFieldByField(testEvolucaoPaciente);
    }

    @Test
    @Transactional
    public void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = evolucaoPacienteRepository.findAll().size();
        // set the field null
        evolucaoPaciente.setDescricao(null);

        // Create the EvolucaoPaciente, which fails.

        restEvolucaoPacienteMockMvc.perform(post("/api/evolucao-pacientes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(evolucaoPaciente)))
                .andExpect(status().isBadRequest());

        List<EvolucaoPaciente> evolucaoPacientes = evolucaoPacienteRepository.findAll();
        assertThat(evolucaoPacientes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEvolucaoPacientes() throws Exception {
        // Initialize the database
        evolucaoPacienteRepository.saveAndFlush(evolucaoPaciente);

        // Get all the evolucaoPacientes
        restEvolucaoPacienteMockMvc.perform(get("/api/evolucao-pacientes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(evolucaoPaciente.getId().intValue())))
                .andExpect(jsonPath("$.[*].dataEvolucao").value(hasItem(DEFAULT_DATA_EVOLUCAO.toString())))
                .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())));
    }

    @Test
    @Transactional
    public void getEvolucaoPaciente() throws Exception {
        // Initialize the database
        evolucaoPacienteRepository.saveAndFlush(evolucaoPaciente);

        // Get the evolucaoPaciente
        restEvolucaoPacienteMockMvc.perform(get("/api/evolucao-pacientes/{id}", evolucaoPaciente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(evolucaoPaciente.getId().intValue()))
            .andExpect(jsonPath("$.dataEvolucao").value(DEFAULT_DATA_EVOLUCAO.toString()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEvolucaoPaciente() throws Exception {
        // Get the evolucaoPaciente
        restEvolucaoPacienteMockMvc.perform(get("/api/evolucao-pacientes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEvolucaoPaciente() throws Exception {
        // Initialize the database
        evolucaoPacienteRepository.saveAndFlush(evolucaoPaciente);
        evolucaoPacienteSearchRepository.save(evolucaoPaciente);
        int databaseSizeBeforeUpdate = evolucaoPacienteRepository.findAll().size();

        // Update the evolucaoPaciente
        EvolucaoPaciente updatedEvolucaoPaciente = new EvolucaoPaciente();
        updatedEvolucaoPaciente.setId(evolucaoPaciente.getId());
        updatedEvolucaoPaciente.setDataEvolucao(UPDATED_DATA_EVOLUCAO);
        updatedEvolucaoPaciente.setDescricao(UPDATED_DESCRICAO);

        restEvolucaoPacienteMockMvc.perform(put("/api/evolucao-pacientes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEvolucaoPaciente)))
                .andExpect(status().isOk());

        // Validate the EvolucaoPaciente in the database
        List<EvolucaoPaciente> evolucaoPacientes = evolucaoPacienteRepository.findAll();
        assertThat(evolucaoPacientes).hasSize(databaseSizeBeforeUpdate);
        EvolucaoPaciente testEvolucaoPaciente = evolucaoPacientes.get(evolucaoPacientes.size() - 1);
        assertThat(testEvolucaoPaciente.getDataEvolucao()).isEqualTo(UPDATED_DATA_EVOLUCAO);
        assertThat(testEvolucaoPaciente.getDescricao()).isEqualTo(UPDATED_DESCRICAO);

        // Validate the EvolucaoPaciente in ElasticSearch
        EvolucaoPaciente evolucaoPacienteEs = evolucaoPacienteSearchRepository.findOne(testEvolucaoPaciente.getId());
        assertThat(evolucaoPacienteEs).isEqualToComparingFieldByField(testEvolucaoPaciente);
    }

    @Test
    @Transactional
    public void deleteEvolucaoPaciente() throws Exception {
        // Initialize the database
        evolucaoPacienteRepository.saveAndFlush(evolucaoPaciente);
        evolucaoPacienteSearchRepository.save(evolucaoPaciente);
        int databaseSizeBeforeDelete = evolucaoPacienteRepository.findAll().size();

        // Get the evolucaoPaciente
        restEvolucaoPacienteMockMvc.perform(delete("/api/evolucao-pacientes/{id}", evolucaoPaciente.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean evolucaoPacienteExistsInEs = evolucaoPacienteSearchRepository.exists(evolucaoPaciente.getId());
        assertThat(evolucaoPacienteExistsInEs).isFalse();

        // Validate the database is empty
        List<EvolucaoPaciente> evolucaoPacientes = evolucaoPacienteRepository.findAll();
        assertThat(evolucaoPacientes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEvolucaoPaciente() throws Exception {
        // Initialize the database
        evolucaoPacienteRepository.saveAndFlush(evolucaoPaciente);
        evolucaoPacienteSearchRepository.save(evolucaoPaciente);

        // Search the evolucaoPaciente
        restEvolucaoPacienteMockMvc.perform(get("/api/_search/evolucao-pacientes?query=id:" + evolucaoPaciente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(evolucaoPaciente.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataEvolucao").value(hasItem(DEFAULT_DATA_EVOLUCAO.toString())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())));
    }
}
