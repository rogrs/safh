package br.com.rogrs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.rogrs.IntegrationTest;
import br.com.rogrs.domain.Prescricoes;
import br.com.rogrs.repository.PrescricoesRepository;
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
 * Integration tests for the {@link PrescricoesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PrescricoesResourceIT {

    private static final String DEFAULT_PRESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_PRESCRICAO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/prescricoes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PrescricoesRepository prescricoesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPrescricoesMockMvc;

    private Prescricoes prescricoes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Prescricoes createEntity(EntityManager em) {
        Prescricoes prescricoes = new Prescricoes().prescricao(DEFAULT_PRESCRICAO);
        return prescricoes;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Prescricoes createUpdatedEntity(EntityManager em) {
        Prescricoes prescricoes = new Prescricoes().prescricao(UPDATED_PRESCRICAO);
        return prescricoes;
    }

    @BeforeEach
    public void initTest() {
        prescricoes = createEntity(em);
    }

    @Test
    @Transactional
    void createPrescricoes() throws Exception {
        int databaseSizeBeforeCreate = prescricoesRepository.findAll().size();
        // Create the Prescricoes
        restPrescricoesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prescricoes)))
            .andExpect(status().isCreated());

        // Validate the Prescricoes in the database
        List<Prescricoes> prescricoesList = prescricoesRepository.findAll();
        assertThat(prescricoesList).hasSize(databaseSizeBeforeCreate + 1);
        Prescricoes testPrescricoes = prescricoesList.get(prescricoesList.size() - 1);
        assertThat(testPrescricoes.getPrescricao()).isEqualTo(DEFAULT_PRESCRICAO);
    }

    @Test
    @Transactional
    void createPrescricoesWithExistingId() throws Exception {
        // Create the Prescricoes with an existing ID
        prescricoes.setId(1L);

        int databaseSizeBeforeCreate = prescricoesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrescricoesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prescricoes)))
            .andExpect(status().isBadRequest());

        // Validate the Prescricoes in the database
        List<Prescricoes> prescricoesList = prescricoesRepository.findAll();
        assertThat(prescricoesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPrescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = prescricoesRepository.findAll().size();
        // set the field null
        prescricoes.setPrescricao(null);

        // Create the Prescricoes, which fails.

        restPrescricoesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prescricoes)))
            .andExpect(status().isBadRequest());

        List<Prescricoes> prescricoesList = prescricoesRepository.findAll();
        assertThat(prescricoesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPrescricoes() throws Exception {
        // Initialize the database
        prescricoesRepository.saveAndFlush(prescricoes);

        // Get all the prescricoesList
        restPrescricoesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prescricoes.getId().intValue())))
            .andExpect(jsonPath("$.[*].prescricao").value(hasItem(DEFAULT_PRESCRICAO)));
    }

    @Test
    @Transactional
    void getPrescricoes() throws Exception {
        // Initialize the database
        prescricoesRepository.saveAndFlush(prescricoes);

        // Get the prescricoes
        restPrescricoesMockMvc
            .perform(get(ENTITY_API_URL_ID, prescricoes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(prescricoes.getId().intValue()))
            .andExpect(jsonPath("$.prescricao").value(DEFAULT_PRESCRICAO));
    }

    @Test
    @Transactional
    void getNonExistingPrescricoes() throws Exception {
        // Get the prescricoes
        restPrescricoesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPrescricoes() throws Exception {
        // Initialize the database
        prescricoesRepository.saveAndFlush(prescricoes);

        int databaseSizeBeforeUpdate = prescricoesRepository.findAll().size();

        // Update the prescricoes
        Prescricoes updatedPrescricoes = prescricoesRepository.findById(prescricoes.getId()).get();
        // Disconnect from session so that the updates on updatedPrescricoes are not directly saved in db
        em.detach(updatedPrescricoes);
        updatedPrescricoes.prescricao(UPDATED_PRESCRICAO);

        restPrescricoesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPrescricoes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPrescricoes))
            )
            .andExpect(status().isOk());

        // Validate the Prescricoes in the database
        List<Prescricoes> prescricoesList = prescricoesRepository.findAll();
        assertThat(prescricoesList).hasSize(databaseSizeBeforeUpdate);
        Prescricoes testPrescricoes = prescricoesList.get(prescricoesList.size() - 1);
        assertThat(testPrescricoes.getPrescricao()).isEqualTo(UPDATED_PRESCRICAO);
    }

    @Test
    @Transactional
    void putNonExistingPrescricoes() throws Exception {
        int databaseSizeBeforeUpdate = prescricoesRepository.findAll().size();
        prescricoes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrescricoesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prescricoes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prescricoes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prescricoes in the database
        List<Prescricoes> prescricoesList = prescricoesRepository.findAll();
        assertThat(prescricoesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPrescricoes() throws Exception {
        int databaseSizeBeforeUpdate = prescricoesRepository.findAll().size();
        prescricoes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrescricoesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prescricoes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prescricoes in the database
        List<Prescricoes> prescricoesList = prescricoesRepository.findAll();
        assertThat(prescricoesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPrescricoes() throws Exception {
        int databaseSizeBeforeUpdate = prescricoesRepository.findAll().size();
        prescricoes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrescricoesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prescricoes)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Prescricoes in the database
        List<Prescricoes> prescricoesList = prescricoesRepository.findAll();
        assertThat(prescricoesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePrescricoesWithPatch() throws Exception {
        // Initialize the database
        prescricoesRepository.saveAndFlush(prescricoes);

        int databaseSizeBeforeUpdate = prescricoesRepository.findAll().size();

        // Update the prescricoes using partial update
        Prescricoes partialUpdatedPrescricoes = new Prescricoes();
        partialUpdatedPrescricoes.setId(prescricoes.getId());

        partialUpdatedPrescricoes.prescricao(UPDATED_PRESCRICAO);

        restPrescricoesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrescricoes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrescricoes))
            )
            .andExpect(status().isOk());

        // Validate the Prescricoes in the database
        List<Prescricoes> prescricoesList = prescricoesRepository.findAll();
        assertThat(prescricoesList).hasSize(databaseSizeBeforeUpdate);
        Prescricoes testPrescricoes = prescricoesList.get(prescricoesList.size() - 1);
        assertThat(testPrescricoes.getPrescricao()).isEqualTo(UPDATED_PRESCRICAO);
    }

    @Test
    @Transactional
    void fullUpdatePrescricoesWithPatch() throws Exception {
        // Initialize the database
        prescricoesRepository.saveAndFlush(prescricoes);

        int databaseSizeBeforeUpdate = prescricoesRepository.findAll().size();

        // Update the prescricoes using partial update
        Prescricoes partialUpdatedPrescricoes = new Prescricoes();
        partialUpdatedPrescricoes.setId(prescricoes.getId());

        partialUpdatedPrescricoes.prescricao(UPDATED_PRESCRICAO);

        restPrescricoesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrescricoes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrescricoes))
            )
            .andExpect(status().isOk());

        // Validate the Prescricoes in the database
        List<Prescricoes> prescricoesList = prescricoesRepository.findAll();
        assertThat(prescricoesList).hasSize(databaseSizeBeforeUpdate);
        Prescricoes testPrescricoes = prescricoesList.get(prescricoesList.size() - 1);
        assertThat(testPrescricoes.getPrescricao()).isEqualTo(UPDATED_PRESCRICAO);
    }

    @Test
    @Transactional
    void patchNonExistingPrescricoes() throws Exception {
        int databaseSizeBeforeUpdate = prescricoesRepository.findAll().size();
        prescricoes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrescricoesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, prescricoes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prescricoes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prescricoes in the database
        List<Prescricoes> prescricoesList = prescricoesRepository.findAll();
        assertThat(prescricoesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPrescricoes() throws Exception {
        int databaseSizeBeforeUpdate = prescricoesRepository.findAll().size();
        prescricoes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrescricoesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prescricoes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prescricoes in the database
        List<Prescricoes> prescricoesList = prescricoesRepository.findAll();
        assertThat(prescricoesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPrescricoes() throws Exception {
        int databaseSizeBeforeUpdate = prescricoesRepository.findAll().size();
        prescricoes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrescricoesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(prescricoes))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Prescricoes in the database
        List<Prescricoes> prescricoesList = prescricoesRepository.findAll();
        assertThat(prescricoesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePrescricoes() throws Exception {
        // Initialize the database
        prescricoesRepository.saveAndFlush(prescricoes);

        int databaseSizeBeforeDelete = prescricoesRepository.findAll().size();

        // Delete the prescricoes
        restPrescricoesMockMvc
            .perform(delete(ENTITY_API_URL_ID, prescricoes.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Prescricoes> prescricoesList = prescricoesRepository.findAll();
        assertThat(prescricoesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
