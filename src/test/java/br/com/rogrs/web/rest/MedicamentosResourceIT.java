package br.com.rogrs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.rogrs.IntegrationTest;
import br.com.rogrs.domain.Medicamentos;
import br.com.rogrs.repository.MedicamentosRepository;
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
 * Integration tests for the {@link MedicamentosResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MedicamentosResourceIT {

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final String DEFAULT_REGISTRO_MINISTERIO_SAUDE = "AAAAAAAAAA";
    private static final String UPDATED_REGISTRO_MINISTERIO_SAUDE = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO_BARRAS = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO_BARRAS = "BBBBBBBBBB";

    private static final Float DEFAULT_QTD_ATUAL = 1F;
    private static final Float UPDATED_QTD_ATUAL = 2F;

    private static final Float DEFAULT_QTD_MIN = 1F;
    private static final Float UPDATED_QTD_MIN = 2F;

    private static final Float DEFAULT_QTD_MAX = 1F;
    private static final Float UPDATED_QTD_MAX = 2F;

    private static final String DEFAULT_OBSERVACOES = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACOES = "BBBBBBBBBB";

    private static final String DEFAULT_APRESENTACAO = "AAAAAAAAAA";
    private static final String UPDATED_APRESENTACAO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/medicamentos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MedicamentosRepository medicamentosRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMedicamentosMockMvc;

    private Medicamentos medicamentos;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medicamentos createEntity(EntityManager em) {
        Medicamentos medicamentos = new Medicamentos()
            .descricao(DEFAULT_DESCRICAO)
            .registroMinisterioSaude(DEFAULT_REGISTRO_MINISTERIO_SAUDE)
            .codigoBarras(DEFAULT_CODIGO_BARRAS)
            .qtdAtual(DEFAULT_QTD_ATUAL)
            .qtdMin(DEFAULT_QTD_MIN)
            .qtdMax(DEFAULT_QTD_MAX)
            .observacoes(DEFAULT_OBSERVACOES)
            .apresentacao(DEFAULT_APRESENTACAO);
        return medicamentos;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medicamentos createUpdatedEntity(EntityManager em) {
        Medicamentos medicamentos = new Medicamentos()
            .descricao(UPDATED_DESCRICAO)
            .registroMinisterioSaude(UPDATED_REGISTRO_MINISTERIO_SAUDE)
            .codigoBarras(UPDATED_CODIGO_BARRAS)
            .qtdAtual(UPDATED_QTD_ATUAL)
            .qtdMin(UPDATED_QTD_MIN)
            .qtdMax(UPDATED_QTD_MAX)
            .observacoes(UPDATED_OBSERVACOES)
            .apresentacao(UPDATED_APRESENTACAO);
        return medicamentos;
    }

    @BeforeEach
    public void initTest() {
        medicamentos = createEntity(em);
    }

    @Test
    @Transactional
    void createMedicamentos() throws Exception {
        int databaseSizeBeforeCreate = medicamentosRepository.findAll().size();
        // Create the Medicamentos
        restMedicamentosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medicamentos)))
            .andExpect(status().isCreated());

        // Validate the Medicamentos in the database
        List<Medicamentos> medicamentosList = medicamentosRepository.findAll();
        assertThat(medicamentosList).hasSize(databaseSizeBeforeCreate + 1);
        Medicamentos testMedicamentos = medicamentosList.get(medicamentosList.size() - 1);
        assertThat(testMedicamentos.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testMedicamentos.getRegistroMinisterioSaude()).isEqualTo(DEFAULT_REGISTRO_MINISTERIO_SAUDE);
        assertThat(testMedicamentos.getCodigoBarras()).isEqualTo(DEFAULT_CODIGO_BARRAS);
        assertThat(testMedicamentos.getQtdAtual()).isEqualTo(DEFAULT_QTD_ATUAL);
        assertThat(testMedicamentos.getQtdMin()).isEqualTo(DEFAULT_QTD_MIN);
        assertThat(testMedicamentos.getQtdMax()).isEqualTo(DEFAULT_QTD_MAX);
        assertThat(testMedicamentos.getObservacoes()).isEqualTo(DEFAULT_OBSERVACOES);
        assertThat(testMedicamentos.getApresentacao()).isEqualTo(DEFAULT_APRESENTACAO);
    }

    @Test
    @Transactional
    void createMedicamentosWithExistingId() throws Exception {
        // Create the Medicamentos with an existing ID
        medicamentos.setId(1L);

        int databaseSizeBeforeCreate = medicamentosRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedicamentosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medicamentos)))
            .andExpect(status().isBadRequest());

        // Validate the Medicamentos in the database
        List<Medicamentos> medicamentosList = medicamentosRepository.findAll();
        assertThat(medicamentosList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = medicamentosRepository.findAll().size();
        // set the field null
        medicamentos.setDescricao(null);

        // Create the Medicamentos, which fails.

        restMedicamentosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medicamentos)))
            .andExpect(status().isBadRequest());

        List<Medicamentos> medicamentosList = medicamentosRepository.findAll();
        assertThat(medicamentosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRegistroMinisterioSaudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = medicamentosRepository.findAll().size();
        // set the field null
        medicamentos.setRegistroMinisterioSaude(null);

        // Create the Medicamentos, which fails.

        restMedicamentosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medicamentos)))
            .andExpect(status().isBadRequest());

        List<Medicamentos> medicamentosList = medicamentosRepository.findAll();
        assertThat(medicamentosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodigoBarrasIsRequired() throws Exception {
        int databaseSizeBeforeTest = medicamentosRepository.findAll().size();
        // set the field null
        medicamentos.setCodigoBarras(null);

        // Create the Medicamentos, which fails.

        restMedicamentosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medicamentos)))
            .andExpect(status().isBadRequest());

        List<Medicamentos> medicamentosList = medicamentosRepository.findAll();
        assertThat(medicamentosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMedicamentos() throws Exception {
        // Initialize the database
        medicamentosRepository.saveAndFlush(medicamentos);

        // Get all the medicamentosList
        restMedicamentosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicamentos.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].registroMinisterioSaude").value(hasItem(DEFAULT_REGISTRO_MINISTERIO_SAUDE)))
            .andExpect(jsonPath("$.[*].codigoBarras").value(hasItem(DEFAULT_CODIGO_BARRAS)))
            .andExpect(jsonPath("$.[*].qtdAtual").value(hasItem(DEFAULT_QTD_ATUAL.doubleValue())))
            .andExpect(jsonPath("$.[*].qtdMin").value(hasItem(DEFAULT_QTD_MIN.doubleValue())))
            .andExpect(jsonPath("$.[*].qtdMax").value(hasItem(DEFAULT_QTD_MAX.doubleValue())))
            .andExpect(jsonPath("$.[*].observacoes").value(hasItem(DEFAULT_OBSERVACOES)))
            .andExpect(jsonPath("$.[*].apresentacao").value(hasItem(DEFAULT_APRESENTACAO)));
    }

    @Test
    @Transactional
    void getMedicamentos() throws Exception {
        // Initialize the database
        medicamentosRepository.saveAndFlush(medicamentos);

        // Get the medicamentos
        restMedicamentosMockMvc
            .perform(get(ENTITY_API_URL_ID, medicamentos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(medicamentos.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.registroMinisterioSaude").value(DEFAULT_REGISTRO_MINISTERIO_SAUDE))
            .andExpect(jsonPath("$.codigoBarras").value(DEFAULT_CODIGO_BARRAS))
            .andExpect(jsonPath("$.qtdAtual").value(DEFAULT_QTD_ATUAL.doubleValue()))
            .andExpect(jsonPath("$.qtdMin").value(DEFAULT_QTD_MIN.doubleValue()))
            .andExpect(jsonPath("$.qtdMax").value(DEFAULT_QTD_MAX.doubleValue()))
            .andExpect(jsonPath("$.observacoes").value(DEFAULT_OBSERVACOES))
            .andExpect(jsonPath("$.apresentacao").value(DEFAULT_APRESENTACAO));
    }

    @Test
    @Transactional
    void getNonExistingMedicamentos() throws Exception {
        // Get the medicamentos
        restMedicamentosMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMedicamentos() throws Exception {
        // Initialize the database
        medicamentosRepository.saveAndFlush(medicamentos);

        int databaseSizeBeforeUpdate = medicamentosRepository.findAll().size();

        // Update the medicamentos
        Medicamentos updatedMedicamentos = medicamentosRepository.findById(medicamentos.getId()).get();
        // Disconnect from session so that the updates on updatedMedicamentos are not directly saved in db
        em.detach(updatedMedicamentos);
        updatedMedicamentos
            .descricao(UPDATED_DESCRICAO)
            .registroMinisterioSaude(UPDATED_REGISTRO_MINISTERIO_SAUDE)
            .codigoBarras(UPDATED_CODIGO_BARRAS)
            .qtdAtual(UPDATED_QTD_ATUAL)
            .qtdMin(UPDATED_QTD_MIN)
            .qtdMax(UPDATED_QTD_MAX)
            .observacoes(UPDATED_OBSERVACOES)
            .apresentacao(UPDATED_APRESENTACAO);

        restMedicamentosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMedicamentos.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMedicamentos))
            )
            .andExpect(status().isOk());

        // Validate the Medicamentos in the database
        List<Medicamentos> medicamentosList = medicamentosRepository.findAll();
        assertThat(medicamentosList).hasSize(databaseSizeBeforeUpdate);
        Medicamentos testMedicamentos = medicamentosList.get(medicamentosList.size() - 1);
        assertThat(testMedicamentos.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testMedicamentos.getRegistroMinisterioSaude()).isEqualTo(UPDATED_REGISTRO_MINISTERIO_SAUDE);
        assertThat(testMedicamentos.getCodigoBarras()).isEqualTo(UPDATED_CODIGO_BARRAS);
        assertThat(testMedicamentos.getQtdAtual()).isEqualTo(UPDATED_QTD_ATUAL);
        assertThat(testMedicamentos.getQtdMin()).isEqualTo(UPDATED_QTD_MIN);
        assertThat(testMedicamentos.getQtdMax()).isEqualTo(UPDATED_QTD_MAX);
        assertThat(testMedicamentos.getObservacoes()).isEqualTo(UPDATED_OBSERVACOES);
        assertThat(testMedicamentos.getApresentacao()).isEqualTo(UPDATED_APRESENTACAO);
    }

    @Test
    @Transactional
    void putNonExistingMedicamentos() throws Exception {
        int databaseSizeBeforeUpdate = medicamentosRepository.findAll().size();
        medicamentos.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicamentosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicamentos.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(medicamentos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medicamentos in the database
        List<Medicamentos> medicamentosList = medicamentosRepository.findAll();
        assertThat(medicamentosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMedicamentos() throws Exception {
        int databaseSizeBeforeUpdate = medicamentosRepository.findAll().size();
        medicamentos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicamentosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(medicamentos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medicamentos in the database
        List<Medicamentos> medicamentosList = medicamentosRepository.findAll();
        assertThat(medicamentosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMedicamentos() throws Exception {
        int databaseSizeBeforeUpdate = medicamentosRepository.findAll().size();
        medicamentos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicamentosMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medicamentos)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Medicamentos in the database
        List<Medicamentos> medicamentosList = medicamentosRepository.findAll();
        assertThat(medicamentosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMedicamentosWithPatch() throws Exception {
        // Initialize the database
        medicamentosRepository.saveAndFlush(medicamentos);

        int databaseSizeBeforeUpdate = medicamentosRepository.findAll().size();

        // Update the medicamentos using partial update
        Medicamentos partialUpdatedMedicamentos = new Medicamentos();
        partialUpdatedMedicamentos.setId(medicamentos.getId());

        partialUpdatedMedicamentos
            .descricao(UPDATED_DESCRICAO)
            .registroMinisterioSaude(UPDATED_REGISTRO_MINISTERIO_SAUDE)
            .qtdAtual(UPDATED_QTD_ATUAL)
            .qtdMin(UPDATED_QTD_MIN)
            .qtdMax(UPDATED_QTD_MAX)
            .observacoes(UPDATED_OBSERVACOES);

        restMedicamentosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedicamentos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMedicamentos))
            )
            .andExpect(status().isOk());

        // Validate the Medicamentos in the database
        List<Medicamentos> medicamentosList = medicamentosRepository.findAll();
        assertThat(medicamentosList).hasSize(databaseSizeBeforeUpdate);
        Medicamentos testMedicamentos = medicamentosList.get(medicamentosList.size() - 1);
        assertThat(testMedicamentos.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testMedicamentos.getRegistroMinisterioSaude()).isEqualTo(UPDATED_REGISTRO_MINISTERIO_SAUDE);
        assertThat(testMedicamentos.getCodigoBarras()).isEqualTo(DEFAULT_CODIGO_BARRAS);
        assertThat(testMedicamentos.getQtdAtual()).isEqualTo(UPDATED_QTD_ATUAL);
        assertThat(testMedicamentos.getQtdMin()).isEqualTo(UPDATED_QTD_MIN);
        assertThat(testMedicamentos.getQtdMax()).isEqualTo(UPDATED_QTD_MAX);
        assertThat(testMedicamentos.getObservacoes()).isEqualTo(UPDATED_OBSERVACOES);
        assertThat(testMedicamentos.getApresentacao()).isEqualTo(DEFAULT_APRESENTACAO);
    }

    @Test
    @Transactional
    void fullUpdateMedicamentosWithPatch() throws Exception {
        // Initialize the database
        medicamentosRepository.saveAndFlush(medicamentos);

        int databaseSizeBeforeUpdate = medicamentosRepository.findAll().size();

        // Update the medicamentos using partial update
        Medicamentos partialUpdatedMedicamentos = new Medicamentos();
        partialUpdatedMedicamentos.setId(medicamentos.getId());

        partialUpdatedMedicamentos
            .descricao(UPDATED_DESCRICAO)
            .registroMinisterioSaude(UPDATED_REGISTRO_MINISTERIO_SAUDE)
            .codigoBarras(UPDATED_CODIGO_BARRAS)
            .qtdAtual(UPDATED_QTD_ATUAL)
            .qtdMin(UPDATED_QTD_MIN)
            .qtdMax(UPDATED_QTD_MAX)
            .observacoes(UPDATED_OBSERVACOES)
            .apresentacao(UPDATED_APRESENTACAO);

        restMedicamentosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedicamentos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMedicamentos))
            )
            .andExpect(status().isOk());

        // Validate the Medicamentos in the database
        List<Medicamentos> medicamentosList = medicamentosRepository.findAll();
        assertThat(medicamentosList).hasSize(databaseSizeBeforeUpdate);
        Medicamentos testMedicamentos = medicamentosList.get(medicamentosList.size() - 1);
        assertThat(testMedicamentos.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testMedicamentos.getRegistroMinisterioSaude()).isEqualTo(UPDATED_REGISTRO_MINISTERIO_SAUDE);
        assertThat(testMedicamentos.getCodigoBarras()).isEqualTo(UPDATED_CODIGO_BARRAS);
        assertThat(testMedicamentos.getQtdAtual()).isEqualTo(UPDATED_QTD_ATUAL);
        assertThat(testMedicamentos.getQtdMin()).isEqualTo(UPDATED_QTD_MIN);
        assertThat(testMedicamentos.getQtdMax()).isEqualTo(UPDATED_QTD_MAX);
        assertThat(testMedicamentos.getObservacoes()).isEqualTo(UPDATED_OBSERVACOES);
        assertThat(testMedicamentos.getApresentacao()).isEqualTo(UPDATED_APRESENTACAO);
    }

    @Test
    @Transactional
    void patchNonExistingMedicamentos() throws Exception {
        int databaseSizeBeforeUpdate = medicamentosRepository.findAll().size();
        medicamentos.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicamentosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, medicamentos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(medicamentos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medicamentos in the database
        List<Medicamentos> medicamentosList = medicamentosRepository.findAll();
        assertThat(medicamentosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMedicamentos() throws Exception {
        int databaseSizeBeforeUpdate = medicamentosRepository.findAll().size();
        medicamentos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicamentosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(medicamentos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medicamentos in the database
        List<Medicamentos> medicamentosList = medicamentosRepository.findAll();
        assertThat(medicamentosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMedicamentos() throws Exception {
        int databaseSizeBeforeUpdate = medicamentosRepository.findAll().size();
        medicamentos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicamentosMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(medicamentos))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Medicamentos in the database
        List<Medicamentos> medicamentosList = medicamentosRepository.findAll();
        assertThat(medicamentosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMedicamentos() throws Exception {
        // Initialize the database
        medicamentosRepository.saveAndFlush(medicamentos);

        int databaseSizeBeforeDelete = medicamentosRepository.findAll().size();

        // Delete the medicamentos
        restMedicamentosMockMvc
            .perform(delete(ENTITY_API_URL_ID, medicamentos.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Medicamentos> medicamentosList = medicamentosRepository.findAll();
        assertThat(medicamentosList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
