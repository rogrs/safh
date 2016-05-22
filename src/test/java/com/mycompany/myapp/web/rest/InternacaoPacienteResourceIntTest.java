package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.SafhApp;
import com.mycompany.myapp.domain.InternacaoPaciente;
import com.mycompany.myapp.repository.InternacaoPacienteRepository;
import com.mycompany.myapp.repository.search.InternacaoPacienteSearchRepository;

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
 * Test class for the InternacaoPacienteResource REST controller.
 *
 * @see InternacaoPacienteResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SafhApp.class)
@WebAppConfiguration
@IntegrationTest
public class InternacaoPacienteResourceIntTest {


    private static final LocalDate DEFAULT_DATA_INTERNACAO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_INTERNACAO = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private InternacaoPacienteRepository internacaoPacienteRepository;

    @Inject
    private InternacaoPacienteSearchRepository internacaoPacienteSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restInternacaoPacienteMockMvc;

    private InternacaoPaciente internacaoPaciente;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InternacaoPacienteResource internacaoPacienteResource = new InternacaoPacienteResource();
        ReflectionTestUtils.setField(internacaoPacienteResource, "internacaoPacienteSearchRepository", internacaoPacienteSearchRepository);
        ReflectionTestUtils.setField(internacaoPacienteResource, "internacaoPacienteRepository", internacaoPacienteRepository);
        this.restInternacaoPacienteMockMvc = MockMvcBuilders.standaloneSetup(internacaoPacienteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        internacaoPacienteSearchRepository.deleteAll();
        internacaoPaciente = new InternacaoPaciente();
        internacaoPaciente.setDataInternacao(DEFAULT_DATA_INTERNACAO);
    }

    @Test
    @Transactional
    public void createInternacaoPaciente() throws Exception {
        int databaseSizeBeforeCreate = internacaoPacienteRepository.findAll().size();

        // Create the InternacaoPaciente

        restInternacaoPacienteMockMvc.perform(post("/api/internacao-pacientes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(internacaoPaciente)))
                .andExpect(status().isCreated());

        // Validate the InternacaoPaciente in the database
        List<InternacaoPaciente> internacaoPacientes = internacaoPacienteRepository.findAll();
        assertThat(internacaoPacientes).hasSize(databaseSizeBeforeCreate + 1);
        InternacaoPaciente testInternacaoPaciente = internacaoPacientes.get(internacaoPacientes.size() - 1);
        assertThat(testInternacaoPaciente.getDataInternacao()).isEqualTo(DEFAULT_DATA_INTERNACAO);

        // Validate the InternacaoPaciente in ElasticSearch
        InternacaoPaciente internacaoPacienteEs = internacaoPacienteSearchRepository.findOne(testInternacaoPaciente.getId());
        assertThat(internacaoPacienteEs).isEqualToComparingFieldByField(testInternacaoPaciente);
    }

    @Test
    @Transactional
    public void checkDataInternacaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = internacaoPacienteRepository.findAll().size();
        // set the field null
        internacaoPaciente.setDataInternacao(null);

        // Create the InternacaoPaciente, which fails.

        restInternacaoPacienteMockMvc.perform(post("/api/internacao-pacientes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(internacaoPaciente)))
                .andExpect(status().isBadRequest());

        List<InternacaoPaciente> internacaoPacientes = internacaoPacienteRepository.findAll();
        assertThat(internacaoPacientes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInternacaoPacientes() throws Exception {
        // Initialize the database
        internacaoPacienteRepository.saveAndFlush(internacaoPaciente);

        // Get all the internacaoPacientes
        restInternacaoPacienteMockMvc.perform(get("/api/internacao-pacientes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(internacaoPaciente.getId().intValue())))
                .andExpect(jsonPath("$.[*].dataInternacao").value(hasItem(DEFAULT_DATA_INTERNACAO.toString())));
    }

    @Test
    @Transactional
    public void getInternacaoPaciente() throws Exception {
        // Initialize the database
        internacaoPacienteRepository.saveAndFlush(internacaoPaciente);

        // Get the internacaoPaciente
        restInternacaoPacienteMockMvc.perform(get("/api/internacao-pacientes/{id}", internacaoPaciente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(internacaoPaciente.getId().intValue()))
            .andExpect(jsonPath("$.dataInternacao").value(DEFAULT_DATA_INTERNACAO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInternacaoPaciente() throws Exception {
        // Get the internacaoPaciente
        restInternacaoPacienteMockMvc.perform(get("/api/internacao-pacientes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInternacaoPaciente() throws Exception {
        // Initialize the database
        internacaoPacienteRepository.saveAndFlush(internacaoPaciente);
        internacaoPacienteSearchRepository.save(internacaoPaciente);
        int databaseSizeBeforeUpdate = internacaoPacienteRepository.findAll().size();

        // Update the internacaoPaciente
        InternacaoPaciente updatedInternacaoPaciente = new InternacaoPaciente();
        updatedInternacaoPaciente.setId(internacaoPaciente.getId());
        updatedInternacaoPaciente.setDataInternacao(UPDATED_DATA_INTERNACAO);

        restInternacaoPacienteMockMvc.perform(put("/api/internacao-pacientes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedInternacaoPaciente)))
                .andExpect(status().isOk());

        // Validate the InternacaoPaciente in the database
        List<InternacaoPaciente> internacaoPacientes = internacaoPacienteRepository.findAll();
        assertThat(internacaoPacientes).hasSize(databaseSizeBeforeUpdate);
        InternacaoPaciente testInternacaoPaciente = internacaoPacientes.get(internacaoPacientes.size() - 1);
        assertThat(testInternacaoPaciente.getDataInternacao()).isEqualTo(UPDATED_DATA_INTERNACAO);

        // Validate the InternacaoPaciente in ElasticSearch
        InternacaoPaciente internacaoPacienteEs = internacaoPacienteSearchRepository.findOne(testInternacaoPaciente.getId());
        assertThat(internacaoPacienteEs).isEqualToComparingFieldByField(testInternacaoPaciente);
    }

    @Test
    @Transactional
    public void deleteInternacaoPaciente() throws Exception {
        // Initialize the database
        internacaoPacienteRepository.saveAndFlush(internacaoPaciente);
        internacaoPacienteSearchRepository.save(internacaoPaciente);
        int databaseSizeBeforeDelete = internacaoPacienteRepository.findAll().size();

        // Get the internacaoPaciente
        restInternacaoPacienteMockMvc.perform(delete("/api/internacao-pacientes/{id}", internacaoPaciente.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean internacaoPacienteExistsInEs = internacaoPacienteSearchRepository.exists(internacaoPaciente.getId());
        assertThat(internacaoPacienteExistsInEs).isFalse();

        // Validate the database is empty
        List<InternacaoPaciente> internacaoPacientes = internacaoPacienteRepository.findAll();
        assertThat(internacaoPacientes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchInternacaoPaciente() throws Exception {
        // Initialize the database
        internacaoPacienteRepository.saveAndFlush(internacaoPaciente);
        internacaoPacienteSearchRepository.save(internacaoPaciente);

        // Search the internacaoPaciente
        restInternacaoPacienteMockMvc.perform(get("/api/_search/internacao-pacientes?query=id:" + internacaoPaciente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(internacaoPaciente.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataInternacao").value(hasItem(DEFAULT_DATA_INTERNACAO.toString())));
    }
}
