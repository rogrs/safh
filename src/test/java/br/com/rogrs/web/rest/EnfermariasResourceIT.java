package br.com.rogrs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.rogrs.IntegrationTest;
import br.com.rogrs.domain.Enfermarias;
import br.com.rogrs.repository.EnfermariasRepository;
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
 * Integration tests for the {@link EnfermariasResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EnfermariasResourceIT {

    private static final String DEFAULT_ENFERMARIA = "AAAAAAAAAA";
    private static final String UPDATED_ENFERMARIA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/enfermarias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EnfermariasRepository enfermariasRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEnfermariasMockMvc;

    private Enfermarias enfermarias;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Enfermarias createEntity(EntityManager em) {
        Enfermarias enfermarias = new Enfermarias().enfermaria(DEFAULT_ENFERMARIA);
        return enfermarias;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Enfermarias createUpdatedEntity(EntityManager em) {
        Enfermarias enfermarias = new Enfermarias().enfermaria(UPDATED_ENFERMARIA);
        return enfermarias;
    }

    @BeforeEach
    public void initTest() {
        enfermarias = createEntity(em);
    }

    @Test
    @Transactional
    void createEnfermarias() throws Exception {
        int databaseSizeBeforeCreate = enfermariasRepository.findAll().size();
        // Create the Enfermarias
        restEnfermariasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enfermarias)))
            .andExpect(status().isCreated());

        // Validate the Enfermarias in the database
        List<Enfermarias> enfermariasList = enfermariasRepository.findAll();
        assertThat(enfermariasList).hasSize(databaseSizeBeforeCreate + 1);
        Enfermarias testEnfermarias = enfermariasList.get(enfermariasList.size() - 1);
        assertThat(testEnfermarias.getEnfermaria()).isEqualTo(DEFAULT_ENFERMARIA);
    }

    @Test
    @Transactional
    void createEnfermariasWithExistingId() throws Exception {
        // Create the Enfermarias with an existing ID
        enfermarias.setId(1L);

        int databaseSizeBeforeCreate = enfermariasRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEnfermariasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enfermarias)))
            .andExpect(status().isBadRequest());

        // Validate the Enfermarias in the database
        List<Enfermarias> enfermariasList = enfermariasRepository.findAll();
        assertThat(enfermariasList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEnfermariaIsRequired() throws Exception {
        int databaseSizeBeforeTest = enfermariasRepository.findAll().size();
        // set the field null
        enfermarias.setEnfermaria(null);

        // Create the Enfermarias, which fails.

        restEnfermariasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enfermarias)))
            .andExpect(status().isBadRequest());

        List<Enfermarias> enfermariasList = enfermariasRepository.findAll();
        assertThat(enfermariasList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEnfermarias() throws Exception {
        // Initialize the database
        enfermariasRepository.saveAndFlush(enfermarias);

        // Get all the enfermariasList
        restEnfermariasMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(enfermarias.getId().intValue())))
            .andExpect(jsonPath("$.[*].enfermaria").value(hasItem(DEFAULT_ENFERMARIA)));
    }

    @Test
    @Transactional
    void getEnfermarias() throws Exception {
        // Initialize the database
        enfermariasRepository.saveAndFlush(enfermarias);

        // Get the enfermarias
        restEnfermariasMockMvc
            .perform(get(ENTITY_API_URL_ID, enfermarias.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(enfermarias.getId().intValue()))
            .andExpect(jsonPath("$.enfermaria").value(DEFAULT_ENFERMARIA));
    }

    @Test
    @Transactional
    void getNonExistingEnfermarias() throws Exception {
        // Get the enfermarias
        restEnfermariasMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEnfermarias() throws Exception {
        // Initialize the database
        enfermariasRepository.saveAndFlush(enfermarias);

        int databaseSizeBeforeUpdate = enfermariasRepository.findAll().size();

        // Update the enfermarias
        Enfermarias updatedEnfermarias = enfermariasRepository.findById(enfermarias.getId()).get();
        // Disconnect from session so that the updates on updatedEnfermarias are not directly saved in db
        em.detach(updatedEnfermarias);
        updatedEnfermarias.enfermaria(UPDATED_ENFERMARIA);

        restEnfermariasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEnfermarias.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEnfermarias))
            )
            .andExpect(status().isOk());

        // Validate the Enfermarias in the database
        List<Enfermarias> enfermariasList = enfermariasRepository.findAll();
        assertThat(enfermariasList).hasSize(databaseSizeBeforeUpdate);
        Enfermarias testEnfermarias = enfermariasList.get(enfermariasList.size() - 1);
        assertThat(testEnfermarias.getEnfermaria()).isEqualTo(UPDATED_ENFERMARIA);
    }

    @Test
    @Transactional
    void putNonExistingEnfermarias() throws Exception {
        int databaseSizeBeforeUpdate = enfermariasRepository.findAll().size();
        enfermarias.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnfermariasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, enfermarias.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(enfermarias))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enfermarias in the database
        List<Enfermarias> enfermariasList = enfermariasRepository.findAll();
        assertThat(enfermariasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEnfermarias() throws Exception {
        int databaseSizeBeforeUpdate = enfermariasRepository.findAll().size();
        enfermarias.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnfermariasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(enfermarias))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enfermarias in the database
        List<Enfermarias> enfermariasList = enfermariasRepository.findAll();
        assertThat(enfermariasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEnfermarias() throws Exception {
        int databaseSizeBeforeUpdate = enfermariasRepository.findAll().size();
        enfermarias.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnfermariasMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enfermarias)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Enfermarias in the database
        List<Enfermarias> enfermariasList = enfermariasRepository.findAll();
        assertThat(enfermariasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEnfermariasWithPatch() throws Exception {
        // Initialize the database
        enfermariasRepository.saveAndFlush(enfermarias);

        int databaseSizeBeforeUpdate = enfermariasRepository.findAll().size();

        // Update the enfermarias using partial update
        Enfermarias partialUpdatedEnfermarias = new Enfermarias();
        partialUpdatedEnfermarias.setId(enfermarias.getId());

        restEnfermariasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnfermarias.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEnfermarias))
            )
            .andExpect(status().isOk());

        // Validate the Enfermarias in the database
        List<Enfermarias> enfermariasList = enfermariasRepository.findAll();
        assertThat(enfermariasList).hasSize(databaseSizeBeforeUpdate);
        Enfermarias testEnfermarias = enfermariasList.get(enfermariasList.size() - 1);
        assertThat(testEnfermarias.getEnfermaria()).isEqualTo(DEFAULT_ENFERMARIA);
    }

    @Test
    @Transactional
    void fullUpdateEnfermariasWithPatch() throws Exception {
        // Initialize the database
        enfermariasRepository.saveAndFlush(enfermarias);

        int databaseSizeBeforeUpdate = enfermariasRepository.findAll().size();

        // Update the enfermarias using partial update
        Enfermarias partialUpdatedEnfermarias = new Enfermarias();
        partialUpdatedEnfermarias.setId(enfermarias.getId());

        partialUpdatedEnfermarias.enfermaria(UPDATED_ENFERMARIA);

        restEnfermariasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnfermarias.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEnfermarias))
            )
            .andExpect(status().isOk());

        // Validate the Enfermarias in the database
        List<Enfermarias> enfermariasList = enfermariasRepository.findAll();
        assertThat(enfermariasList).hasSize(databaseSizeBeforeUpdate);
        Enfermarias testEnfermarias = enfermariasList.get(enfermariasList.size() - 1);
        assertThat(testEnfermarias.getEnfermaria()).isEqualTo(UPDATED_ENFERMARIA);
    }

    @Test
    @Transactional
    void patchNonExistingEnfermarias() throws Exception {
        int databaseSizeBeforeUpdate = enfermariasRepository.findAll().size();
        enfermarias.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnfermariasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, enfermarias.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(enfermarias))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enfermarias in the database
        List<Enfermarias> enfermariasList = enfermariasRepository.findAll();
        assertThat(enfermariasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEnfermarias() throws Exception {
        int databaseSizeBeforeUpdate = enfermariasRepository.findAll().size();
        enfermarias.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnfermariasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(enfermarias))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enfermarias in the database
        List<Enfermarias> enfermariasList = enfermariasRepository.findAll();
        assertThat(enfermariasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEnfermarias() throws Exception {
        int databaseSizeBeforeUpdate = enfermariasRepository.findAll().size();
        enfermarias.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnfermariasMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(enfermarias))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Enfermarias in the database
        List<Enfermarias> enfermariasList = enfermariasRepository.findAll();
        assertThat(enfermariasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEnfermarias() throws Exception {
        // Initialize the database
        enfermariasRepository.saveAndFlush(enfermarias);

        int databaseSizeBeforeDelete = enfermariasRepository.findAll().size();

        // Delete the enfermarias
        restEnfermariasMockMvc
            .perform(delete(ENTITY_API_URL_ID, enfermarias.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Enfermarias> enfermariasList = enfermariasRepository.findAll();
        assertThat(enfermariasList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
