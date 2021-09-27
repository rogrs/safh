package br.com.rogrs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.rogrs.IntegrationTest;
import br.com.rogrs.domain.Medicos;
import br.com.rogrs.domain.enumeration.Estados;
import br.com.rogrs.repository.MedicosRepository;
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
 * Integration tests for the {@link MedicosResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MedicosResourceIT {

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

    private static final String ENTITY_API_URL = "/api/medicos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MedicosRepository medicosRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMedicosMockMvc;

    private Medicos medicos;

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

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medicos createUpdatedEntity(EntityManager em) {
        Medicos medicos = new Medicos()
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
        return medicos;
    }

    @BeforeEach
    public void initTest() {
        medicos = createEntity(em);
    }

    @Test
    @Transactional
    void createMedicos() throws Exception {
        int databaseSizeBeforeCreate = medicosRepository.findAll().size();
        // Create the Medicos
        restMedicosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medicos)))
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
    }

    @Test
    @Transactional
    void createMedicosWithExistingId() throws Exception {
        // Create the Medicos with an existing ID
        medicos.setId(1L);

        int databaseSizeBeforeCreate = medicosRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedicosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medicos)))
            .andExpect(status().isBadRequest());

        // Validate the Medicos in the database
        List<Medicos> medicosList = medicosRepository.findAll();
        assertThat(medicosList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = medicosRepository.findAll().size();
        // set the field null
        medicos.setNome(null);

        // Create the Medicos, which fails.

        restMedicosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medicos)))
            .andExpect(status().isBadRequest());

        List<Medicos> medicosList = medicosRepository.findAll();
        assertThat(medicosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCrmIsRequired() throws Exception {
        int databaseSizeBeforeTest = medicosRepository.findAll().size();
        // set the field null
        medicos.setCrm(null);

        // Create the Medicos, which fails.

        restMedicosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medicos)))
            .andExpect(status().isBadRequest());

        List<Medicos> medicosList = medicosRepository.findAll();
        assertThat(medicosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMedicos() throws Exception {
        // Initialize the database
        medicosRepository.saveAndFlush(medicos);

        // Get all the medicosList
        restMedicosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
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
    void getMedicos() throws Exception {
        // Initialize the database
        medicosRepository.saveAndFlush(medicos);

        // Get the medicos
        restMedicosMockMvc
            .perform(get(ENTITY_API_URL_ID, medicos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(medicos.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.crm").value(DEFAULT_CRM))
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
    void getNonExistingMedicos() throws Exception {
        // Get the medicos
        restMedicosMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMedicos() throws Exception {
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

        restMedicosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMedicos.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMedicos))
            )
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
    }

    @Test
    @Transactional
    void putNonExistingMedicos() throws Exception {
        int databaseSizeBeforeUpdate = medicosRepository.findAll().size();
        medicos.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicos.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(medicos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medicos in the database
        List<Medicos> medicosList = medicosRepository.findAll();
        assertThat(medicosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMedicos() throws Exception {
        int databaseSizeBeforeUpdate = medicosRepository.findAll().size();
        medicos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(medicos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medicos in the database
        List<Medicos> medicosList = medicosRepository.findAll();
        assertThat(medicosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMedicos() throws Exception {
        int databaseSizeBeforeUpdate = medicosRepository.findAll().size();
        medicos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicosMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medicos)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Medicos in the database
        List<Medicos> medicosList = medicosRepository.findAll();
        assertThat(medicosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMedicosWithPatch() throws Exception {
        // Initialize the database
        medicosRepository.saveAndFlush(medicos);

        int databaseSizeBeforeUpdate = medicosRepository.findAll().size();

        // Update the medicos using partial update
        Medicos partialUpdatedMedicos = new Medicos();
        partialUpdatedMedicos.setId(medicos.getId());

        partialUpdatedMedicos.nome(UPDATED_NOME).cep(UPDATED_CEP).logradouro(UPDATED_LOGRADOURO).bairro(UPDATED_BAIRRO).uF(UPDATED_U_F);

        restMedicosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedicos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMedicos))
            )
            .andExpect(status().isOk());

        // Validate the Medicos in the database
        List<Medicos> medicosList = medicosRepository.findAll();
        assertThat(medicosList).hasSize(databaseSizeBeforeUpdate);
        Medicos testMedicos = medicosList.get(medicosList.size() - 1);
        assertThat(testMedicos.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testMedicos.getCrm()).isEqualTo(DEFAULT_CRM);
        assertThat(testMedicos.getCpf()).isEqualTo(DEFAULT_CPF);
        assertThat(testMedicos.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testMedicos.getCep()).isEqualTo(UPDATED_CEP);
        assertThat(testMedicos.getLogradouro()).isEqualTo(UPDATED_LOGRADOURO);
        assertThat(testMedicos.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testMedicos.getComplemento()).isEqualTo(DEFAULT_COMPLEMENTO);
        assertThat(testMedicos.getBairro()).isEqualTo(UPDATED_BAIRRO);
        assertThat(testMedicos.getCidade()).isEqualTo(DEFAULT_CIDADE);
        assertThat(testMedicos.getuF()).isEqualTo(UPDATED_U_F);
    }

    @Test
    @Transactional
    void fullUpdateMedicosWithPatch() throws Exception {
        // Initialize the database
        medicosRepository.saveAndFlush(medicos);

        int databaseSizeBeforeUpdate = medicosRepository.findAll().size();

        // Update the medicos using partial update
        Medicos partialUpdatedMedicos = new Medicos();
        partialUpdatedMedicos.setId(medicos.getId());

        partialUpdatedMedicos
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

        restMedicosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedicos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMedicos))
            )
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
    }

    @Test
    @Transactional
    void patchNonExistingMedicos() throws Exception {
        int databaseSizeBeforeUpdate = medicosRepository.findAll().size();
        medicos.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, medicos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(medicos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medicos in the database
        List<Medicos> medicosList = medicosRepository.findAll();
        assertThat(medicosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMedicos() throws Exception {
        int databaseSizeBeforeUpdate = medicosRepository.findAll().size();
        medicos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(medicos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medicos in the database
        List<Medicos> medicosList = medicosRepository.findAll();
        assertThat(medicosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMedicos() throws Exception {
        int databaseSizeBeforeUpdate = medicosRepository.findAll().size();
        medicos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicosMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(medicos)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Medicos in the database
        List<Medicos> medicosList = medicosRepository.findAll();
        assertThat(medicosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMedicos() throws Exception {
        // Initialize the database
        medicosRepository.saveAndFlush(medicos);

        int databaseSizeBeforeDelete = medicosRepository.findAll().size();

        // Delete the medicos
        restMedicosMockMvc
            .perform(delete(ENTITY_API_URL_ID, medicos.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Medicos> medicosList = medicosRepository.findAll();
        assertThat(medicosList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
