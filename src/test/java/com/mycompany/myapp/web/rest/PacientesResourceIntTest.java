package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.myapp.SafhApp;
import com.mycompany.myapp.domain.Pacientes;
import com.mycompany.myapp.repository.PacientesRepository;
import com.mycompany.myapp.repository.search.PacientesSearchRepository;


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

    private static final String DEFAULT_NOME = "AAAAA";
    private static final String UPDATED_NOME = "BBBBB";
    private static final String DEFAULT_SOBRENOME = "AAAAA";
    private static final String UPDATED_SOBRENOME = "BBBBB";
    private static final String DEFAULT_CPF = "461.823.493-87";
    private static final String UPDATED_CPF = "199.755.384-87";

    private static final LocalDate DEFAULT_NASCIMENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_NASCIMENTO = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_NATURALIDADE = "AAAAA";
    private static final String UPDATED_NATURALIDADE = "BBBBB";
    private static final String DEFAULT_EMAIL = "teste_velho@teste.com.br";
    private static final String UPDATED_EMAIL = "teste_novo@teste.com.br";
    private static final String DEFAULT_TELEFONE = "(21) 2222-3333";
    private static final String UPDATED_TELEFONE = "(21) 91111-6029";
    private static final String DEFAULT_CELULAR = "(21) 93341-6029";
    private static final String UPDATED_CELULAR = "(21) 91221-6029";

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
        pacientes.setNome(DEFAULT_NOME);
        pacientes.setSobrenome(DEFAULT_SOBRENOME);
        pacientes.setCpf(DEFAULT_CPF);
        pacientes.setNascimento(DEFAULT_NASCIMENTO);
        pacientes.setNaturalidade(DEFAULT_NATURALIDADE);
        pacientes.setEmail(DEFAULT_EMAIL);
        pacientes.setTelefone(DEFAULT_TELEFONE);
        pacientes.setCelular(DEFAULT_CELULAR);
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
        assertThat(testPacientes.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testPacientes.getSobrenome()).isEqualTo(DEFAULT_SOBRENOME);
        assertThat(testPacientes.getCpf()).isEqualTo(DEFAULT_CPF);
        assertThat(testPacientes.getNascimento()).isEqualTo(DEFAULT_NASCIMENTO);
        assertThat(testPacientes.getNaturalidade()).isEqualTo(DEFAULT_NATURALIDADE);
        assertThat(testPacientes.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPacientes.getTelefone()).isEqualTo(DEFAULT_TELEFONE);
        assertThat(testPacientes.getCelular()).isEqualTo(DEFAULT_CELULAR);

        // Validate the Pacientes in ElasticSearch
        Pacientes pacientesEs = pacientesSearchRepository.findOne(testPacientes.getId());
        assertThat(pacientesEs).isEqualToComparingFieldByField(testPacientes);
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
    public void checkSobrenomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = pacientesRepository.findAll().size();
        // set the field null
        pacientes.setSobrenome(null);

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
    public void checkCpfIsRequired() throws Exception {
        int databaseSizeBeforeTest = pacientesRepository.findAll().size();
        // set the field null
        pacientes.setCpf(null);

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
    public void checkTelefoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = pacientesRepository.findAll().size();
        // set the field null
        pacientes.setTelefone(null);

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
                .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
                .andExpect(jsonPath("$.[*].sobrenome").value(hasItem(DEFAULT_SOBRENOME.toString())))
                .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF.toString())))
                .andExpect(jsonPath("$.[*].nascimento").value(hasItem(DEFAULT_NASCIMENTO.toString())))
                .andExpect(jsonPath("$.[*].naturalidade").value(hasItem(DEFAULT_NATURALIDADE.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].telefone").value(hasItem(DEFAULT_TELEFONE.toString())))
                .andExpect(jsonPath("$.[*].celular").value(hasItem(DEFAULT_CELULAR.toString())));
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
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.sobrenome").value(DEFAULT_SOBRENOME.toString()))
            .andExpect(jsonPath("$.cpf").value(DEFAULT_CPF.toString()))
            .andExpect(jsonPath("$.nascimento").value(DEFAULT_NASCIMENTO.toString()))
            .andExpect(jsonPath("$.naturalidade").value(DEFAULT_NATURALIDADE.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.telefone").value(DEFAULT_TELEFONE.toString()))
            .andExpect(jsonPath("$.celular").value(DEFAULT_CELULAR.toString()));
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
        updatedPacientes.setNome(UPDATED_NOME);
        updatedPacientes.setSobrenome(UPDATED_SOBRENOME);
        updatedPacientes.setCpf(UPDATED_CPF);
        updatedPacientes.setNascimento(UPDATED_NASCIMENTO);
        updatedPacientes.setNaturalidade(UPDATED_NATURALIDADE);
        updatedPacientes.setEmail(UPDATED_EMAIL);
        updatedPacientes.setTelefone(UPDATED_TELEFONE);
        updatedPacientes.setCelular(UPDATED_CELULAR);

        restPacientesMockMvc.perform(put("/api/pacientes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPacientes)))
                .andExpect(status().isOk());

        // Validate the Pacientes in the database
        List<Pacientes> pacientes = pacientesRepository.findAll();
        assertThat(pacientes).hasSize(databaseSizeBeforeUpdate);
        Pacientes testPacientes = pacientes.get(pacientes.size() - 1);
        assertThat(testPacientes.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testPacientes.getSobrenome()).isEqualTo(UPDATED_SOBRENOME);
        assertThat(testPacientes.getCpf()).isEqualTo(UPDATED_CPF);
        assertThat(testPacientes.getNascimento()).isEqualTo(UPDATED_NASCIMENTO);
        assertThat(testPacientes.getNaturalidade()).isEqualTo(UPDATED_NATURALIDADE);
        assertThat(testPacientes.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPacientes.getTelefone()).isEqualTo(UPDATED_TELEFONE);
        assertThat(testPacientes.getCelular()).isEqualTo(UPDATED_CELULAR);

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
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].sobrenome").value(hasItem(DEFAULT_SOBRENOME.toString())))
            .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF.toString())))
            .andExpect(jsonPath("$.[*].nascimento").value(hasItem(DEFAULT_NASCIMENTO.toString())))
            .andExpect(jsonPath("$.[*].naturalidade").value(hasItem(DEFAULT_NATURALIDADE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].telefone").value(hasItem(DEFAULT_TELEFONE.toString())))
            .andExpect(jsonPath("$.[*].celular").value(hasItem(DEFAULT_CELULAR.toString())));
    }
}
