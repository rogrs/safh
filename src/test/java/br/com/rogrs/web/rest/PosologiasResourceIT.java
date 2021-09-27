package br.com.rogrs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.rogrs.IntegrationTest;
import br.com.rogrs.domain.Posologias;
import br.com.rogrs.repository.PosologiasRepository;
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
 * Integration tests for the {@link PosologiasResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PosologiasResourceIT {

    private static final String DEFAULT_POSOLOGIA = "AAAAAAAAAA";
    private static final String UPDATED_POSOLOGIA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/posologias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PosologiasRepository posologiasRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPosologiasMockMvc;

    private Posologias posologias;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Posologias createEntity(EntityManager em) {
        Posologias posologias = new Posologias().posologia(DEFAULT_POSOLOGIA);
        return posologias;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Posologias createUpdatedEntity(EntityManager em) {
        Posologias posologias = new Posologias().posologia(UPDATED_POSOLOGIA);
        return posologias;
    }

    @BeforeEach
    public void initTest() {
        posologias = createEntity(em);
    }

    @Test
    @Transactional
    void createPosologias() throws Exception {
        int databaseSizeBeforeCreate = posologiasRepository.findAll().size();
        // Create the Posologias
        restPosologiasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(posologias)))
            .andExpect(status().isCreated());

        // Validate the Posologias in the database
        List<Posologias> posologiasList = posologiasRepository.findAll();
        assertThat(posologiasList).hasSize(databaseSizeBeforeCreate + 1);
        Posologias testPosologias = posologiasList.get(posologiasList.size() - 1);
        assertThat(testPosologias.getPosologia()).isEqualTo(DEFAULT_POSOLOGIA);
    }

    @Test
    @Transactional
    void createPosologiasWithExistingId() throws Exception {
        // Create the Posologias with an existing ID
        posologias.setId(1L);

        int databaseSizeBeforeCreate = posologiasRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPosologiasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(posologias)))
            .andExpect(status().isBadRequest());

        // Validate the Posologias in the database
        List<Posologias> posologiasList = posologiasRepository.findAll();
        assertThat(posologiasList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPosologiaIsRequired() throws Exception {
        int databaseSizeBeforeTest = posologiasRepository.findAll().size();
        // set the field null
        posologias.setPosologia(null);

        // Create the Posologias, which fails.

        restPosologiasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(posologias)))
            .andExpect(status().isBadRequest());

        List<Posologias> posologiasList = posologiasRepository.findAll();
        assertThat(posologiasList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPosologias() throws Exception {
        // Initialize the database
        posologiasRepository.saveAndFlush(posologias);

        // Get all the posologiasList
        restPosologiasMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(posologias.getId().intValue())))
            .andExpect(jsonPath("$.[*].posologia").value(hasItem(DEFAULT_POSOLOGIA)));
    }

    @Test
    @Transactional
    void getPosologias() throws Exception {
        // Initialize the database
        posologiasRepository.saveAndFlush(posologias);

        // Get the posologias
        restPosologiasMockMvc
            .perform(get(ENTITY_API_URL_ID, posologias.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(posologias.getId().intValue()))
            .andExpect(jsonPath("$.posologia").value(DEFAULT_POSOLOGIA));
    }

    @Test
    @Transactional
    void getNonExistingPosologias() throws Exception {
        // Get the posologias
        restPosologiasMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPosologias() throws Exception {
        // Initialize the database
        posologiasRepository.saveAndFlush(posologias);

        int databaseSizeBeforeUpdate = posologiasRepository.findAll().size();

        // Update the posologias
        Posologias updatedPosologias = posologiasRepository.findById(posologias.getId()).get();
        // Disconnect from session so that the updates on updatedPosologias are not directly saved in db
        em.detach(updatedPosologias);
        updatedPosologias.posologia(UPDATED_POSOLOGIA);

        restPosologiasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPosologias.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPosologias))
            )
            .andExpect(status().isOk());

        // Validate the Posologias in the database
        List<Posologias> posologiasList = posologiasRepository.findAll();
        assertThat(posologiasList).hasSize(databaseSizeBeforeUpdate);
        Posologias testPosologias = posologiasList.get(posologiasList.size() - 1);
        assertThat(testPosologias.getPosologia()).isEqualTo(UPDATED_POSOLOGIA);
    }

    @Test
    @Transactional
    void putNonExistingPosologias() throws Exception {
        int databaseSizeBeforeUpdate = posologiasRepository.findAll().size();
        posologias.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPosologiasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, posologias.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(posologias))
            )
            .andExpect(status().isBadRequest());

        // Validate the Posologias in the database
        List<Posologias> posologiasList = posologiasRepository.findAll();
        assertThat(posologiasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPosologias() throws Exception {
        int databaseSizeBeforeUpdate = posologiasRepository.findAll().size();
        posologias.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPosologiasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(posologias))
            )
            .andExpect(status().isBadRequest());

        // Validate the Posologias in the database
        List<Posologias> posologiasList = posologiasRepository.findAll();
        assertThat(posologiasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPosologias() throws Exception {
        int databaseSizeBeforeUpdate = posologiasRepository.findAll().size();
        posologias.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPosologiasMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(posologias)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Posologias in the database
        List<Posologias> posologiasList = posologiasRepository.findAll();
        assertThat(posologiasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePosologiasWithPatch() throws Exception {
        // Initialize the database
        posologiasRepository.saveAndFlush(posologias);

        int databaseSizeBeforeUpdate = posologiasRepository.findAll().size();

        // Update the posologias using partial update
        Posologias partialUpdatedPosologias = new Posologias();
        partialUpdatedPosologias.setId(posologias.getId());

        restPosologiasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPosologias.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPosologias))
            )
            .andExpect(status().isOk());

        // Validate the Posologias in the database
        List<Posologias> posologiasList = posologiasRepository.findAll();
        assertThat(posologiasList).hasSize(databaseSizeBeforeUpdate);
        Posologias testPosologias = posologiasList.get(posologiasList.size() - 1);
        assertThat(testPosologias.getPosologia()).isEqualTo(DEFAULT_POSOLOGIA);
    }

    @Test
    @Transactional
    void fullUpdatePosologiasWithPatch() throws Exception {
        // Initialize the database
        posologiasRepository.saveAndFlush(posologias);

        int databaseSizeBeforeUpdate = posologiasRepository.findAll().size();

        // Update the posologias using partial update
        Posologias partialUpdatedPosologias = new Posologias();
        partialUpdatedPosologias.setId(posologias.getId());

        partialUpdatedPosologias.posologia(UPDATED_POSOLOGIA);

        restPosologiasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPosologias.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPosologias))
            )
            .andExpect(status().isOk());

        // Validate the Posologias in the database
        List<Posologias> posologiasList = posologiasRepository.findAll();
        assertThat(posologiasList).hasSize(databaseSizeBeforeUpdate);
        Posologias testPosologias = posologiasList.get(posologiasList.size() - 1);
        assertThat(testPosologias.getPosologia()).isEqualTo(UPDATED_POSOLOGIA);
    }

    @Test
    @Transactional
    void patchNonExistingPosologias() throws Exception {
        int databaseSizeBeforeUpdate = posologiasRepository.findAll().size();
        posologias.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPosologiasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, posologias.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(posologias))
            )
            .andExpect(status().isBadRequest());

        // Validate the Posologias in the database
        List<Posologias> posologiasList = posologiasRepository.findAll();
        assertThat(posologiasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPosologias() throws Exception {
        int databaseSizeBeforeUpdate = posologiasRepository.findAll().size();
        posologias.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPosologiasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(posologias))
            )
            .andExpect(status().isBadRequest());

        // Validate the Posologias in the database
        List<Posologias> posologiasList = posologiasRepository.findAll();
        assertThat(posologiasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPosologias() throws Exception {
        int databaseSizeBeforeUpdate = posologiasRepository.findAll().size();
        posologias.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPosologiasMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(posologias))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Posologias in the database
        List<Posologias> posologiasList = posologiasRepository.findAll();
        assertThat(posologiasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePosologias() throws Exception {
        // Initialize the database
        posologiasRepository.saveAndFlush(posologias);

        int databaseSizeBeforeDelete = posologiasRepository.findAll().size();

        // Delete the posologias
        restPosologiasMockMvc
            .perform(delete(ENTITY_API_URL_ID, posologias.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Posologias> posologiasList = posologiasRepository.findAll();
        assertThat(posologiasList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
