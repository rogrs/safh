package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.SafhApp;
import com.mycompany.myapp.domain.Medicos;
import com.mycompany.myapp.repository.MedicosRepository;
import com.mycompany.myapp.repository.search.MedicosSearchRepository;

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
 * Test class for the MedicosResource REST controller.
 *
 * @see MedicosResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SafhApp.class)
@WebAppConfiguration
@IntegrationTest
public class MedicosResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAA";
    private static final String UPDATED_NOME = "BBBBB";
    private static final String DEFAULT_CRM = "AAAAA";
    private static final String UPDATED_CRM = "BBBBB";
    private static final String DEFAULT_CPF = "AAAAA";
    private static final String UPDATED_CPF = "BBBBB";
    private static final String DEFAULT_EMAIL = "AAAAA";
    private static final String UPDATED_EMAIL = "BBBBB";

    @Inject
    private MedicosRepository medicosRepository;

    @Inject
    private MedicosSearchRepository medicosSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMedicosMockMvc;

    private Medicos medicos;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MedicosResource medicosResource = new MedicosResource();
        ReflectionTestUtils.setField(medicosResource, "medicosSearchRepository", medicosSearchRepository);
        ReflectionTestUtils.setField(medicosResource, "medicosRepository", medicosRepository);
        this.restMedicosMockMvc = MockMvcBuilders.standaloneSetup(medicosResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        medicosSearchRepository.deleteAll();
        medicos = new Medicos();
        medicos.setNome(DEFAULT_NOME);
        medicos.setCrm(DEFAULT_CRM);
        medicos.setCpf(DEFAULT_CPF);
        medicos.setEmail(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    public void createMedicos() throws Exception {
        int databaseSizeBeforeCreate = medicosRepository.findAll().size();

        // Create the Medicos

        restMedicosMockMvc.perform(post("/api/medicos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(medicos)))
                .andExpect(status().isCreated());

        // Validate the Medicos in the database
        List<Medicos> medicos = medicosRepository.findAll();
        assertThat(medicos).hasSize(databaseSizeBeforeCreate + 1);
        Medicos testMedicos = medicos.get(medicos.size() - 1);
        assertThat(testMedicos.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testMedicos.getCrm()).isEqualTo(DEFAULT_CRM);
        assertThat(testMedicos.getCpf()).isEqualTo(DEFAULT_CPF);
        assertThat(testMedicos.getEmail()).isEqualTo(DEFAULT_EMAIL);

        // Validate the Medicos in ElasticSearch
        Medicos medicosEs = medicosSearchRepository.findOne(testMedicos.getId());
        assertThat(medicosEs).isEqualToComparingFieldByField(testMedicos);
    }

    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = medicosRepository.findAll().size();
        // set the field null
        medicos.setNome(null);

        // Create the Medicos, which fails.

        restMedicosMockMvc.perform(post("/api/medicos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(medicos)))
                .andExpect(status().isBadRequest());

        List<Medicos> medicos = medicosRepository.findAll();
        assertThat(medicos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMedicos() throws Exception {
        // Initialize the database
        medicosRepository.saveAndFlush(medicos);

        // Get all the medicos
        restMedicosMockMvc.perform(get("/api/medicos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(medicos.getId().intValue())))
                .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
                .andExpect(jsonPath("$.[*].crm").value(hasItem(DEFAULT_CRM.toString())))
                .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())));
    }

    @Test
    @Transactional
    public void getMedicos() throws Exception {
        // Initialize the database
        medicosRepository.saveAndFlush(medicos);

        // Get the medicos
        restMedicosMockMvc.perform(get("/api/medicos/{id}", medicos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(medicos.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.crm").value(DEFAULT_CRM.toString()))
            .andExpect(jsonPath("$.cpf").value(DEFAULT_CPF.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMedicos() throws Exception {
        // Get the medicos
        restMedicosMockMvc.perform(get("/api/medicos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMedicos() throws Exception {
        // Initialize the database
        medicosRepository.saveAndFlush(medicos);
        medicosSearchRepository.save(medicos);
        int databaseSizeBeforeUpdate = medicosRepository.findAll().size();

        // Update the medicos
        Medicos updatedMedicos = new Medicos();
        updatedMedicos.setId(medicos.getId());
        updatedMedicos.setNome(UPDATED_NOME);
        updatedMedicos.setCrm(UPDATED_CRM);
        updatedMedicos.setCpf(UPDATED_CPF);
        updatedMedicos.setEmail(UPDATED_EMAIL);

        restMedicosMockMvc.perform(put("/api/medicos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedMedicos)))
                .andExpect(status().isOk());

        // Validate the Medicos in the database
        List<Medicos> medicos = medicosRepository.findAll();
        assertThat(medicos).hasSize(databaseSizeBeforeUpdate);
        Medicos testMedicos = medicos.get(medicos.size() - 1);
        assertThat(testMedicos.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testMedicos.getCrm()).isEqualTo(UPDATED_CRM);
        assertThat(testMedicos.getCpf()).isEqualTo(UPDATED_CPF);
        assertThat(testMedicos.getEmail()).isEqualTo(UPDATED_EMAIL);

        // Validate the Medicos in ElasticSearch
        Medicos medicosEs = medicosSearchRepository.findOne(testMedicos.getId());
        assertThat(medicosEs).isEqualToComparingFieldByField(testMedicos);
    }

    @Test
    @Transactional
    public void deleteMedicos() throws Exception {
        // Initialize the database
        medicosRepository.saveAndFlush(medicos);
        medicosSearchRepository.save(medicos);
        int databaseSizeBeforeDelete = medicosRepository.findAll().size();

        // Get the medicos
        restMedicosMockMvc.perform(delete("/api/medicos/{id}", medicos.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean medicosExistsInEs = medicosSearchRepository.exists(medicos.getId());
        assertThat(medicosExistsInEs).isFalse();

        // Validate the database is empty
        List<Medicos> medicos = medicosRepository.findAll();
        assertThat(medicos).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMedicos() throws Exception {
        // Initialize the database
        medicosRepository.saveAndFlush(medicos);
        medicosSearchRepository.save(medicos);

        // Search the medicos
        restMedicosMockMvc.perform(get("/api/_search/medicos?query=id:" + medicos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicos.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].crm").value(hasItem(DEFAULT_CRM.toString())))
            .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())));
    }
}
