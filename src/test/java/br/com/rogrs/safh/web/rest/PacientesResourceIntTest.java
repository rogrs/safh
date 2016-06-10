package br.com.rogrs.safh.web.rest;

import br.com.rogrs.safh.SafhApp;
import br.com.rogrs.safh.domain.Pacientes;
import br.com.rogrs.safh.repository.PacientesRepository;
import br.com.rogrs.safh.repository.search.PacientesSearchRepository;

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

import br.com.rogrs.safh.domain.enumeration.Estados;

/**
 * Test class for the PacientesResource REST controller.
 *
 * @see PacientesResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SafhApp.class)
@WebAppConfiguration
@IntegrationTest
public class PacientesResourceIntTest {


    private static final Long DEFAULT_PRONTUARIO = 1L;
    private static final Long UPDATED_PRONTUARIO = 2L;
    private static final String DEFAULT_NOME = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_CPF = "AAAAAAAAAAA";
    private static final String UPDATED_CPF = "BBBBBBBBBBB";
    private static final String DEFAULT_EMAIL = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_CEP = "AAAAAAAAAA";
    private static final String UPDATED_CEP = "BBBBBBBBBB";
    private static final String DEFAULT_LOGRADOURO = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LOGRADOURO = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";
    private static final String DEFAULT_COMPLEMENTO = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_COMPLEMENTO = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_BAIRRO = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_BAIRRO = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_CIDADE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_CIDADE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final Estados DEFAULT_U_F = Estados.AC;
    private static final Estados UPDATED_U_F = Estados.AL;

    @Inject
    private PacientesRepository pacientesRepository;

    @Inject
    private PacientesSearchRepository pacientesSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPacientesMockMvc;

    private Pacientes pacientes;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PacientesResource pacientesResource = new PacientesResource();
        ReflectionTestUtils.setField(pacientesResource, "pacientesSearchRepository", pacientesSearchRepository);
        ReflectionTestUtils.setField(pacientesResource, "pacientesRepository", pacientesRepository);
        this.restPacientesMockMvc = MockMvcBuilders.standaloneSetup(pacientesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        pacientesSearchRepository.deleteAll();
        pacientes = new Pacientes();
        pacientes.setProntuario(DEFAULT_PRONTUARIO);
        pacientes.setNome(DEFAULT_NOME);
        pacientes.setCpf(DEFAULT_CPF);
        pacientes.setEmail(DEFAULT_EMAIL);
        pacientes.setCep(DEFAULT_CEP);
        pacientes.setLogradouro(DEFAULT_LOGRADOURO);
        pacientes.setNumero(DEFAULT_NUMERO);
        pacientes.setComplemento(DEFAULT_COMPLEMENTO);
        pacientes.setBairro(DEFAULT_BAIRRO);
        pacientes.setCidade(DEFAULT_CIDADE);
        pacientes.setuF(DEFAULT_U_F);
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
        List<Pacientes> pacientes = pacientesRepository.findAll();
        assertThat(pacientes).hasSize(databaseSizeBeforeCreate + 1);
        Pacientes testPacientes = pacientes.get(pacientes.size() - 1);
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

        // Validate the Pacientes in ElasticSearch
        Pacientes pacientesEs = pacientesSearchRepository.findOne(testPacientes.getId());
        assertThat(pacientesEs).isEqualToComparingFieldByField(testPacientes);
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

        List<Pacientes> pacientes = pacientesRepository.findAll();
        assertThat(pacientes).hasSize(databaseSizeBeforeTest);
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

        List<Pacientes> pacientes = pacientesRepository.findAll();
        assertThat(pacientes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPacientes() throws Exception {
        // Initialize the database
        pacientesRepository.saveAndFlush(pacientes);

        // Get all the pacientes
        restPacientesMockMvc.perform(get("/api/pacientes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
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
        Pacientes updatedPacientes = new Pacientes();
        updatedPacientes.setId(pacientes.getId());
        updatedPacientes.setProntuario(UPDATED_PRONTUARIO);
        updatedPacientes.setNome(UPDATED_NOME);
        updatedPacientes.setCpf(UPDATED_CPF);
        updatedPacientes.setEmail(UPDATED_EMAIL);
        updatedPacientes.setCep(UPDATED_CEP);
        updatedPacientes.setLogradouro(UPDATED_LOGRADOURO);
        updatedPacientes.setNumero(UPDATED_NUMERO);
        updatedPacientes.setComplemento(UPDATED_COMPLEMENTO);
        updatedPacientes.setBairro(UPDATED_BAIRRO);
        updatedPacientes.setCidade(UPDATED_CIDADE);
        updatedPacientes.setuF(UPDATED_U_F);

        restPacientesMockMvc.perform(put("/api/pacientes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPacientes)))
                .andExpect(status().isOk());

        // Validate the Pacientes in the database
        List<Pacientes> pacientes = pacientesRepository.findAll();
        assertThat(pacientes).hasSize(databaseSizeBeforeUpdate);
        Pacientes testPacientes = pacientes.get(pacientes.size() - 1);
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

        // Validate the Pacientes in ElasticSearch
        Pacientes pacientesEs = pacientesSearchRepository.findOne(testPacientes.getId());
        assertThat(pacientesEs).isEqualToComparingFieldByField(testPacientes);
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

        // Validate ElasticSearch is empty
        boolean pacientesExistsInEs = pacientesSearchRepository.exists(pacientes.getId());
        assertThat(pacientesExistsInEs).isFalse();

        // Validate the database is empty
        List<Pacientes> pacientes = pacientesRepository.findAll();
        assertThat(pacientes).hasSize(databaseSizeBeforeDelete - 1);
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
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
}
