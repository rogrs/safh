package br.com.rogrs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.rogrs.IntegrationTest;
import br.com.rogrs.domain.InternacoesDetalhes;
import br.com.rogrs.repository.InternacoesDetalhesRepository;
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
 * Integration tests for the {@link InternacoesDetalhesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InternacoesDetalhesResourceIT {

    private static final LocalDate DEFAULT_DATA_DETALHE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_DETALHE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_HORARIO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_HORARIO = LocalDate.now(ZoneId.systemDefault());

    private static final Float DEFAULT_QTD = 1F;
    private static final Float UPDATED_QTD = 2F;

    private static final String ENTITY_API_URL = "/api/internacoes-detalhes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InternacoesDetalhesRepository internacoesDetalhesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInternacoesDetalhesMockMvc;

    private InternacoesDetalhes internacoesDetalhes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InternacoesDetalhes createEntity(EntityManager em) {
        InternacoesDetalhes internacoesDetalhes = new InternacoesDetalhes()
            .dataDetalhe(DEFAULT_DATA_DETALHE)
            .horario(DEFAULT_HORARIO)
            .qtd(DEFAULT_QTD);
        return internacoesDetalhes;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InternacoesDetalhes createUpdatedEntity(EntityManager em) {
        InternacoesDetalhes internacoesDetalhes = new InternacoesDetalhes()
            .dataDetalhe(UPDATED_DATA_DETALHE)
            .horario(UPDATED_HORARIO)
            .qtd(UPDATED_QTD);
        return internacoesDetalhes;
    }

    @BeforeEach
    public void initTest() {
        internacoesDetalhes = createEntity(em);
    }

    @Test
    @Transactional
    void createInternacoesDetalhes() throws Exception {
        int databaseSizeBeforeCreate = internacoesDetalhesRepository.findAll().size();
        // Create the InternacoesDetalhes
        restInternacoesDetalhesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(internacoesDetalhes))
            )
            .andExpect(status().isCreated());

        // Validate the InternacoesDetalhes in the database
        List<InternacoesDetalhes> internacoesDetalhesList = internacoesDetalhesRepository.findAll();
        assertThat(internacoesDetalhesList).hasSize(databaseSizeBeforeCreate + 1);
        InternacoesDetalhes testInternacoesDetalhes = internacoesDetalhesList.get(internacoesDetalhesList.size() - 1);
        assertThat(testInternacoesDetalhes.getDataDetalhe()).isEqualTo(DEFAULT_DATA_DETALHE);
        assertThat(testInternacoesDetalhes.getHorario()).isEqualTo(DEFAULT_HORARIO);
        assertThat(testInternacoesDetalhes.getQtd()).isEqualTo(DEFAULT_QTD);
    }

    @Test
    @Transactional
    void createInternacoesDetalhesWithExistingId() throws Exception {
        // Create the InternacoesDetalhes with an existing ID
        internacoesDetalhes.setId(1L);

        int databaseSizeBeforeCreate = internacoesDetalhesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInternacoesDetalhesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(internacoesDetalhes))
            )
            .andExpect(status().isBadRequest());

        // Validate the InternacoesDetalhes in the database
        List<InternacoesDetalhes> internacoesDetalhesList = internacoesDetalhesRepository.findAll();
        assertThat(internacoesDetalhesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDataDetalheIsRequired() throws Exception {
        int databaseSizeBeforeTest = internacoesDetalhesRepository.findAll().size();
        // set the field null
        internacoesDetalhes.setDataDetalhe(null);

        // Create the InternacoesDetalhes, which fails.

        restInternacoesDetalhesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(internacoesDetalhes))
            )
            .andExpect(status().isBadRequest());

        List<InternacoesDetalhes> internacoesDetalhesList = internacoesDetalhesRepository.findAll();
        assertThat(internacoesDetalhesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHorarioIsRequired() throws Exception {
        int databaseSizeBeforeTest = internacoesDetalhesRepository.findAll().size();
        // set the field null
        internacoesDetalhes.setHorario(null);

        // Create the InternacoesDetalhes, which fails.

        restInternacoesDetalhesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(internacoesDetalhes))
            )
            .andExpect(status().isBadRequest());

        List<InternacoesDetalhes> internacoesDetalhesList = internacoesDetalhesRepository.findAll();
        assertThat(internacoesDetalhesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQtdIsRequired() throws Exception {
        int databaseSizeBeforeTest = internacoesDetalhesRepository.findAll().size();
        // set the field null
        internacoesDetalhes.setQtd(null);

        // Create the InternacoesDetalhes, which fails.

        restInternacoesDetalhesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(internacoesDetalhes))
            )
            .andExpect(status().isBadRequest());

        List<InternacoesDetalhes> internacoesDetalhesList = internacoesDetalhesRepository.findAll();
        assertThat(internacoesDetalhesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInternacoesDetalhes() throws Exception {
        // Initialize the database
        internacoesDetalhesRepository.saveAndFlush(internacoesDetalhes);

        // Get all the internacoesDetalhesList
        restInternacoesDetalhesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(internacoesDetalhes.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataDetalhe").value(hasItem(DEFAULT_DATA_DETALHE.toString())))
            .andExpect(jsonPath("$.[*].horario").value(hasItem(DEFAULT_HORARIO.toString())))
            .andExpect(jsonPath("$.[*].qtd").value(hasItem(DEFAULT_QTD.doubleValue())));
    }

    @Test
    @Transactional
    void getInternacoesDetalhes() throws Exception {
        // Initialize the database
        internacoesDetalhesRepository.saveAndFlush(internacoesDetalhes);

        // Get the internacoesDetalhes
        restInternacoesDetalhesMockMvc
            .perform(get(ENTITY_API_URL_ID, internacoesDetalhes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(internacoesDetalhes.getId().intValue()))
            .andExpect(jsonPath("$.dataDetalhe").value(DEFAULT_DATA_DETALHE.toString()))
            .andExpect(jsonPath("$.horario").value(DEFAULT_HORARIO.toString()))
            .andExpect(jsonPath("$.qtd").value(DEFAULT_QTD.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingInternacoesDetalhes() throws Exception {
        // Get the internacoesDetalhes
        restInternacoesDetalhesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInternacoesDetalhes() throws Exception {
        // Initialize the database
        internacoesDetalhesRepository.saveAndFlush(internacoesDetalhes);

        int databaseSizeBeforeUpdate = internacoesDetalhesRepository.findAll().size();

        // Update the internacoesDetalhes
        InternacoesDetalhes updatedInternacoesDetalhes = internacoesDetalhesRepository.findById(internacoesDetalhes.getId()).get();
        // Disconnect from session so that the updates on updatedInternacoesDetalhes are not directly saved in db
        em.detach(updatedInternacoesDetalhes);
        updatedInternacoesDetalhes.dataDetalhe(UPDATED_DATA_DETALHE).horario(UPDATED_HORARIO).qtd(UPDATED_QTD);

        restInternacoesDetalhesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInternacoesDetalhes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedInternacoesDetalhes))
            )
            .andExpect(status().isOk());

        // Validate the InternacoesDetalhes in the database
        List<InternacoesDetalhes> internacoesDetalhesList = internacoesDetalhesRepository.findAll();
        assertThat(internacoesDetalhesList).hasSize(databaseSizeBeforeUpdate);
        InternacoesDetalhes testInternacoesDetalhes = internacoesDetalhesList.get(internacoesDetalhesList.size() - 1);
        assertThat(testInternacoesDetalhes.getDataDetalhe()).isEqualTo(UPDATED_DATA_DETALHE);
        assertThat(testInternacoesDetalhes.getHorario()).isEqualTo(UPDATED_HORARIO);
        assertThat(testInternacoesDetalhes.getQtd()).isEqualTo(UPDATED_QTD);
    }

    @Test
    @Transactional
    void putNonExistingInternacoesDetalhes() throws Exception {
        int databaseSizeBeforeUpdate = internacoesDetalhesRepository.findAll().size();
        internacoesDetalhes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInternacoesDetalhesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, internacoesDetalhes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(internacoesDetalhes))
            )
            .andExpect(status().isBadRequest());

        // Validate the InternacoesDetalhes in the database
        List<InternacoesDetalhes> internacoesDetalhesList = internacoesDetalhesRepository.findAll();
        assertThat(internacoesDetalhesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInternacoesDetalhes() throws Exception {
        int databaseSizeBeforeUpdate = internacoesDetalhesRepository.findAll().size();
        internacoesDetalhes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInternacoesDetalhesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(internacoesDetalhes))
            )
            .andExpect(status().isBadRequest());

        // Validate the InternacoesDetalhes in the database
        List<InternacoesDetalhes> internacoesDetalhesList = internacoesDetalhesRepository.findAll();
        assertThat(internacoesDetalhesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInternacoesDetalhes() throws Exception {
        int databaseSizeBeforeUpdate = internacoesDetalhesRepository.findAll().size();
        internacoesDetalhes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInternacoesDetalhesMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(internacoesDetalhes))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InternacoesDetalhes in the database
        List<InternacoesDetalhes> internacoesDetalhesList = internacoesDetalhesRepository.findAll();
        assertThat(internacoesDetalhesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInternacoesDetalhesWithPatch() throws Exception {
        // Initialize the database
        internacoesDetalhesRepository.saveAndFlush(internacoesDetalhes);

        int databaseSizeBeforeUpdate = internacoesDetalhesRepository.findAll().size();

        // Update the internacoesDetalhes using partial update
        InternacoesDetalhes partialUpdatedInternacoesDetalhes = new InternacoesDetalhes();
        partialUpdatedInternacoesDetalhes.setId(internacoesDetalhes.getId());

        restInternacoesDetalhesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInternacoesDetalhes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInternacoesDetalhes))
            )
            .andExpect(status().isOk());

        // Validate the InternacoesDetalhes in the database
        List<InternacoesDetalhes> internacoesDetalhesList = internacoesDetalhesRepository.findAll();
        assertThat(internacoesDetalhesList).hasSize(databaseSizeBeforeUpdate);
        InternacoesDetalhes testInternacoesDetalhes = internacoesDetalhesList.get(internacoesDetalhesList.size() - 1);
        assertThat(testInternacoesDetalhes.getDataDetalhe()).isEqualTo(DEFAULT_DATA_DETALHE);
        assertThat(testInternacoesDetalhes.getHorario()).isEqualTo(DEFAULT_HORARIO);
        assertThat(testInternacoesDetalhes.getQtd()).isEqualTo(DEFAULT_QTD);
    }

    @Test
    @Transactional
    void fullUpdateInternacoesDetalhesWithPatch() throws Exception {
        // Initialize the database
        internacoesDetalhesRepository.saveAndFlush(internacoesDetalhes);

        int databaseSizeBeforeUpdate = internacoesDetalhesRepository.findAll().size();

        // Update the internacoesDetalhes using partial update
        InternacoesDetalhes partialUpdatedInternacoesDetalhes = new InternacoesDetalhes();
        partialUpdatedInternacoesDetalhes.setId(internacoesDetalhes.getId());

        partialUpdatedInternacoesDetalhes.dataDetalhe(UPDATED_DATA_DETALHE).horario(UPDATED_HORARIO).qtd(UPDATED_QTD);

        restInternacoesDetalhesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInternacoesDetalhes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInternacoesDetalhes))
            )
            .andExpect(status().isOk());

        // Validate the InternacoesDetalhes in the database
        List<InternacoesDetalhes> internacoesDetalhesList = internacoesDetalhesRepository.findAll();
        assertThat(internacoesDetalhesList).hasSize(databaseSizeBeforeUpdate);
        InternacoesDetalhes testInternacoesDetalhes = internacoesDetalhesList.get(internacoesDetalhesList.size() - 1);
        assertThat(testInternacoesDetalhes.getDataDetalhe()).isEqualTo(UPDATED_DATA_DETALHE);
        assertThat(testInternacoesDetalhes.getHorario()).isEqualTo(UPDATED_HORARIO);
        assertThat(testInternacoesDetalhes.getQtd()).isEqualTo(UPDATED_QTD);
    }

    @Test
    @Transactional
    void patchNonExistingInternacoesDetalhes() throws Exception {
        int databaseSizeBeforeUpdate = internacoesDetalhesRepository.findAll().size();
        internacoesDetalhes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInternacoesDetalhesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, internacoesDetalhes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(internacoesDetalhes))
            )
            .andExpect(status().isBadRequest());

        // Validate the InternacoesDetalhes in the database
        List<InternacoesDetalhes> internacoesDetalhesList = internacoesDetalhesRepository.findAll();
        assertThat(internacoesDetalhesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInternacoesDetalhes() throws Exception {
        int databaseSizeBeforeUpdate = internacoesDetalhesRepository.findAll().size();
        internacoesDetalhes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInternacoesDetalhesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(internacoesDetalhes))
            )
            .andExpect(status().isBadRequest());

        // Validate the InternacoesDetalhes in the database
        List<InternacoesDetalhes> internacoesDetalhesList = internacoesDetalhesRepository.findAll();
        assertThat(internacoesDetalhesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInternacoesDetalhes() throws Exception {
        int databaseSizeBeforeUpdate = internacoesDetalhesRepository.findAll().size();
        internacoesDetalhes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInternacoesDetalhesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(internacoesDetalhes))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InternacoesDetalhes in the database
        List<InternacoesDetalhes> internacoesDetalhesList = internacoesDetalhesRepository.findAll();
        assertThat(internacoesDetalhesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInternacoesDetalhes() throws Exception {
        // Initialize the database
        internacoesDetalhesRepository.saveAndFlush(internacoesDetalhes);

        int databaseSizeBeforeDelete = internacoesDetalhesRepository.findAll().size();

        // Delete the internacoesDetalhes
        restInternacoesDetalhesMockMvc
            .perform(delete(ENTITY_API_URL_ID, internacoesDetalhes.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InternacoesDetalhes> internacoesDetalhesList = internacoesDetalhesRepository.findAll();
        assertThat(internacoesDetalhesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
