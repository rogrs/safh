package br.com.rogrs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.rogrs.IntegrationTest;
import br.com.rogrs.domain.Dietas;
import br.com.rogrs.repository.DietasRepository;
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
 * Integration tests for the {@link DietasResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DietasResourceIT {

    private static final String DEFAULT_DIETA = "AAAAAAAAAA";
    private static final String UPDATED_DIETA = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/dietas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DietasRepository dietasRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDietasMockMvc;

    private Dietas dietas;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dietas createEntity(EntityManager em) {
        Dietas dietas = new Dietas().dieta(DEFAULT_DIETA).descricao(DEFAULT_DESCRICAO);
        return dietas;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dietas createUpdatedEntity(EntityManager em) {
        Dietas dietas = new Dietas().dieta(UPDATED_DIETA).descricao(UPDATED_DESCRICAO);
        return dietas;
    }

    @BeforeEach
    public void initTest() {
        dietas = createEntity(em);
    }

    @Test
    @Transactional
    void createDietas() throws Exception {
        int databaseSizeBeforeCreate = dietasRepository.findAll().size();
        // Create the Dietas
        restDietasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dietas)))
            .andExpect(status().isCreated());

        // Validate the Dietas in the database
        List<Dietas> dietasList = dietasRepository.findAll();
        assertThat(dietasList).hasSize(databaseSizeBeforeCreate + 1);
        Dietas testDietas = dietasList.get(dietasList.size() - 1);
        assertThat(testDietas.getDieta()).isEqualTo(DEFAULT_DIETA);
        assertThat(testDietas.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    void createDietasWithExistingId() throws Exception {
        // Create the Dietas with an existing ID
        dietas.setId(1L);

        int databaseSizeBeforeCreate = dietasRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDietasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dietas)))
            .andExpect(status().isBadRequest());

        // Validate the Dietas in the database
        List<Dietas> dietasList = dietasRepository.findAll();
        assertThat(dietasList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDietaIsRequired() throws Exception {
        int databaseSizeBeforeTest = dietasRepository.findAll().size();
        // set the field null
        dietas.setDieta(null);

        // Create the Dietas, which fails.

        restDietasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dietas)))
            .andExpect(status().isBadRequest());

        List<Dietas> dietasList = dietasRepository.findAll();
        assertThat(dietasList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDietas() throws Exception {
        // Initialize the database
        dietasRepository.saveAndFlush(dietas);

        // Get all the dietasList
        restDietasMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dietas.getId().intValue())))
            .andExpect(jsonPath("$.[*].dieta").value(hasItem(DEFAULT_DIETA)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));
    }

    @Test
    @Transactional
    void getDietas() throws Exception {
        // Initialize the database
        dietasRepository.saveAndFlush(dietas);

        // Get the dietas
        restDietasMockMvc
            .perform(get(ENTITY_API_URL_ID, dietas.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dietas.getId().intValue()))
            .andExpect(jsonPath("$.dieta").value(DEFAULT_DIETA))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO));
    }

    @Test
    @Transactional
    void getNonExistingDietas() throws Exception {
        // Get the dietas
        restDietasMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDietas() throws Exception {
        // Initialize the database
        dietasRepository.saveAndFlush(dietas);

        int databaseSizeBeforeUpdate = dietasRepository.findAll().size();

        // Update the dietas
        Dietas updatedDietas = dietasRepository.findById(dietas.getId()).get();
        // Disconnect from session so that the updates on updatedDietas are not directly saved in db
        em.detach(updatedDietas);
        updatedDietas.dieta(UPDATED_DIETA).descricao(UPDATED_DESCRICAO);

        restDietasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDietas.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDietas))
            )
            .andExpect(status().isOk());

        // Validate the Dietas in the database
        List<Dietas> dietasList = dietasRepository.findAll();
        assertThat(dietasList).hasSize(databaseSizeBeforeUpdate);
        Dietas testDietas = dietasList.get(dietasList.size() - 1);
        assertThat(testDietas.getDieta()).isEqualTo(UPDATED_DIETA);
        assertThat(testDietas.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void putNonExistingDietas() throws Exception {
        int databaseSizeBeforeUpdate = dietasRepository.findAll().size();
        dietas.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDietasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dietas.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dietas))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dietas in the database
        List<Dietas> dietasList = dietasRepository.findAll();
        assertThat(dietasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDietas() throws Exception {
        int databaseSizeBeforeUpdate = dietasRepository.findAll().size();
        dietas.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDietasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dietas))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dietas in the database
        List<Dietas> dietasList = dietasRepository.findAll();
        assertThat(dietasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDietas() throws Exception {
        int databaseSizeBeforeUpdate = dietasRepository.findAll().size();
        dietas.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDietasMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dietas)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dietas in the database
        List<Dietas> dietasList = dietasRepository.findAll();
        assertThat(dietasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDietasWithPatch() throws Exception {
        // Initialize the database
        dietasRepository.saveAndFlush(dietas);

        int databaseSizeBeforeUpdate = dietasRepository.findAll().size();

        // Update the dietas using partial update
        Dietas partialUpdatedDietas = new Dietas();
        partialUpdatedDietas.setId(dietas.getId());

        partialUpdatedDietas.dieta(UPDATED_DIETA).descricao(UPDATED_DESCRICAO);

        restDietasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDietas.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDietas))
            )
            .andExpect(status().isOk());

        // Validate the Dietas in the database
        List<Dietas> dietasList = dietasRepository.findAll();
        assertThat(dietasList).hasSize(databaseSizeBeforeUpdate);
        Dietas testDietas = dietasList.get(dietasList.size() - 1);
        assertThat(testDietas.getDieta()).isEqualTo(UPDATED_DIETA);
        assertThat(testDietas.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void fullUpdateDietasWithPatch() throws Exception {
        // Initialize the database
        dietasRepository.saveAndFlush(dietas);

        int databaseSizeBeforeUpdate = dietasRepository.findAll().size();

        // Update the dietas using partial update
        Dietas partialUpdatedDietas = new Dietas();
        partialUpdatedDietas.setId(dietas.getId());

        partialUpdatedDietas.dieta(UPDATED_DIETA).descricao(UPDATED_DESCRICAO);

        restDietasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDietas.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDietas))
            )
            .andExpect(status().isOk());

        // Validate the Dietas in the database
        List<Dietas> dietasList = dietasRepository.findAll();
        assertThat(dietasList).hasSize(databaseSizeBeforeUpdate);
        Dietas testDietas = dietasList.get(dietasList.size() - 1);
        assertThat(testDietas.getDieta()).isEqualTo(UPDATED_DIETA);
        assertThat(testDietas.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void patchNonExistingDietas() throws Exception {
        int databaseSizeBeforeUpdate = dietasRepository.findAll().size();
        dietas.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDietasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dietas.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dietas))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dietas in the database
        List<Dietas> dietasList = dietasRepository.findAll();
        assertThat(dietasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDietas() throws Exception {
        int databaseSizeBeforeUpdate = dietasRepository.findAll().size();
        dietas.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDietasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dietas))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dietas in the database
        List<Dietas> dietasList = dietasRepository.findAll();
        assertThat(dietasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDietas() throws Exception {
        int databaseSizeBeforeUpdate = dietasRepository.findAll().size();
        dietas.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDietasMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(dietas)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dietas in the database
        List<Dietas> dietasList = dietasRepository.findAll();
        assertThat(dietasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDietas() throws Exception {
        // Initialize the database
        dietasRepository.saveAndFlush(dietas);

        int databaseSizeBeforeDelete = dietasRepository.findAll().size();

        // Delete the dietas
        restDietasMockMvc
            .perform(delete(ENTITY_API_URL_ID, dietas.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dietas> dietasList = dietasRepository.findAll();
        assertThat(dietasList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
