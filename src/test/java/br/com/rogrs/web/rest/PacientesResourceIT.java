package br.com.rogrs.web.rest;

import br.com.rogrs.SafhApp;
import br.com.rogrs.domain.Pacientes;
import br.com.rogrs.repository.PacientesRepository;
import br.com.rogrs.repository.search.PacientesSearchRepository;
import br.com.rogrs.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;


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
 * Integration tests for the {@link PacientesResource} REST controller.
 */
@SpringBootTest(classes = SafhApp.class)
public class PacientesResourceIT {

    private static final Long DEFAULT_PRONTUARIO = 1L;
    private static final Long UPDATED_PRONTUARIO = 2L;

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

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
    private PacientesRepository pacientesRepository;

    /**
     * This repository is mocked in the br.com.rogrs.repository.search test package.
     *
     * @see br.com.rogrs.repository.search.PacientesSearchRepositoryMockConfiguration
     */
    @Autowired
    private PacientesSearchRepository mockPacientesSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restPacientesMockMvc;

    private Pacientes pacientes;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PacientesResource pacientesResource = new PacientesResource(pacientesRepository, mockPacientesSearchRepository);
        this.restPacientesMockMvc = MockMvcBuilders.standaloneSetup(pacientesResource)
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
    public static Pacientes createEntity() {
        Pacientes pacientes = new Pacientes()
            .prontuario(DEFAULT_PRONTUARIO)
            .nome(DEFAULT_NOME)
            .cpf(DEFAULT_CPF)
            .email(DEFAULT_EMAIL)
            .cep(DEFAULT_CEP)
            .logradouro(DEFAULT_LOGRADOURO)
            .numero(DEFAULT_NUMERO)
            .complemento(DEFAULT_COMPLEMENTO)
            .bairro(DEFAULT_BAIRRO)
            .cidade(DEFAULT_CIDADE)
            .uF(DEFAULT_U_F);
        return pacientes;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pacientes createUpdatedEntity() {
        Pacientes pacientes = new Pacientes()
            .prontuario(UPDATED_PRONTUARIO)
            .nome(UPDATED_NOME)
            .cpf(UPDATED_CPF)
            .email(UPDATED_EMAIL)
            .cep(UPDATED_CEP)
            .logradouro(UPDATED_LOGRADOURO)
            .numero(UPDATED_NUMERO)
            .complemento(UPDATED_COMPLEMENTO)
            .bairro(UPDATED_BAIRRO)
            .cidade(UPDATED_CIDADE)
            .uF(UPDATED_U_F);
        return pacientes;
    }

    @BeforeEach
    public void initTest() {
        pacientesRepository.deleteAll();
        pacientes = createEntity();
    }

    @Test
    public void createPacientes() throws Exception {
        int databaseSizeBeforeCreate = pacientesRepository.findAll().size();

        // Create the Pacientes
        restPacientesMockMvc.perform(post("/api/pacientes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pacientes)))
            .andExpect(status().isCreated());

        // Validate the Pacientes in the database
        List<Pacientes> pacientesList = pacientesRepository.findAll();
        assertThat(pacientesList).hasSize(databaseSizeBeforeCreate + 1);
        Pacientes testPacientes = pacientesList.get(pacientesList.size() - 1);
        assertThat(testPacientes.getProntuario()).isEqualTo(DEFAULT_PRONTUARIO);
        assertThat(testPacientes.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testPacientes.getCpf()).isEqualTo(DEFAULT_CPF);
        assertThat(testPacientes.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPacientes.getCep()).isEqualTo(DEFAULT_CEP);
        assertThat(testPacientes.getLogradouro()).isEqualTo(DEFAULT_LOGRADOURO);
        assertThat(testPacientes.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testPacientes.getComplemento()).isEqualTo(DEFAULT_COMPLEMENTO);
        assertThat(testPacientes.getBairro()).isEqualTo(DEFAULT_BAIRRO);
        assertThat(testPacientes.getCidade()).isEqualTo(DEFAULT_CIDADE);
        assertThat(testPacientes.getuF()).isEqualTo(DEFAULT_U_F);

        // Validate the Pacientes in Elasticsearch
        verify(mockPacientesSearchRepository, times(1)).save(testPacientes);
    }

    @Test
    public void createPacientesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pacientesRepository.findAll().size();

        // Create the Pacientes with an existing ID
        pacientes.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restPacientesMockMvc.perform(post("/api/pacientes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pacientes)))
            .andExpect(status().isBadRequest());

        // Validate the Pacientes in the database
        List<Pacientes> pacientesList = pacientesRepository.findAll();
        assertThat(pacientesList).hasSize(databaseSizeBeforeCreate);

        // Validate the Pacientes in Elasticsearch
        verify(mockPacientesSearchRepository, times(0)).save(pacientes);
    }


    @Test
    public void checkProntuarioIsRequired() throws Exception {
        int databaseSizeBeforeTest = pacientesRepository.findAll().size();
        // set the field null
        pacientes.setProntuario(null);

        // Create the Pacientes, which fails.

        restPacientesMockMvc.perform(post("/api/pacientes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pacientes)))
            .andExpect(status().isBadRequest());

        List<Pacientes> pacientesList = pacientesRepository.findAll();
        assertThat(pacientesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = pacientesRepository.findAll().size();
        // set the field null
        pacientes.setNome(null);

        // Create the Pacientes, which fails.

        restPacientesMockMvc.perform(post("/api/pacientes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pacientes)))
            .andExpect(status().isBadRequest());

        List<Pacientes> pacientesList = pacientesRepository.findAll();
        assertThat(pacientesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllPacientes() throws Exception {
        // Initialize the database
        pacientesRepository.save(pacientes);

        // Get all the pacientesList
        restPacientesMockMvc.perform(get("/api/pacientes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pacientes.getId())))
            .andExpect(jsonPath("$.[*].prontuario").value(hasItem(DEFAULT_PRONTUARIO.intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
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
    public void getPacientes() throws Exception {
        // Initialize the database
        pacientesRepository.save(pacientes);

        // Get the pacientes
        restPacientesMockMvc.perform(get("/api/pacientes/{id}", pacientes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pacientes.getId()))
            .andExpect(jsonPath("$.prontuario").value(DEFAULT_PRONTUARIO.intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.cpf").value(DEFAULT_CPF))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.cep").value(DEFAULT_CEP))
            .andExpect(jsonPath("$.logradouro").value(DEFAULT_LOGRADOURO))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.complemento").value(DEFAULT_COMPLEMENTO))
            .andExpect(jsonPath("$.bairro").value(DEFAULT_BAIRRO))
            .andExpect(jsonPath("$.cidade").value(DEFAULT_CIDADE))
            .andExpect(jsonPath("$.uF").value(DEFAULT_U_F.toString()));
    }

    @Test
    public void getNonExistingPacientes() throws Exception {
        // Get the pacientes
        restPacientesMockMvc.perform(get("/api/pacientes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updatePacientes() throws Exception {
        // Initialize the database
        pacientesRepository.save(pacientes);

        int databaseSizeBeforeUpdate = pacientesRepository.findAll().size();

        // Update the pacientes
        Pacientes updatedPacientes = pacientesRepository.findById(pacientes.getId()).get();
        updatedPacientes
            .prontuario(UPDATED_PRONTUARIO)
            .nome(UPDATED_NOME)
            .cpf(UPDATED_CPF)
            .email(UPDATED_EMAIL)
            .cep(UPDATED_CEP)
            .logradouro(UPDATED_LOGRADOURO)
            .numero(UPDATED_NUMERO)
            .complemento(UPDATED_COMPLEMENTO)
            .bairro(UPDATED_BAIRRO)
            .cidade(UPDATED_CIDADE)
            .uF(UPDATED_U_F);

        restPacientesMockMvc.perform(put("/api/pacientes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPacientes)))
            .andExpect(status().isOk());

        // Validate the Pacientes in the database
        List<Pacientes> pacientesList = pacientesRepository.findAll();
        assertThat(pacientesList).hasSize(databaseSizeBeforeUpdate);
        Pacientes testPacientes = pacientesList.get(pacientesList.size() - 1);
        assertThat(testPacientes.getProntuario()).isEqualTo(UPDATED_PRONTUARIO);
        assertThat(testPacientes.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testPacientes.getCpf()).isEqualTo(UPDATED_CPF);
        assertThat(testPacientes.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPacientes.getCep()).isEqualTo(UPDATED_CEP);
        assertThat(testPacientes.getLogradouro()).isEqualTo(UPDATED_LOGRADOURO);
        assertThat(testPacientes.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testPacientes.getComplemento()).isEqualTo(UPDATED_COMPLEMENTO);
        assertThat(testPacientes.getBairro()).isEqualTo(UPDATED_BAIRRO);
        assertThat(testPacientes.getCidade()).isEqualTo(UPDATED_CIDADE);
        assertThat(testPacientes.getuF()).isEqualTo(UPDATED_U_F);

        // Validate the Pacientes in Elasticsearch
        verify(mockPacientesSearchRepository, times(1)).save(testPacientes);
    }

    @Test
    public void updateNonExistingPacientes() throws Exception {
        int databaseSizeBeforeUpdate = pacientesRepository.findAll().size();

        // Create the Pacientes

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPacientesMockMvc.perform(put("/api/pacientes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pacientes)))
            .andExpect(status().isBadRequest());

        // Validate the Pacientes in the database
        List<Pacientes> pacientesList = pacientesRepository.findAll();
        assertThat(pacientesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Pacientes in Elasticsearch
        verify(mockPacientesSearchRepository, times(0)).save(pacientes);
    }

    @Test
    public void deletePacientes() throws Exception {
        // Initialize the database
        pacientesRepository.save(pacientes);

        int databaseSizeBeforeDelete = pacientesRepository.findAll().size();

        // Delete the pacientes
        restPacientesMockMvc.perform(delete("/api/pacientes/{id}", pacientes.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pacientes> pacientesList = pacientesRepository.findAll();
        assertThat(pacientesList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Pacientes in Elasticsearch
        verify(mockPacientesSearchRepository, times(1)).deleteById(pacientes.getId());
    }

    @Test
    public void searchPacientes() throws Exception {
        // Initialize the database
        pacientesRepository.save(pacientes);
        when(mockPacientesSearchRepository.search(queryStringQuery("id:" + pacientes.getId())))
            .thenReturn(Collections.singletonList(pacientes));
        // Search the pacientes
        restPacientesMockMvc.perform(get("/api/_search/pacientes?query=id:" + pacientes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pacientes.getId())))
            .andExpect(jsonPath("$.[*].prontuario").value(hasItem(DEFAULT_PRONTUARIO.intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
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
}
