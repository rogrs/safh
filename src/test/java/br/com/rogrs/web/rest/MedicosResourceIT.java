package br.com.rogrs.web.rest;

import br.com.rogrs.SafhApp;
import br.com.rogrs.domain.Medicos;
import br.com.rogrs.repository.MedicosRepository;
import br.com.rogrs.repository.search.MedicosSearchRepository;
import br.com.rogrs.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static br.com.rogrs.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.rogrs.domain.enumeration.Estados;
/**
 * Integration tests for the {@Link MedicosResource} REST controller.
 */
@SpringBootTest(classes = SafhApp.class)
public class MedicosResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_CRM = "AAAAAAAAAA";
    private static final String UPDATED_CRM = "BBBBBBBBBB";

    private static final String DEFAULT_CPF = "AAAAAAAAAA";
    private static final String UPDATED_CPF = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_CEP = "AAAAAAAAAA";
    private static final String UPDATED_CEP = "BBBBBBBBBB";

    private static final String DEFAULT_LOGRADOURO = "AAAAAAAAAA";
    private static final String UPDATED_LOGRADOURO = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    private static final String DEFAULT_COMPLEMENTO = "AAAAAAAAAA";
    private static final String UPDATED_COMPLEMENTO = "BBBBBBBBBB";

    private static final String DEFAULT_BAIRRO = "AAAAAAAAAA";
    private static final String UPDATED_BAIRRO = "BBBBBBBBBB";

    private static final String DEFAULT_CIDADE = "AAAAAAAAAA";
    private static final String UPDATED_CIDADE = "BBBBBBBBBB";

    private static final Estados DEFAULT_U_F = Estados.AC;
    private static final Estados UPDATED_U_F = Estados.AL;

    @Autowired
    private MedicosRepository medicosRepository;

    /**
     * This repository is mocked in the br.com.rogrs.repository.search test package.
     *
     * @see br.com.rogrs.repository.search.MedicosSearchRepositoryMockConfiguration
     */
    @Autowired
    private MedicosSearchRepository mockMedicosSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restMedicosMockMvc;

    private Medicos medicos;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MedicosResource medicosResource = new MedicosResource(medicosRepository, mockMedicosSearchRepository);
        this.restMedicosMockMvc = MockMvcBuilders.standaloneSetup(medicosResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medicos createEntity(EntityManager em) {
        Medicos medicos = new Medicos()
            .nome(DEFAULT_NOME)
            .crm(DEFAULT_CRM)
            .cpf(DEFAULT_CPF)
            .email(DEFAULT_EMAIL)
            .cep(DEFAULT_CEP)
            .logradouro(DEFAULT_LOGRADOURO)
            .numero(DEFAULT_NUMERO)
            .complemento(DEFAULT_COMPLEMENTO)
            .bairro(DEFAULT_BAIRRO)
            .cidade(DEFAULT_CIDADE)
            .uF(DEFAULT_U_F);
        return medicos;
    }

    @BeforeEach
    public void initTest() {
        medicos = createEntity(em);
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
        List<Medicos> medicosList = medicosRepository.findAll();
        assertThat(medicosList).hasSize(databaseSizeBeforeCreate + 1);
        Medicos testMedicos = medicosList.get(medicosList.size() - 1);
        assertThat(testMedicos.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testMedicos.getCrm()).isEqualTo(DEFAULT_CRM);
        assertThat(testMedicos.getCpf()).isEqualTo(DEFAULT_CPF);
        assertThat(testMedicos.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testMedicos.getCep()).isEqualTo(DEFAULT_CEP);
        assertThat(testMedicos.getLogradouro()).isEqualTo(DEFAULT_LOGRADOURO);
        assertThat(testMedicos.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testMedicos.getComplemento()).isEqualTo(DEFAULT_COMPLEMENTO);
        assertThat(testMedicos.getBairro()).isEqualTo(DEFAULT_BAIRRO);
        assertThat(testMedicos.getCidade()).isEqualTo(DEFAULT_CIDADE);
        assertThat(testMedicos.getuF()).isEqualTo(DEFAULT_U_F);

        // Validate the Medicos in Elasticsearch
        verify(mockMedicosSearchRepository, times(1)).save(testMedicos);
    }

    @Test
    @Transactional
    public void createMedicosWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = medicosRepository.findAll().size();

        // Create the Medicos with an existing ID
        medicos.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedicosMockMvc.perform(post("/api/medicos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medicos)))
            .andExpect(status().isBadRequest());

        // Validate the Medicos in the database
        List<Medicos> medicosList = medicosRepository.findAll();
        assertThat(medicosList).hasSize(databaseSizeBeforeCreate);

        // Validate the Medicos in Elasticsearch
        verify(mockMedicosSearchRepository, times(0)).save(medicos);
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

        List<Medicos> medicosList = medicosRepository.findAll();
        assertThat(medicosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCrmIsRequired() throws Exception {
        int databaseSizeBeforeTest = medicosRepository.findAll().size();
        // set the field null
        medicos.setCrm(null);

        // Create the Medicos, which fails.

        restMedicosMockMvc.perform(post("/api/medicos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medicos)))
            .andExpect(status().isBadRequest());

        List<Medicos> medicosList = medicosRepository.findAll();
        assertThat(medicosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMedicos() throws Exception {
        // Initialize the database
        medicosRepository.saveAndFlush(medicos);

        // Get all the medicosList
        restMedicosMockMvc.perform(get("/api/medicos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicos.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].crm").value(hasItem(DEFAULT_CRM.toString())))
            .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP.toString())))
            .andExpect(jsonPath("$.[*].logradouro").value(hasItem(DEFAULT_LOGRADOURO.toString())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO.toString())))
            .andExpect(jsonPath("$.[*].complemento").value(hasItem(DEFAULT_COMPLEMENTO.toString())))
            .andExpect(jsonPath("$.[*].bairro").value(hasItem(DEFAULT_BAIRRO.toString())))
            .andExpect(jsonPath("$.[*].cidade").value(hasItem(DEFAULT_CIDADE.toString())))
            .andExpect(jsonPath("$.[*].uF").value(hasItem(DEFAULT_U_F.toString())));
    }
    
    @Test
    @Transactional
    public void getMedicos() throws Exception {
        // Initialize the database
        medicosRepository.saveAndFlush(medicos);

        // Get the medicos
        restMedicosMockMvc.perform(get("/api/medicos/{id}", medicos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(medicos.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.crm").value(DEFAULT_CRM.toString()))
            .andExpect(jsonPath("$.cpf").value(DEFAULT_CPF.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.cep").value(DEFAULT_CEP.toString()))
            .andExpect(jsonPath("$.logradouro").value(DEFAULT_LOGRADOURO.toString()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO.toString()))
            .andExpect(jsonPath("$.complemento").value(DEFAULT_COMPLEMENTO.toString()))
            .andExpect(jsonPath("$.bairro").value(DEFAULT_BAIRRO.toString()))
            .andExpect(jsonPath("$.cidade").value(DEFAULT_CIDADE.toString()))
            .andExpect(jsonPath("$.uF").value(DEFAULT_U_F.toString()));
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

        int databaseSizeBeforeUpdate = medicosRepository.findAll().size();

        // Update the medicos
        Medicos updatedMedicos = medicosRepository.findById(medicos.getId()).get();
        // Disconnect from session so that the updates on updatedMedicos are not directly saved in db
        em.detach(updatedMedicos);
        updatedMedicos
            .nome(UPDATED_NOME)
            .crm(UPDATED_CRM)
            .cpf(UPDATED_CPF)
            .email(UPDATED_EMAIL)
            .cep(UPDATED_CEP)
            .logradouro(UPDATED_LOGRADOURO)
            .numero(UPDATED_NUMERO)
            .complemento(UPDATED_COMPLEMENTO)
            .bairro(UPDATED_BAIRRO)
            .cidade(UPDATED_CIDADE)
            .uF(UPDATED_U_F);

        restMedicosMockMvc.perform(put("/api/medicos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMedicos)))
            .andExpect(status().isOk());

        // Validate the Medicos in the database
        List<Medicos> medicosList = medicosRepository.findAll();
        assertThat(medicosList).hasSize(databaseSizeBeforeUpdate);
        Medicos testMedicos = medicosList.get(medicosList.size() - 1);
        assertThat(testMedicos.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testMedicos.getCrm()).isEqualTo(UPDATED_CRM);
        assertThat(testMedicos.getCpf()).isEqualTo(UPDATED_CPF);
        assertThat(testMedicos.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testMedicos.getCep()).isEqualTo(UPDATED_CEP);
        assertThat(testMedicos.getLogradouro()).isEqualTo(UPDATED_LOGRADOURO);
        assertThat(testMedicos.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testMedicos.getComplemento()).isEqualTo(UPDATED_COMPLEMENTO);
        assertThat(testMedicos.getBairro()).isEqualTo(UPDATED_BAIRRO);
        assertThat(testMedicos.getCidade()).isEqualTo(UPDATED_CIDADE);
        assertThat(testMedicos.getuF()).isEqualTo(UPDATED_U_F);

        // Validate the Medicos in Elasticsearch
        verify(mockMedicosSearchRepository, times(1)).save(testMedicos);
    }

    @Test
    @Transactional
    public void updateNonExistingMedicos() throws Exception {
        int databaseSizeBeforeUpdate = medicosRepository.findAll().size();

        // Create the Medicos

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicosMockMvc.perform(put("/api/medicos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medicos)))
            .andExpect(status().isBadRequest());

        // Validate the Medicos in the database
        List<Medicos> medicosList = medicosRepository.findAll();
        assertThat(medicosList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Medicos in Elasticsearch
        verify(mockMedicosSearchRepository, times(0)).save(medicos);
    }

    @Test
    @Transactional
    public void deleteMedicos() throws Exception {
        // Initialize the database
        medicosRepository.saveAndFlush(medicos);

        int databaseSizeBeforeDelete = medicosRepository.findAll().size();

        // Delete the medicos
        restMedicosMockMvc.perform(delete("/api/medicos/{id}", medicos.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Medicos> medicosList = medicosRepository.findAll();
        assertThat(medicosList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Medicos in Elasticsearch
        verify(mockMedicosSearchRepository, times(1)).deleteById(medicos.getId());
    }

    @Test
    @Transactional
    public void searchMedicos() throws Exception {
        // Initialize the database
        medicosRepository.saveAndFlush(medicos);
        when(mockMedicosSearchRepository.search(queryStringQuery("id:" + medicos.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(medicos), PageRequest.of(0, 1), 1));
        // Search the medicos
        restMedicosMockMvc.perform(get("/api/_search/medicos?query=id:" + medicos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicos.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].crm").value(hasItem(DEFAULT_CRM)))
            .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP)))
            .andExpect(jsonPath("$.[*].logradouro").value(hasItem(DEFAULT_LOGRADOURO)))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].complemento").value(hasItem(DEFAULT_COMPLEMENTO)))
            .andExpect(jsonPath("$.[*].bairro").value(hasItem(DEFAULT_BAIRRO)))
            .andExpect(jsonPath("$.[*].cidade").value(hasItem(DEFAULT_CIDADE)))
            .andExpect(jsonPath("$.[*].uF").value(hasItem(DEFAULT_U_F.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Medicos.class);
        Medicos medicos1 = new Medicos();
        medicos1.setId(1L);
        Medicos medicos2 = new Medicos();
        medicos2.setId(medicos1.getId());
        assertThat(medicos1).isEqualTo(medicos2);
        medicos2.setId(2L);
        assertThat(medicos1).isNotEqualTo(medicos2);
        medicos1.setId(null);
        assertThat(medicos1).isNotEqualTo(medicos2);
    }
}
