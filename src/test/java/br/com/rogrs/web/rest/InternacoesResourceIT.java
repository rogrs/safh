package br.com.rogrs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.rogrs.IntegrationTest;
import br.com.rogrs.domain.Internacoes;
import br.com.rogrs.repository.InternacoesRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link InternacoesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InternacoesResourceIT {

    private static final LocalDate DEFAULT_DATA_INTERNACAO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_INTERNACAO = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/internacoes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InternacoesRepository internacoesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInternacoesMockMvc;

    private Internacoes internacoes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Internacoes createEntity(EntityManager em) {
        Internacoes internacoes = new Internacoes().dataInternacao(DEFAULT_DATA_INTERNACAO).descricao(DEFAULT_DESCRICAO);
        return internacoes;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Internacoes createUpdatedEntity(EntityManager em) {
        Internacoes internacoes = new Internacoes().dataInternacao(UPDATED_DATA_INTERNACAO).descricao(UPDATED_DESCRICAO);
        return internacoes;
    }

    @BeforeEach
    public void initTest() {
        internacoes = createEntity(em);
    }

    @Test
    @Transactional
    void createInternacoes() throws Exception {
        int databaseSizeBeforeCreate = internacoesRepository.findAll().size();
        // Create the Internacoes
        restInternacoesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(internacoes)))
            .andExpect(status().isCreated());

        // Validate the Internacoes in the database
        List<Internacoes> internacoesList = internacoesRepository.findAll();
        assertThat(internacoesList).hasSize(databaseSizeBeforeCreate + 1);
        Internacoes testInternacoes = internacoesList.get(internacoesList.size() - 1);
        assertThat(testInternacoes.getDataInternacao()).isEqualTo(DEFAULT_DATA_INTERNACAO);
        assertThat(testInternacoes.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    void createInternacoesWithExistingId() throws Exception {
        // Create the Internacoes with an existing ID
        internacoes.setId(1L);

        int databaseSizeBeforeCreate = internacoesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInternacoesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(internacoes)))
            .andExpect(status().isBadRequest());

        // Validate the Internacoes in the database
        List<Internacoes> internacoesList = internacoesRepository.findAll();
        assertThat(internacoesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDataInternacaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = internacoesRepository.findAll().size();
        // set the field null
        internacoes.setDataInternacao(null);

        // Create the Internacoes, which fails.

        restInternacoesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(internacoes)))
            .andExpect(status().isBadRequest());

        List<Internacoes> internacoesList = internacoesRepository.findAll();
        assertThat(internacoesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = internacoesRepository.findAll().size();
        // set the field null
        internacoes.setDescricao(null);

        // Create the Internacoes, which fails.

        restInternacoesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(internacoes)))
            .andExpect(status().isBadRequest());

        List<Internacoes> internacoesList = internacoesRepository.findAll();
        assertThat(internacoesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInternacoes() throws Exception {
        // Initialize the database
        internacoesRepository.saveAndFlush(internacoes);

        // Get all the internacoesList
        restInternacoesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(internacoes.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataInternacao").value(hasItem(DEFAULT_DATA_INTERNACAO.toString())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));
    }

    @Test
    @Transactional
    void getInternacoes() throws Exception {
        // Initialize the database
        internacoesRepository.saveAndFlush(internacoes);

        // Get the internacoes
        restInternacoesMockMvc
            .perform(get(ENTITY_API_URL_ID, internacoes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(internacoes.getId().intValue()))
            .andExpect(jsonPath("$.dataInternacao").value(DEFAULT_DATA_INTERNACAO.toString()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO));
    }

    @Test
    @Transactional
    void getNonExistingInternacoes() throws Exception {
        // Get the internacoes
        restInternacoesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInternacoes() throws Exception {
        // Initialize the database
        internacoesRepository.saveAndFlush(internacoes);

        int databaseSizeBeforeUpdate = internacoesRepository.findAll().size();

        // Update the internacoes
        Internacoes updatedInternacoes = internacoesRepository.findById(internacoes.getId()).get();
        // Disconnect from session so that the updates on updatedInternacoes are not directly saved in db
        em.detach(updatedInternacoes);
        updatedInternacoes.dataInternacao(UPDATED_DATA_INTERNACAO).descricao(UPDATED_DESCRICAO);

        restInternacoesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInternacoes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedInternacoes))
            )
            .andExpect(status().isOk());

        // Validate the Internacoes in the database
        List<Internacoes> internacoesList = internacoesRepository.findAll();
        assertThat(internacoesList).hasSize(databaseSizeBeforeUpdate);
        Internacoes testInternacoes = internacoesList.get(internacoesList.size() - 1);
        assertThat(testInternacoes.getDataInternacao()).isEqualTo(UPDATED_DATA_INTERNACAO);
        assertThat(testInternacoes.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void putNonExistingInternacoes() throws Exception {
        int databaseSizeBeforeUpdate = internacoesRepository.findAll().size();
        internacoes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInternacoesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, internacoes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(internacoes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Internacoes in the database
        List<Internacoes> internacoesList = internacoesRepository.findAll();
        assertThat(internacoesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInternacoes() throws Exception {
        int databaseSizeBeforeUpdate = internacoesRepository.findAll().size();
        internacoes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInternacoesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(internacoes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Internacoes in the database
        List<Internacoes> internacoesList = internacoesRepository.findAll();
        assertThat(internacoesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInternacoes() throws Exception {
        int databaseSizeBeforeUpdate = internacoesRepository.findAll().size();
        internacoes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInternacoesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(internacoes)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Internacoes in the database
        List<Internacoes> internacoesList = internacoesRepository.findAll();
        assertThat(internacoesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInternacoesWithPatch() throws Exception {
        // Initialize the database
        internacoesRepository.saveAndFlush(internacoes);

        int databaseSizeBeforeUpdate = internacoesRepository.findAll().size();

        // Update the internacoes using partial update
        Internacoes partialUpdatedInternacoes = new Internacoes();
        partialUpdatedInternacoes.setId(internacoes.getId());

        partialUpdatedInternacoes.dataInternacao(UPDATED_DATA_INTERNACAO).descricao(UPDATED_DESCRICAO);

        restInternacoesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInternacoes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInternacoes))
            )
            .andExpect(status().isOk());

        // Validate the Internacoes in the database
        List<Internacoes> internacoesList = internacoesRepository.findAll();
        assertThat(internacoesList).hasSize(databaseSizeBeforeUpdate);
        Internacoes testInternacoes = internacoesList.get(internacoesList.size() - 1);
        assertThat(testInternacoes.getDataInternacao()).isEqualTo(UPDATED_DATA_INTERNACAO);
        assertThat(testInternacoes.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void fullUpdateInternacoesWithPatch() throws Exception {
        // Initialize the database
        internacoesRepository.saveAndFlush(internacoes);

        int databaseSizeBeforeUpdate = internacoesRepository.findAll().size();

        // Update the internacoes using partial update
        Internacoes partialUpdatedInternacoes = new Internacoes();
        partialUpdatedInternacoes.setId(internacoes.getId());

        partialUpdatedInternacoes.dataInternacao(UPDATED_DATA_INTERNACAO).descricao(UPDATED_DESCRICAO);

        restInternacoesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInternacoes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInternacoes))
            )
            .andExpect(status().isOk());

        // Validate the Internacoes in the database
        List<Internacoes> internacoesList = internacoesRepository.findAll();
        assertThat(internacoesList).hasSize(databaseSizeBeforeUpdate);
        Internacoes testInternacoes = internacoesList.get(internacoesList.size() - 1);
        assertThat(testInternacoes.getDataInternacao()).isEqualTo(UPDATED_DATA_INTERNACAO);
        assertThat(testInternacoes.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void patchNonExistingInternacoes() throws Exception {
        int databaseSizeBeforeUpdate = internacoesRepository.findAll().size();
        internacoes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInternacoesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, internacoes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(internacoes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Internacoes in the database
        List<Internacoes> internacoesList = internacoesRepository.findAll();
        assertThat(internacoesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInternacoes() throws Exception {
        int databaseSizeBeforeUpdate = internacoesRepository.findAll().size();
        internacoes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInternacoesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(internacoes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Internacoes in the database
        List<Internacoes> internacoesList = internacoesRepository.findAll();
        assertThat(internacoesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInternacoes() throws Exception {
        int databaseSizeBeforeUpdate = internacoesRepository.findAll().size();
        internacoes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInternacoesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(internacoes))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Internacoes in the database
        List<Internacoes> internacoesList = internacoesRepository.findAll();
        assertThat(internacoesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInternacoes() throws Exception {
        // Initialize the database
        internacoesRepository.saveAndFlush(internacoes);

        int databaseSizeBeforeDelete = internacoesRepository.findAll().size();

        // Delete the internacoes
        restInternacoesMockMvc
            .perform(delete(ENTITY_API_URL_ID, internacoes.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Internacoes> internacoesList = internacoesRepository.findAll();
        assertThat(internacoesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
