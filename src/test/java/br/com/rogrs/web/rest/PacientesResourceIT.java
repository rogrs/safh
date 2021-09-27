package br.com.rogrs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.rogrs.IntegrationTest;
import br.com.rogrs.domain.Pacientes;
import br.com.rogrs.domain.enumeration.Estados;
import br.com.rogrs.repository.PacientesRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PacientesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PacientesResourceIT {

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

    private static final String ENTITY_API_URL = "/api/pacientes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PacientesRepository pacientesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPacientesMockMvc;

    private Pacientes pacientes;

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

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pacientes createUpdatedEntity(EntityManager em) {
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
        pacientes = createEntity(em);
    }

    @Test
    @Transactional
    void createPacientes() throws Exception {
        int databaseSizeBeforeCreate = pacientesRepository.findAll().size();
        // Create the Pacientes
        restPacientesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pacientes)))
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
    }

    @Test
    @Transactional
    void createPacientesWithExistingId() throws Exception {
        // Create the Pacientes with an existing ID
        pacientes.setId(1L);

        int databaseSizeBeforeCreate = pacientesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPacientesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pacientes)))
            .andExpect(status().isBadRequest());

        // Validate the Pacientes in the database
        List<Pacientes> pacientesList = pacientesRepository.findAll();
        assertThat(pacientesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkProntuarioIsRequired() throws Exception {
        int databaseSizeBeforeTest = pacientesRepository.findAll().size();
        // set the field null
        pacientes.setProntuario(null);

        // Create the Pacientes, which fails.

        restPacientesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pacientes)))
            .andExpect(status().isBadRequest());

        List<Pacientes> pacientesList = pacientesRepository.findAll();
        assertThat(pacientesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = pacientesRepository.findAll().size();
        // set the field null
        pacientes.setNome(null);

        // Create the Pacientes, which fails.

        restPacientesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pacientes)))
            .andExpect(status().isBadRequest());

        List<Pacientes> pacientesList = pacientesRepository.findAll();
        assertThat(pacientesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPacientes() throws Exception {
        // Initialize the database
        pacientesRepository.saveAndFlush(pacientes);

        // Get all the pacientesList
        restPacientesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pacientes.getId().intValue())))
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
    @Transactional
    void getPacientes() throws Exception {
        // Initialize the database
        pacientesRepository.saveAndFlush(pacientes);

        // Get the pacientes
        restPacientesMockMvc
            .perform(get(ENTITY_API_URL_ID, pacientes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pacientes.getId().intValue()))
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
    @Transactional
    void getNonExistingPacientes() throws Exception {
        // Get the pacientes
        restPacientesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPacientes() throws Exception {
        // Initialize the database
        pacientesRepository.saveAndFlush(pacientes);

        int databaseSizeBeforeUpdate = pacientesRepository.findAll().size();

        // Update the pacientes
        Pacientes updatedPacientes = pacientesRepository.findById(pacientes.getId()).get();
        // Disconnect from session so that the updates on updatedPacientes are not directly saved in db
        em.detach(updatedPacientes);
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

        restPacientesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPacientes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPacientes))
            )
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
    }

    @Test
    @Transactional
    void putNonExistingPacientes() throws Exception {
        int databaseSizeBeforeUpdate = pacientesRepository.findAll().size();
        pacientes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPacientesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pacientes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pacientes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pacientes in the database
        List<Pacientes> pacientesList = pacientesRepository.findAll();
        assertThat(pacientesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPacientes() throws Exception {
        int databaseSizeBeforeUpdate = pacientesRepository.findAll().size();
        pacientes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPacientesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pacientes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pacientes in the database
        List<Pacientes> pacientesList = pacientesRepository.findAll();
        assertThat(pacientesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPacientes() throws Exception {
        int databaseSizeBeforeUpdate = pacientesRepository.findAll().size();
        pacientes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPacientesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pacientes)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pacientes in the database
        List<Pacientes> pacientesList = pacientesRepository.findAll();
        assertThat(pacientesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePacientesWithPatch() throws Exception {
        // Initialize the database
        pacientesRepository.saveAndFlush(pacientes);

        int databaseSizeBeforeUpdate = pacientesRepository.findAll().size();

        // Update the pacientes using partial update
        Pacientes partialUpdatedPacientes = new Pacientes();
        partialUpdatedPacientes.setId(pacientes.getId());

        partialUpdatedPacientes
            .prontuario(UPDATED_PRONTUARIO)
            .nome(UPDATED_NOME)
            .cep(UPDATED_CEP)
            .logradouro(UPDATED_LOGRADOURO)
            .numero(UPDATED_NUMERO)
            .complemento(UPDATED_COMPLEMENTO)
            .bairro(UPDATED_BAIRRO)
            .cidade(UPDATED_CIDADE);

        restPacientesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPacientes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPacientes))
            )
            .andExpect(status().isOk());

        // Validate the Pacientes in the database
        List<Pacientes> pacientesList = pacientesRepository.findAll();
        assertThat(pacientesList).hasSize(databaseSizeBeforeUpdate);
        Pacientes testPacientes = pacientesList.get(pacientesList.size() - 1);
        assertThat(testPacientes.getProntuario()).isEqualTo(UPDATED_PRONTUARIO);
        assertThat(testPacientes.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testPacientes.getCpf()).isEqualTo(DEFAULT_CPF);
        assertThat(testPacientes.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPacientes.getCep()).isEqualTo(UPDATED_CEP);
        assertThat(testPacientes.getLogradouro()).isEqualTo(UPDATED_LOGRADOURO);
        assertThat(testPacientes.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testPacientes.getComplemento()).isEqualTo(UPDATED_COMPLEMENTO);
        assertThat(testPacientes.getBairro()).isEqualTo(UPDATED_BAIRRO);
        assertThat(testPacientes.getCidade()).isEqualTo(UPDATED_CIDADE);
        assertThat(testPacientes.getuF()).isEqualTo(DEFAULT_U_F);
    }

    @Test
    @Transactional
    void fullUpdatePacientesWithPatch() throws Exception {
        // Initialize the database
        pacientesRepository.saveAndFlush(pacientes);

        int databaseSizeBeforeUpdate = pacientesRepository.findAll().size();

        // Update the pacientes using partial update
        Pacientes partialUpdatedPacientes = new Pacientes();
        partialUpdatedPacientes.setId(pacientes.getId());

        partialUpdatedPacientes
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

        restPacientesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPacientes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPacientes))
            )
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
    }

    @Test
    @Transactional
    void patchNonExistingPacientes() throws Exception {
        int databaseSizeBeforeUpdate = pacientesRepository.findAll().size();
        pacientes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPacientesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pacientes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pacientes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pacientes in the database
        List<Pacientes> pacientesList = pacientesRepository.findAll();
        assertThat(pacientesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPacientes() throws Exception {
        int databaseSizeBeforeUpdate = pacientesRepository.findAll().size();
        pacientes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPacientesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pacientes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pacientes in the database
        List<Pacientes> pacientesList = pacientesRepository.findAll();
        assertThat(pacientesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPacientes() throws Exception {
        int databaseSizeBeforeUpdate = pacientesRepository.findAll().size();
        pacientes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPacientesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pacientes))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pacientes in the database
        List<Pacientes> pacientesList = pacientesRepository.findAll();
        assertThat(pacientesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePacientes() throws Exception {
        // Initialize the database
        pacientesRepository.saveAndFlush(pacientes);

        int databaseSizeBeforeDelete = pacientesRepository.findAll().size();

        // Delete the pacientes
        restPacientesMockMvc
            .perform(delete(ENTITY_API_URL_ID, pacientes.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pacientes> pacientesList = pacientesRepository.findAll();
        assertThat(pacientesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
