package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.SafhApp;
import com.mycompany.myapp.domain.PrescricaoPaciente;
import com.mycompany.myapp.repository.PrescricaoPacienteRepository;
import com.mycompany.myapp.repository.search.PrescricaoPacienteSearchRepository;

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
 * Test class for the PrescricaoPacienteResource REST controller.
 *
 * @see PrescricaoPacienteResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SafhApp.class)
@WebAppConfiguration
@IntegrationTest
public class PrescricaoPacienteResourceIntTest {


    private static final LocalDate DEFAULT_DATA_ENTRADA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_ENTRADA = LocalDate.now(ZoneId.systemDefault());

    private static final Float DEFAULT_QUANT = 1F;
    private static final Float UPDATED_QUANT = 2F;
    private static final String DEFAULT_OBSE = "AAAAA";
    private static final String UPDATED_OBSE = "BBBBB";

    @Inject
    private PrescricaoPacienteRepository prescricaoPacienteRepository;

    @Inject
    private PrescricaoPacienteSearchRepository prescricaoPacienteSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPrescricaoPacienteMockMvc;

    private PrescricaoPaciente prescricaoPaciente;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PrescricaoPacienteResource prescricaoPacienteResource = new PrescricaoPacienteResource();
        ReflectionTestUtils.setField(prescricaoPacienteResource, "prescricaoPacienteSearchRepository", prescricaoPacienteSearchRepository);
        ReflectionTestUtils.setField(prescricaoPacienteResource, "prescricaoPacienteRepository", prescricaoPacienteRepository);
        this.restPrescricaoPacienteMockMvc = MockMvcBuilders.standaloneSetup(prescricaoPacienteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        prescricaoPacienteSearchRepository.deleteAll();
        prescricaoPaciente = new PrescricaoPaciente();
        prescricaoPaciente.setDataEntrada(DEFAULT_DATA_ENTRADA);
        prescricaoPaciente.setQuant(DEFAULT_QUANT);
        prescricaoPaciente.setObse(DEFAULT_OBSE);
    }

    @Test
    @Transactional
    public void createPrescricaoPaciente() throws Exception {
        int databaseSizeBeforeCreate = prescricaoPacienteRepository.findAll().size();

        // Create the PrescricaoPaciente

        restPrescricaoPacienteMockMvc.perform(post("/api/prescricao-pacientes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(prescricaoPaciente)))
                .andExpect(status().isCreated());

        // Validate the PrescricaoPaciente in the database
        List<PrescricaoPaciente> prescricaoPacientes = prescricaoPacienteRepository.findAll();
        assertThat(prescricaoPacientes).hasSize(databaseSizeBeforeCreate + 1);
        PrescricaoPaciente testPrescricaoPaciente = prescricaoPacientes.get(prescricaoPacientes.size() - 1);
        assertThat(testPrescricaoPaciente.getDataEntrada()).isEqualTo(DEFAULT_DATA_ENTRADA);
        assertThat(testPrescricaoPaciente.getQuant()).isEqualTo(DEFAULT_QUANT);
        assertThat(testPrescricaoPaciente.getObse()).isEqualTo(DEFAULT_OBSE);

        // Validate the PrescricaoPaciente in ElasticSearch
        PrescricaoPaciente prescricaoPacienteEs = prescricaoPacienteSearchRepository.findOne(testPrescricaoPaciente.getId());
        assertThat(prescricaoPacienteEs).isEqualToComparingFieldByField(testPrescricaoPaciente);
    }

    @Test
    @Transactional
    public void checkDataEntradaIsRequired() throws Exception {
        int databaseSizeBeforeTest = prescricaoPacienteRepository.findAll().size();
        // set the field null
        prescricaoPaciente.setDataEntrada(null);

        // Create the PrescricaoPaciente, which fails.

        restPrescricaoPacienteMockMvc.perform(post("/api/prescricao-pacientes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(prescricaoPaciente)))
                .andExpect(status().isBadRequest());

        List<PrescricaoPaciente> prescricaoPacientes = prescricaoPacienteRepository.findAll();
        assertThat(prescricaoPacientes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantIsRequired() throws Exception {
        int databaseSizeBeforeTest = prescricaoPacienteRepository.findAll().size();
        // set the field null
        prescricaoPaciente.setQuant(null);

        // Create the PrescricaoPaciente, which fails.

        restPrescricaoPacienteMockMvc.perform(post("/api/prescricao-pacientes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(prescricaoPaciente)))
                .andExpect(status().isBadRequest());

        List<PrescricaoPaciente> prescricaoPacientes = prescricaoPacienteRepository.findAll();
        assertThat(prescricaoPacientes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPrescricaoPacientes() throws Exception {
        // Initialize the database
        prescricaoPacienteRepository.saveAndFlush(prescricaoPaciente);

        // Get all the prescricaoPacientes
        restPrescricaoPacienteMockMvc.perform(get("/api/prescricao-pacientes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(prescricaoPaciente.getId().intValue())))
                .andExpect(jsonPath("$.[*].dataEntrada").value(hasItem(DEFAULT_DATA_ENTRADA.toString())))
                .andExpect(jsonPath("$.[*].quant").value(hasItem(DEFAULT_QUANT.doubleValue())))
                .andExpect(jsonPath("$.[*].obse").value(hasItem(DEFAULT_OBSE.toString())));
    }

    @Test
    @Transactional
    public void getPrescricaoPaciente() throws Exception {
        // Initialize the database
        prescricaoPacienteRepository.saveAndFlush(prescricaoPaciente);

        // Get the prescricaoPaciente
        restPrescricaoPacienteMockMvc.perform(get("/api/prescricao-pacientes/{id}", prescricaoPaciente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(prescricaoPaciente.getId().intValue()))
            .andExpect(jsonPath("$.dataEntrada").value(DEFAULT_DATA_ENTRADA.toString()))
            .andExpect(jsonPath("$.quant").value(DEFAULT_QUANT.doubleValue()))
            .andExpect(jsonPath("$.obse").value(DEFAULT_OBSE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPrescricaoPaciente() throws Exception {
        // Get the prescricaoPaciente
        restPrescricaoPacienteMockMvc.perform(get("/api/prescricao-pacientes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePrescricaoPaciente() throws Exception {
        // Initialize the database
        prescricaoPacienteRepository.saveAndFlush(prescricaoPaciente);
        prescricaoPacienteSearchRepository.save(prescricaoPaciente);
        int databaseSizeBeforeUpdate = prescricaoPacienteRepository.findAll().size();

        // Update the prescricaoPaciente
        PrescricaoPaciente updatedPrescricaoPaciente = new PrescricaoPaciente();
        updatedPrescricaoPaciente.setId(prescricaoPaciente.getId());
        updatedPrescricaoPaciente.setDataEntrada(UPDATED_DATA_ENTRADA);
        updatedPrescricaoPaciente.setQuant(UPDATED_QUANT);
        updatedPrescricaoPaciente.setObse(UPDATED_OBSE);

        restPrescricaoPacienteMockMvc.perform(put("/api/prescricao-pacientes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPrescricaoPaciente)))
                .andExpect(status().isOk());

        // Validate the PrescricaoPaciente in the database
        List<PrescricaoPaciente> prescricaoPacientes = prescricaoPacienteRepository.findAll();
        assertThat(prescricaoPacientes).hasSize(databaseSizeBeforeUpdate);
        PrescricaoPaciente testPrescricaoPaciente = prescricaoPacientes.get(prescricaoPacientes.size() - 1);
        assertThat(testPrescricaoPaciente.getDataEntrada()).isEqualTo(UPDATED_DATA_ENTRADA);
        assertThat(testPrescricaoPaciente.getQuant()).isEqualTo(UPDATED_QUANT);
        assertThat(testPrescricaoPaciente.getObse()).isEqualTo(UPDATED_OBSE);

        // Validate the PrescricaoPaciente in ElasticSearch
        PrescricaoPaciente prescricaoPacienteEs = prescricaoPacienteSearchRepository.findOne(testPrescricaoPaciente.getId());
        assertThat(prescricaoPacienteEs).isEqualToComparingFieldByField(testPrescricaoPaciente);
    }

    @Test
    @Transactional
    public void deletePrescricaoPaciente() throws Exception {
        // Initialize the database
        prescricaoPacienteRepository.saveAndFlush(prescricaoPaciente);
        prescricaoPacienteSearchRepository.save(prescricaoPaciente);
        int databaseSizeBeforeDelete = prescricaoPacienteRepository.findAll().size();

        // Get the prescricaoPaciente
        restPrescricaoPacienteMockMvc.perform(delete("/api/prescricao-pacientes/{id}", prescricaoPaciente.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean prescricaoPacienteExistsInEs = prescricaoPacienteSearchRepository.exists(prescricaoPaciente.getId());
        assertThat(prescricaoPacienteExistsInEs).isFalse();

        // Validate the database is empty
        List<PrescricaoPaciente> prescricaoPacientes = prescricaoPacienteRepository.findAll();
        assertThat(prescricaoPacientes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPrescricaoPaciente() throws Exception {
        // Initialize the database
        prescricaoPacienteRepository.saveAndFlush(prescricaoPaciente);
        prescricaoPacienteSearchRepository.save(prescricaoPaciente);

        // Search the prescricaoPaciente
        restPrescricaoPacienteMockMvc.perform(get("/api/_search/prescricao-pacientes?query=id:" + prescricaoPaciente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prescricaoPaciente.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataEntrada").value(hasItem(DEFAULT_DATA_ENTRADA.toString())))
            .andExpect(jsonPath("$.[*].quant").value(hasItem(DEFAULT_QUANT.doubleValue())))
            .andExpect(jsonPath("$.[*].obse").value(hasItem(DEFAULT_OBSE.toString())));
    }
}
