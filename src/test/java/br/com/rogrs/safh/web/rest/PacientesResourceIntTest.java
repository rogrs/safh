package br.com.rogrs.safh.web.rest;

import br.com.rogrs.safh.SafhApp;

import br.com.rogrs.safh.domain.Pacientes;
import br.com.rogrs.safh.repository.PacientesRepository;
import br.com.rogrs.safh.repository.search.PacientesSearchRepository;
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

import br.com.rogrs.safh.domain.enumeration.Estados;
/**
 * Test class for the PacientesResource REST controller.
 *
 * @see PacientesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SafhApp.class)
public class PacientesResourceIntTest {

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

    @Autowired
    private PacientesSearchRepository pacientesSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPacientesMockMvc;

    private Pacientes pacientes;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PacientesResource pacientesResource = new PacientesResource(pacientesRepository, pacientesSearchRepository);
        this.restPacientesMockMvc = MockMvcBuilders.standaloneSetup(pacientesResource)
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
    public static Pacientes createEntity(EntityManager em) {
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

    @Before
    public void initTest() {
        pacientesSearchRepository.deleteAll();
        pacientes = createEntity(em);
    }

    @Test
    @Transactional
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
        Pacientes pacientesEs = pacientesSearchRepository.findOne(testPacientes.getId());
        assertThat(pacientesEs).isEqualToComparingFieldByField(testPacientes);
    }

    @Test
    @Transactional
    public void createPacientesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pacientesRepository.findAll().size();

        // Create the Pacientes with an existing ID
        pacientes.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPacientesMockMvc.perform(post("/api/pacientes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pacientes)))
            .andExpect(status().isBadRequest());

        // Validate the Pacientes in the database
        List<Pacientes> pacientesList = pacientesRepository.findAll();
        assertThat(pacientesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
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
    @Transactional
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
    @Transactional
    public void getAllPacientes() throws Exception {
        // Initialize the database
        pacientesRepository.saveAndFlush(pacientes);

        // Get all the pacientesList
        restPacientesMockMvc.perform(get("/api/pacientes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pacientes.getId().intValue())))
            .andExpect(jsonPath("$.[*].prontuario").value(hasItem(DEFAULT_PRONTUARIO.intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
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
    public void getPacientes() throws Exception {
        // Initialize the database
        pacientesRepository.saveAndFlush(pacientes);

        // Get the pacientes
        restPacientesMockMvc.perform(get("/api/pacientes/{id}", pacientes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pacientes.getId().intValue()))
            .andExpect(jsonPath("$.prontuario").value(DEFAULT_PRONTUARIO.intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
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
    public void getNonExistingPacientes() throws Exception {
        // Get the pacientes
        restPacientesMockMvc.perform(get("/api/pacientes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePacientes() throws Exception {
        // Initialize the database
        pacientesRepository.saveAndFlush(pacientes);
        pacientesSearchRepository.save(pacientes);
        int databaseSizeBeforeUpdate = pacientesRepository.findAll().size();

        // Update the pacientes
        Pacientes updatedPacientes = pacientesRepository.findOne(pacientes.getId());
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
        Pacientes pacientesEs = pacientesSearchRepository.findOne(testPacientes.getId());
        assertThat(pacientesEs).isEqualToComparingFieldByField(testPacientes);
    }

    @Test
    @Transactional
    public void updateNonExistingPacientes() throws Exception {
        int databaseSizeBeforeUpdate = pacientesRepository.findAll().size();

        // Create the Pacientes

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPacientesMockMvc.perform(put("/api/pacientes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pacientes)))
            .andExpect(status().isCreated());

        // Validate the Pacientes in the database
        List<Pacientes> pacientesList = pacientesRepository.findAll();
        assertThat(pacientesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePacientes() throws Exception {
        // Initialize the database
        pacientesRepository.saveAndFlush(pacientes);
        pacientesSearchRepository.save(pacientes);
        int databaseSizeBeforeDelete = pacientesRepository.findAll().size();

        // Get the pacientes
        restPacientesMockMvc.perform(delete("/api/pacientes/{id}", pacientes.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean pacientesExistsInEs = pacientesSearchRepository.exists(pacientes.getId());
        assertThat(pacientesExistsInEs).isFalse();

        // Validate the database is empty
        List<Pacientes> pacientesList = pacientesRepository.findAll();
        assertThat(pacientesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPacientes() throws Exception {
        // Initialize the database
        pacientesRepository.saveAndFlush(pacientes);
        pacientesSearchRepository.save(pacientes);

        // Search the pacientes
        restPacientesMockMvc.perform(get("/api/_search/pacientes?query=id:" + pacientes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pacientes.getId().intValue())))
            .andExpect(jsonPath("$.[*].prontuario").value(hasItem(DEFAULT_PRONTUARIO.intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
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
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pacientes.class);
        Pacientes pacientes1 = new Pacientes();
        pacientes1.setId(1L);
        Pacientes pacientes2 = new Pacientes();
        pacientes2.setId(pacientes1.getId());
        assertThat(pacientes1).isEqualTo(pacientes2);
        pacientes2.setId(2L);
        assertThat(pacientes1).isNotEqualTo(pacientes2);
        pacientes1.setId(null);
        assertThat(pacientes1).isNotEqualTo(pacientes2);
    }
}
