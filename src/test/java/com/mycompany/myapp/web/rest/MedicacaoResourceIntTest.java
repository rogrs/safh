package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.SafhApp;
import com.mycompany.myapp.domain.Medicacao;
import com.mycompany.myapp.repository.MedicacaoRepository;
import com.mycompany.myapp.repository.search.MedicacaoSearchRepository;

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
 * Test class for the MedicacaoResource REST controller.
 *
 * @see MedicacaoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SafhApp.class)
@WebAppConfiguration
@IntegrationTest
public class MedicacaoResourceIntTest {


    private static final LocalDate DEFAULT_DATA_MEDICACAO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_MEDICACAO = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_VERIFICAR = "AAAAA";
    private static final String UPDATED_VERIFICAR = "BBBBB";
    private static final String DEFAULT_VALOR = "AAAAA";
    private static final String UPDATED_VALOR = "BBBBB";

    @Inject
    private MedicacaoRepository medicacaoRepository;

    @Inject
    private MedicacaoSearchRepository medicacaoSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMedicacaoMockMvc;

    private Medicacao medicacao;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MedicacaoResource medicacaoResource = new MedicacaoResource();
        ReflectionTestUtils.setField(medicacaoResource, "medicacaoSearchRepository", medicacaoSearchRepository);
        ReflectionTestUtils.setField(medicacaoResource, "medicacaoRepository", medicacaoRepository);
        this.restMedicacaoMockMvc = MockMvcBuilders.standaloneSetup(medicacaoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        medicacaoSearchRepository.deleteAll();
        medicacao = new Medicacao();
        medicacao.setDataMedicacao(DEFAULT_DATA_MEDICACAO);
        medicacao.setVerificar(DEFAULT_VERIFICAR);
        medicacao.setValor(DEFAULT_VALOR);
    }

    @Test
    @Transactional
    public void createMedicacao() throws Exception {
        int databaseSizeBeforeCreate = medicacaoRepository.findAll().size();

        // Create the Medicacao

        restMedicacaoMockMvc.perform(post("/api/medicacaos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(medicacao)))
                .andExpect(status().isCreated());

        // Validate the Medicacao in the database
        List<Medicacao> medicacaos = medicacaoRepository.findAll();
        assertThat(medicacaos).hasSize(databaseSizeBeforeCreate + 1);
        Medicacao testMedicacao = medicacaos.get(medicacaos.size() - 1);
        assertThat(testMedicacao.getDataMedicacao()).isEqualTo(DEFAULT_DATA_MEDICACAO);
        assertThat(testMedicacao.getVerificar()).isEqualTo(DEFAULT_VERIFICAR);
        assertThat(testMedicacao.getValor()).isEqualTo(DEFAULT_VALOR);

        // Validate the Medicacao in ElasticSearch
        Medicacao medicacaoEs = medicacaoSearchRepository.findOne(testMedicacao.getId());
        assertThat(medicacaoEs).isEqualToComparingFieldByField(testMedicacao);
    }

    @Test
    @Transactional
    public void checkDataMedicacaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = medicacaoRepository.findAll().size();
        // set the field null
        medicacao.setDataMedicacao(null);

        // Create the Medicacao, which fails.

        restMedicacaoMockMvc.perform(post("/api/medicacaos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(medicacao)))
                .andExpect(status().isBadRequest());

        List<Medicacao> medicacaos = medicacaoRepository.findAll();
        assertThat(medicacaos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVerificarIsRequired() throws Exception {
        int databaseSizeBeforeTest = medicacaoRepository.findAll().size();
        // set the field null
        medicacao.setVerificar(null);

        // Create the Medicacao, which fails.

        restMedicacaoMockMvc.perform(post("/api/medicacaos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(medicacao)))
                .andExpect(status().isBadRequest());

        List<Medicacao> medicacaos = medicacaoRepository.findAll();
        assertThat(medicacaos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValorIsRequired() throws Exception {
        int databaseSizeBeforeTest = medicacaoRepository.findAll().size();
        // set the field null
        medicacao.setValor(null);

        // Create the Medicacao, which fails.

        restMedicacaoMockMvc.perform(post("/api/medicacaos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(medicacao)))
                .andExpect(status().isBadRequest());

        List<Medicacao> medicacaos = medicacaoRepository.findAll();
        assertThat(medicacaos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMedicacaos() throws Exception {
        // Initialize the database
        medicacaoRepository.saveAndFlush(medicacao);

        // Get all the medicacaos
        restMedicacaoMockMvc.perform(get("/api/medicacaos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(medicacao.getId().intValue())))
                .andExpect(jsonPath("$.[*].dataMedicacao").value(hasItem(DEFAULT_DATA_MEDICACAO.toString())))
                .andExpect(jsonPath("$.[*].verificar").value(hasItem(DEFAULT_VERIFICAR.toString())))
                .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR.toString())));
    }

    @Test
    @Transactional
    public void getMedicacao() throws Exception {
        // Initialize the database
        medicacaoRepository.saveAndFlush(medicacao);

        // Get the medicacao
        restMedicacaoMockMvc.perform(get("/api/medicacaos/{id}", medicacao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(medicacao.getId().intValue()))
            .andExpect(jsonPath("$.dataMedicacao").value(DEFAULT_DATA_MEDICACAO.toString()))
            .andExpect(jsonPath("$.verificar").value(DEFAULT_VERIFICAR.toString()))
            .andExpect(jsonPath("$.valor").value(DEFAULT_VALOR.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMedicacao() throws Exception {
        // Get the medicacao
        restMedicacaoMockMvc.perform(get("/api/medicacaos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMedicacao() throws Exception {
        // Initialize the database
        medicacaoRepository.saveAndFlush(medicacao);
        medicacaoSearchRepository.save(medicacao);
        int databaseSizeBeforeUpdate = medicacaoRepository.findAll().size();

        // Update the medicacao
        Medicacao updatedMedicacao = new Medicacao();
        updatedMedicacao.setId(medicacao.getId());
        updatedMedicacao.setDataMedicacao(UPDATED_DATA_MEDICACAO);
        updatedMedicacao.setVerificar(UPDATED_VERIFICAR);
        updatedMedicacao.setValor(UPDATED_VALOR);

        restMedicacaoMockMvc.perform(put("/api/medicacaos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedMedicacao)))
                .andExpect(status().isOk());

        // Validate the Medicacao in the database
        List<Medicacao> medicacaos = medicacaoRepository.findAll();
        assertThat(medicacaos).hasSize(databaseSizeBeforeUpdate);
        Medicacao testMedicacao = medicacaos.get(medicacaos.size() - 1);
        assertThat(testMedicacao.getDataMedicacao()).isEqualTo(UPDATED_DATA_MEDICACAO);
        assertThat(testMedicacao.getVerificar()).isEqualTo(UPDATED_VERIFICAR);
        assertThat(testMedicacao.getValor()).isEqualTo(UPDATED_VALOR);

        // Validate the Medicacao in ElasticSearch
        Medicacao medicacaoEs = medicacaoSearchRepository.findOne(testMedicacao.getId());
        assertThat(medicacaoEs).isEqualToComparingFieldByField(testMedicacao);
    }

    @Test
    @Transactional
    public void deleteMedicacao() throws Exception {
        // Initialize the database
        medicacaoRepository.saveAndFlush(medicacao);
        medicacaoSearchRepository.save(medicacao);
        int databaseSizeBeforeDelete = medicacaoRepository.findAll().size();

        // Get the medicacao
        restMedicacaoMockMvc.perform(delete("/api/medicacaos/{id}", medicacao.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean medicacaoExistsInEs = medicacaoSearchRepository.exists(medicacao.getId());
        assertThat(medicacaoExistsInEs).isFalse();

        // Validate the database is empty
        List<Medicacao> medicacaos = medicacaoRepository.findAll();
        assertThat(medicacaos).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMedicacao() throws Exception {
        // Initialize the database
        medicacaoRepository.saveAndFlush(medicacao);
        medicacaoSearchRepository.save(medicacao);

        // Search the medicacao
        restMedicacaoMockMvc.perform(get("/api/_search/medicacaos?query=id:" + medicacao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataMedicacao").value(hasItem(DEFAULT_DATA_MEDICACAO.toString())))
            .andExpect(jsonPath("$.[*].verificar").value(hasItem(DEFAULT_VERIFICAR.toString())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR.toString())));
    }
}
