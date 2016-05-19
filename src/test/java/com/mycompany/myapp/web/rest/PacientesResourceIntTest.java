package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.SafhApp;
import com.mycompany.myapp.domain.Pacientes;
import com.mycompany.myapp.repository.PacientesRepository;
import com.mycompany.myapp.repository.search.PacientesSearchRepository;

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
    private static final String DEFAULT_NATURALIDADE = "AAAAA";
    private static final String UPDATED_NATURALIDADE = "BBBBB";
    private static final String DEFAULT_TELEFONE = "AAAAA";
    private static final String UPDATED_TELEFONE = "BBBBB";
    private static final String DEFAULT_CELULAR = "AAAAA";
    private static final String UPDATED_CELULAR = "BBBBB";

    private static final Float DEFAULT_PESO = 1F;
    private static final Float UPDATED_PESO = 2F;
    private static final String DEFAULT_OBSERVACAO = "AAAAA";
    private static final String UPDATED_OBSERVACAO = "BBBBB";

    private static final LocalDate DEFAULT_NASCIMENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_NASCIMENTO = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_ENDERECO = "AAAAA";
    private static final String UPDATED_ENDERECO = "BBBBB";
    private static final String DEFAULT_NUMERO = "AAAAA";
    private static final String UPDATED_NUMERO = "BBBBB";
    private static final String DEFAULT_COMPLEMENTO = "AAAAA";
    private static final String UPDATED_COMPLEMENTO = "BBBBB";
    private static final String DEFAULT_BAIRRO = "AAAAA";
    private static final String UPDATED_BAIRRO = "BBBBB";
    private static final String DEFAULT_CIDADE = "AAAAA";
    private static final String UPDATED_CIDADE = "BBBBB";
    private static final String DEFAULT_UF = "AA";
    private static final String UPDATED_UF = "BB";
    private static final String DEFAULT_CPF = "AAAAA";
    private static final String UPDATED_CPF = "BBBBB";
    private static final String DEFAULT_EMAIL = "AAAAA";
    private static final String UPDATED_EMAIL = "BBBBB";
    private static final String DEFAULT_CEP = "AAAAA";
    private static final String UPDATED_CEP = "BBBBB";

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
        pacientes.setNaturalidade(DEFAULT_NATURALIDADE);
        pacientes.setTelefone(DEFAULT_TELEFONE);
        pacientes.setCelular(DEFAULT_CELULAR);
        pacientes.setPeso(DEFAULT_PESO);
        pacientes.setObservacao(DEFAULT_OBSERVACAO);
        pacientes.setNascimento(DEFAULT_NASCIMENTO);
        pacientes.setEndereco(DEFAULT_ENDERECO);
        pacientes.setNumero(DEFAULT_NUMERO);
        pacientes.setComplemento(DEFAULT_COMPLEMENTO);
        pacientes.setBairro(DEFAULT_BAIRRO);
        pacientes.setCidade(DEFAULT_CIDADE);
        pacientes.setUf(DEFAULT_UF);
        pacientes.setCpf(DEFAULT_CPF);
        pacientes.setEmail(DEFAULT_EMAIL);
        pacientes.setCep(DEFAULT_CEP);
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
        assertThat(testPacientes.getNaturalidade()).isEqualTo(DEFAULT_NATURALIDADE);
        assertThat(testPacientes.getTelefone()).isEqualTo(DEFAULT_TELEFONE);
        assertThat(testPacientes.getCelular()).isEqualTo(DEFAULT_CELULAR);
        assertThat(testPacientes.getPeso()).isEqualTo(DEFAULT_PESO);
        assertThat(testPacientes.getObservacao()).isEqualTo(DEFAULT_OBSERVACAO);
        assertThat(testPacientes.getNascimento()).isEqualTo(DEFAULT_NASCIMENTO);
        assertThat(testPacientes.getEndereco()).isEqualTo(DEFAULT_ENDERECO);
        assertThat(testPacientes.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testPacientes.getComplemento()).isEqualTo(DEFAULT_COMPLEMENTO);
        assertThat(testPacientes.getBairro()).isEqualTo(DEFAULT_BAIRRO);
        assertThat(testPacientes.getCidade()).isEqualTo(DEFAULT_CIDADE);
        assertThat(testPacientes.getUf()).isEqualTo(DEFAULT_UF);
        assertThat(testPacientes.getCpf()).isEqualTo(DEFAULT_CPF);
        assertThat(testPacientes.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPacientes.getCep()).isEqualTo(DEFAULT_CEP);

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
                .andExpect(jsonPath("$.[*].naturalidade").value(hasItem(DEFAULT_NATURALIDADE.toString())))
                .andExpect(jsonPath("$.[*].telefone").value(hasItem(DEFAULT_TELEFONE.toString())))
                .andExpect(jsonPath("$.[*].celular").value(hasItem(DEFAULT_CELULAR.toString())))
                .andExpect(jsonPath("$.[*].peso").value(hasItem(DEFAULT_PESO.doubleValue())))
                .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO.toString())))
                .andExpect(jsonPath("$.[*].nascimento").value(hasItem(DEFAULT_NASCIMENTO.toString())))
                .andExpect(jsonPath("$.[*].endereco").value(hasItem(DEFAULT_ENDERECO.toString())))
                .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO.toString())))
                .andExpect(jsonPath("$.[*].complemento").value(hasItem(DEFAULT_COMPLEMENTO.toString())))
                .andExpect(jsonPath("$.[*].bairro").value(hasItem(DEFAULT_BAIRRO.toString())))
                .andExpect(jsonPath("$.[*].cidade").value(hasItem(DEFAULT_CIDADE.toString())))
                .andExpect(jsonPath("$.[*].uf").value(hasItem(DEFAULT_UF.toString())))
                .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP.toString())));
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
            .andExpect(jsonPath("$.naturalidade").value(DEFAULT_NATURALIDADE.toString()))
            .andExpect(jsonPath("$.telefone").value(DEFAULT_TELEFONE.toString()))
            .andExpect(jsonPath("$.celular").value(DEFAULT_CELULAR.toString()))
            .andExpect(jsonPath("$.peso").value(DEFAULT_PESO.doubleValue()))
            .andExpect(jsonPath("$.observacao").value(DEFAULT_OBSERVACAO.toString()))
            .andExpect(jsonPath("$.nascimento").value(DEFAULT_NASCIMENTO.toString()))
            .andExpect(jsonPath("$.endereco").value(DEFAULT_ENDERECO.toString()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO.toString()))
            .andExpect(jsonPath("$.complemento").value(DEFAULT_COMPLEMENTO.toString()))
            .andExpect(jsonPath("$.bairro").value(DEFAULT_BAIRRO.toString()))
            .andExpect(jsonPath("$.cidade").value(DEFAULT_CIDADE.toString()))
            .andExpect(jsonPath("$.uf").value(DEFAULT_UF.toString()))
            .andExpect(jsonPath("$.cpf").value(DEFAULT_CPF.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.cep").value(DEFAULT_CEP.toString()));
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
        updatedPacientes.setNaturalidade(UPDATED_NATURALIDADE);
        updatedPacientes.setTelefone(UPDATED_TELEFONE);
        updatedPacientes.setCelular(UPDATED_CELULAR);
        updatedPacientes.setPeso(UPDATED_PESO);
        updatedPacientes.setObservacao(UPDATED_OBSERVACAO);
        updatedPacientes.setNascimento(UPDATED_NASCIMENTO);
        updatedPacientes.setEndereco(UPDATED_ENDERECO);
        updatedPacientes.setNumero(UPDATED_NUMERO);
        updatedPacientes.setComplemento(UPDATED_COMPLEMENTO);
        updatedPacientes.setBairro(UPDATED_BAIRRO);
        updatedPacientes.setCidade(UPDATED_CIDADE);
        updatedPacientes.setUf(UPDATED_UF);
        updatedPacientes.setCpf(UPDATED_CPF);
        updatedPacientes.setEmail(UPDATED_EMAIL);
        updatedPacientes.setCep(UPDATED_CEP);

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
        assertThat(testPacientes.getNaturalidade()).isEqualTo(UPDATED_NATURALIDADE);
        assertThat(testPacientes.getTelefone()).isEqualTo(UPDATED_TELEFONE);
        assertThat(testPacientes.getCelular()).isEqualTo(UPDATED_CELULAR);
        assertThat(testPacientes.getPeso()).isEqualTo(UPDATED_PESO);
        assertThat(testPacientes.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
        assertThat(testPacientes.getNascimento()).isEqualTo(UPDATED_NASCIMENTO);
        assertThat(testPacientes.getEndereco()).isEqualTo(UPDATED_ENDERECO);
        assertThat(testPacientes.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testPacientes.getComplemento()).isEqualTo(UPDATED_COMPLEMENTO);
        assertThat(testPacientes.getBairro()).isEqualTo(UPDATED_BAIRRO);
        assertThat(testPacientes.getCidade()).isEqualTo(UPDATED_CIDADE);
        assertThat(testPacientes.getUf()).isEqualTo(UPDATED_UF);
        assertThat(testPacientes.getCpf()).isEqualTo(UPDATED_CPF);
        assertThat(testPacientes.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPacientes.getCep()).isEqualTo(UPDATED_CEP);

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
            .andExpect(jsonPath("$.[*].naturalidade").value(hasItem(DEFAULT_NATURALIDADE.toString())))
            .andExpect(jsonPath("$.[*].telefone").value(hasItem(DEFAULT_TELEFONE.toString())))
            .andExpect(jsonPath("$.[*].celular").value(hasItem(DEFAULT_CELULAR.toString())))
            .andExpect(jsonPath("$.[*].peso").value(hasItem(DEFAULT_PESO.doubleValue())))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO.toString())))
            .andExpect(jsonPath("$.[*].nascimento").value(hasItem(DEFAULT_NASCIMENTO.toString())))
            .andExpect(jsonPath("$.[*].endereco").value(hasItem(DEFAULT_ENDERECO.toString())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO.toString())))
            .andExpect(jsonPath("$.[*].complemento").value(hasItem(DEFAULT_COMPLEMENTO.toString())))
            .andExpect(jsonPath("$.[*].bairro").value(hasItem(DEFAULT_BAIRRO.toString())))
            .andExpect(jsonPath("$.[*].cidade").value(hasItem(DEFAULT_CIDADE.toString())))
            .andExpect(jsonPath("$.[*].uf").value(hasItem(DEFAULT_UF.toString())))
            .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP.toString())));
    }
}
