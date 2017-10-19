package br.com.rogrs.safh.web.rest;

import br.com.rogrs.safh.SafhApp;

import br.com.rogrs.safh.domain.Medicamentos;
import br.com.rogrs.safh.repository.MedicamentosRepository;
import br.com.rogrs.safh.repository.search.MedicamentosSearchRepository;
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

/**
 * Test class for the MedicamentosResource REST controller.
 *
 * @see MedicamentosResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SafhApp.class)
public class MedicamentosResourceIntTest {

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

    @Autowired
    private MedicamentosRepository medicamentosRepository;

    @Autowired
    private MedicamentosSearchRepository medicamentosSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMedicamentosMockMvc;

    private Medicamentos medicamentos;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MedicamentosResource medicamentosResource = new MedicamentosResource(medicamentosRepository, medicamentosSearchRepository);
        this.restMedicamentosMockMvc = MockMvcBuilders.standaloneSetup(medicamentosResource)
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

    @Before
    public void initTest() {
        medicamentosSearchRepository.deleteAll();
        medicamentos = createEntity(em);
    }

    @Test
    @Transactional
    public void createMedicamentos() throws Exception {
        int databaseSizeBeforeCreate = medicamentosRepository.findAll().size();

        // Create the Medicamentos
        restMedicamentosMockMvc.perform(post("/api/medicamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medicamentos)))
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

        // Validate the Medicamentos in Elasticsearch
        Medicamentos medicamentosEs = medicamentosSearchRepository.findOne(testMedicamentos.getId());
        assertThat(medicamentosEs).isEqualToComparingFieldByField(testMedicamentos);
    }

    @Test
    @Transactional
    public void createMedicamentosWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = medicamentosRepository.findAll().size();

        // Create the Medicamentos with an existing ID
        medicamentos.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedicamentosMockMvc.perform(post("/api/medicamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medicamentos)))
            .andExpect(status().isBadRequest());

        // Validate the Medicamentos in the database
        List<Medicamentos> medicamentosList = medicamentosRepository.findAll();
        assertThat(medicamentosList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = medicamentosRepository.findAll().size();
        // set the field null
        medicamentos.setDescricao(null);

        // Create the Medicamentos, which fails.

        restMedicamentosMockMvc.perform(post("/api/medicamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medicamentos)))
            .andExpect(status().isBadRequest());

        List<Medicamentos> medicamentosList = medicamentosRepository.findAll();
        assertThat(medicamentosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRegistroMinisterioSaudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = medicamentosRepository.findAll().size();
        // set the field null
        medicamentos.setRegistroMinisterioSaude(null);

        // Create the Medicamentos, which fails.

        restMedicamentosMockMvc.perform(post("/api/medicamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medicamentos)))
            .andExpect(status().isBadRequest());

        List<Medicamentos> medicamentosList = medicamentosRepository.findAll();
        assertThat(medicamentosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCodigoBarrasIsRequired() throws Exception {
        int databaseSizeBeforeTest = medicamentosRepository.findAll().size();
        // set the field null
        medicamentos.setCodigoBarras(null);

        // Create the Medicamentos, which fails.

        restMedicamentosMockMvc.perform(post("/api/medicamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medicamentos)))
            .andExpect(status().isBadRequest());

        List<Medicamentos> medicamentosList = medicamentosRepository.findAll();
        assertThat(medicamentosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMedicamentos() throws Exception {
        // Initialize the database
        medicamentosRepository.saveAndFlush(medicamentos);

        // Get all the medicamentosList
        restMedicamentosMockMvc.perform(get("/api/medicamentos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicamentos.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())))
            .andExpect(jsonPath("$.[*].registroMinisterioSaude").value(hasItem(DEFAULT_REGISTRO_MINISTERIO_SAUDE.toString())))
            .andExpect(jsonPath("$.[*].codigoBarras").value(hasItem(DEFAULT_CODIGO_BARRAS.toString())))
            .andExpect(jsonPath("$.[*].qtdAtual").value(hasItem(DEFAULT_QTD_ATUAL.doubleValue())))
            .andExpect(jsonPath("$.[*].qtdMin").value(hasItem(DEFAULT_QTD_MIN.doubleValue())))
            .andExpect(jsonPath("$.[*].qtdMax").value(hasItem(DEFAULT_QTD_MAX.doubleValue())))
            .andExpect(jsonPath("$.[*].observacoes").value(hasItem(DEFAULT_OBSERVACOES.toString())))
            .andExpect(jsonPath("$.[*].apresentacao").value(hasItem(DEFAULT_APRESENTACAO.toString())));
    }

    @Test
    @Transactional
    public void getMedicamentos() throws Exception {
        // Initialize the database
        medicamentosRepository.saveAndFlush(medicamentos);

        // Get the medicamentos
        restMedicamentosMockMvc.perform(get("/api/medicamentos/{id}", medicamentos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(medicamentos.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()))
            .andExpect(jsonPath("$.registroMinisterioSaude").value(DEFAULT_REGISTRO_MINISTERIO_SAUDE.toString()))
            .andExpect(jsonPath("$.codigoBarras").value(DEFAULT_CODIGO_BARRAS.toString()))
            .andExpect(jsonPath("$.qtdAtual").value(DEFAULT_QTD_ATUAL.doubleValue()))
            .andExpect(jsonPath("$.qtdMin").value(DEFAULT_QTD_MIN.doubleValue()))
            .andExpect(jsonPath("$.qtdMax").value(DEFAULT_QTD_MAX.doubleValue()))
            .andExpect(jsonPath("$.observacoes").value(DEFAULT_OBSERVACOES.toString()))
            .andExpect(jsonPath("$.apresentacao").value(DEFAULT_APRESENTACAO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMedicamentos() throws Exception {
        // Get the medicamentos
        restMedicamentosMockMvc.perform(get("/api/medicamentos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMedicamentos() throws Exception {
        // Initialize the database
        medicamentosRepository.saveAndFlush(medicamentos);
        medicamentosSearchRepository.save(medicamentos);
        int databaseSizeBeforeUpdate = medicamentosRepository.findAll().size();

        // Update the medicamentos
        Medicamentos updatedMedicamentos = medicamentosRepository.findOne(medicamentos.getId());
        updatedMedicamentos
            .descricao(UPDATED_DESCRICAO)
            .registroMinisterioSaude(UPDATED_REGISTRO_MINISTERIO_SAUDE)
            .codigoBarras(UPDATED_CODIGO_BARRAS)
            .qtdAtual(UPDATED_QTD_ATUAL)
            .qtdMin(UPDATED_QTD_MIN)
            .qtdMax(UPDATED_QTD_MAX)
            .observacoes(UPDATED_OBSERVACOES)
            .apresentacao(UPDATED_APRESENTACAO);

        restMedicamentosMockMvc.perform(put("/api/medicamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMedicamentos)))
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

        // Validate the Medicamentos in Elasticsearch
        Medicamentos medicamentosEs = medicamentosSearchRepository.findOne(testMedicamentos.getId());
        assertThat(medicamentosEs).isEqualToComparingFieldByField(testMedicamentos);
    }

    @Test
    @Transactional
    public void updateNonExistingMedicamentos() throws Exception {
        int databaseSizeBeforeUpdate = medicamentosRepository.findAll().size();

        // Create the Medicamentos

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMedicamentosMockMvc.perform(put("/api/medicamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medicamentos)))
            .andExpect(status().isCreated());

        // Validate the Medicamentos in the database
        List<Medicamentos> medicamentosList = medicamentosRepository.findAll();
        assertThat(medicamentosList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMedicamentos() throws Exception {
        // Initialize the database
        medicamentosRepository.saveAndFlush(medicamentos);
        medicamentosSearchRepository.save(medicamentos);
        int databaseSizeBeforeDelete = medicamentosRepository.findAll().size();

        // Get the medicamentos
        restMedicamentosMockMvc.perform(delete("/api/medicamentos/{id}", medicamentos.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean medicamentosExistsInEs = medicamentosSearchRepository.exists(medicamentos.getId());
        assertThat(medicamentosExistsInEs).isFalse();

        // Validate the database is empty
        List<Medicamentos> medicamentosList = medicamentosRepository.findAll();
        assertThat(medicamentosList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMedicamentos() throws Exception {
        // Initialize the database
        medicamentosRepository.saveAndFlush(medicamentos);
        medicamentosSearchRepository.save(medicamentos);

        // Search the medicamentos
        restMedicamentosMockMvc.perform(get("/api/_search/medicamentos?query=id:" + medicamentos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicamentos.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())))
            .andExpect(jsonPath("$.[*].registroMinisterioSaude").value(hasItem(DEFAULT_REGISTRO_MINISTERIO_SAUDE.toString())))
            .andExpect(jsonPath("$.[*].codigoBarras").value(hasItem(DEFAULT_CODIGO_BARRAS.toString())))
            .andExpect(jsonPath("$.[*].qtdAtual").value(hasItem(DEFAULT_QTD_ATUAL.doubleValue())))
            .andExpect(jsonPath("$.[*].qtdMin").value(hasItem(DEFAULT_QTD_MIN.doubleValue())))
            .andExpect(jsonPath("$.[*].qtdMax").value(hasItem(DEFAULT_QTD_MAX.doubleValue())))
            .andExpect(jsonPath("$.[*].observacoes").value(hasItem(DEFAULT_OBSERVACOES.toString())))
            .andExpect(jsonPath("$.[*].apresentacao").value(hasItem(DEFAULT_APRESENTACAO.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Medicamentos.class);
        Medicamentos medicamentos1 = new Medicamentos();
        medicamentos1.setId(1L);
        Medicamentos medicamentos2 = new Medicamentos();
        medicamentos2.setId(medicamentos1.getId());
        assertThat(medicamentos1).isEqualTo(medicamentos2);
        medicamentos2.setId(2L);
        assertThat(medicamentos1).isNotEqualTo(medicamentos2);
        medicamentos1.setId(null);
        assertThat(medicamentos1).isNotEqualTo(medicamentos2);
    }
}
