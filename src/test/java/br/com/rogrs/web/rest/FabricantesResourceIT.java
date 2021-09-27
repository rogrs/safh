package br.com.rogrs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.rogrs.IntegrationTest;
import br.com.rogrs.domain.Fabricantes;
import br.com.rogrs.repository.FabricantesRepository;
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
 * Integration tests for the {@link FabricantesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FabricantesResourceIT {

    private static final String DEFAULT_FABRICANTE = "AAAAAAAAAA";
    private static final String UPDATED_FABRICANTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/fabricantes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FabricantesRepository fabricantesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFabricantesMockMvc;

    private Fabricantes fabricantes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fabricantes createEntity(EntityManager em) {
        Fabricantes fabricantes = new Fabricantes().fabricante(DEFAULT_FABRICANTE);
        return fabricantes;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fabricantes createUpdatedEntity(EntityManager em) {
        Fabricantes fabricantes = new Fabricantes().fabricante(UPDATED_FABRICANTE);
        return fabricantes;
    }

    @BeforeEach
    public void initTest() {
        fabricantes = createEntity(em);
    }

    @Test
    @Transactional
    void createFabricantes() throws Exception {
        int databaseSizeBeforeCreate = fabricantesRepository.findAll().size();
        // Create the Fabricantes
        restFabricantesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fabricantes)))
            .andExpect(status().isCreated());

        // Validate the Fabricantes in the database
        List<Fabricantes> fabricantesList = fabricantesRepository.findAll();
        assertThat(fabricantesList).hasSize(databaseSizeBeforeCreate + 1);
        Fabricantes testFabricantes = fabricantesList.get(fabricantesList.size() - 1);
        assertThat(testFabricantes.getFabricante()).isEqualTo(DEFAULT_FABRICANTE);
    }

    @Test
    @Transactional
    void createFabricantesWithExistingId() throws Exception {
        // Create the Fabricantes with an existing ID
        fabricantes.setId(1L);

        int databaseSizeBeforeCreate = fabricantesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFabricantesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fabricantes)))
            .andExpect(status().isBadRequest());

        // Validate the Fabricantes in the database
        List<Fabricantes> fabricantesList = fabricantesRepository.findAll();
        assertThat(fabricantesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFabricanteIsRequired() throws Exception {
        int databaseSizeBeforeTest = fabricantesRepository.findAll().size();
        // set the field null
        fabricantes.setFabricante(null);

        // Create the Fabricantes, which fails.

        restFabricantesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fabricantes)))
            .andExpect(status().isBadRequest());

        List<Fabricantes> fabricantesList = fabricantesRepository.findAll();
        assertThat(fabricantesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFabricantes() throws Exception {
        // Initialize the database
        fabricantesRepository.saveAndFlush(fabricantes);

        // Get all the fabricantesList
        restFabricantesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fabricantes.getId().intValue())))
            .andExpect(jsonPath("$.[*].fabricante").value(hasItem(DEFAULT_FABRICANTE)));
    }

    @Test
    @Transactional
    void getFabricantes() throws Exception {
        // Initialize the database
        fabricantesRepository.saveAndFlush(fabricantes);

        // Get the fabricantes
        restFabricantesMockMvc
            .perform(get(ENTITY_API_URL_ID, fabricantes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fabricantes.getId().intValue()))
            .andExpect(jsonPath("$.fabricante").value(DEFAULT_FABRICANTE));
    }

    @Test
    @Transactional
    void getNonExistingFabricantes() throws Exception {
        // Get the fabricantes
        restFabricantesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFabricantes() throws Exception {
        // Initialize the database
        fabricantesRepository.saveAndFlush(fabricantes);

        int databaseSizeBeforeUpdate = fabricantesRepository.findAll().size();

        // Update the fabricantes
        Fabricantes updatedFabricantes = fabricantesRepository.findById(fabricantes.getId()).get();
        // Disconnect from session so that the updates on updatedFabricantes are not directly saved in db
        em.detach(updatedFabricantes);
        updatedFabricantes.fabricante(UPDATED_FABRICANTE);

        restFabricantesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFabricantes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFabricantes))
            )
            .andExpect(status().isOk());

        // Validate the Fabricantes in the database
        List<Fabricantes> fabricantesList = fabricantesRepository.findAll();
        assertThat(fabricantesList).hasSize(databaseSizeBeforeUpdate);
        Fabricantes testFabricantes = fabricantesList.get(fabricantesList.size() - 1);
        assertThat(testFabricantes.getFabricante()).isEqualTo(UPDATED_FABRICANTE);
    }

    @Test
    @Transactional
    void putNonExistingFabricantes() throws Exception {
        int databaseSizeBeforeUpdate = fabricantesRepository.findAll().size();
        fabricantes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFabricantesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fabricantes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fabricantes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fabricantes in the database
        List<Fabricantes> fabricantesList = fabricantesRepository.findAll();
        assertThat(fabricantesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFabricantes() throws Exception {
        int databaseSizeBeforeUpdate = fabricantesRepository.findAll().size();
        fabricantes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFabricantesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fabricantes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fabricantes in the database
        List<Fabricantes> fabricantesList = fabricantesRepository.findAll();
        assertThat(fabricantesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFabricantes() throws Exception {
        int databaseSizeBeforeUpdate = fabricantesRepository.findAll().size();
        fabricantes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFabricantesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fabricantes)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fabricantes in the database
        List<Fabricantes> fabricantesList = fabricantesRepository.findAll();
        assertThat(fabricantesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFabricantesWithPatch() throws Exception {
        // Initialize the database
        fabricantesRepository.saveAndFlush(fabricantes);

        int databaseSizeBeforeUpdate = fabricantesRepository.findAll().size();

        // Update the fabricantes using partial update
        Fabricantes partialUpdatedFabricantes = new Fabricantes();
        partialUpdatedFabricantes.setId(fabricantes.getId());

        restFabricantesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFabricantes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFabricantes))
            )
            .andExpect(status().isOk());

        // Validate the Fabricantes in the database
        List<Fabricantes> fabricantesList = fabricantesRepository.findAll();
        assertThat(fabricantesList).hasSize(databaseSizeBeforeUpdate);
        Fabricantes testFabricantes = fabricantesList.get(fabricantesList.size() - 1);
        assertThat(testFabricantes.getFabricante()).isEqualTo(DEFAULT_FABRICANTE);
    }

    @Test
    @Transactional
    void fullUpdateFabricantesWithPatch() throws Exception {
        // Initialize the database
        fabricantesRepository.saveAndFlush(fabricantes);

        int databaseSizeBeforeUpdate = fabricantesRepository.findAll().size();

        // Update the fabricantes using partial update
        Fabricantes partialUpdatedFabricantes = new Fabricantes();
        partialUpdatedFabricantes.setId(fabricantes.getId());

        partialUpdatedFabricantes.fabricante(UPDATED_FABRICANTE);

        restFabricantesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFabricantes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFabricantes))
            )
            .andExpect(status().isOk());

        // Validate the Fabricantes in the database
        List<Fabricantes> fabricantesList = fabricantesRepository.findAll();
        assertThat(fabricantesList).hasSize(databaseSizeBeforeUpdate);
        Fabricantes testFabricantes = fabricantesList.get(fabricantesList.size() - 1);
        assertThat(testFabricantes.getFabricante()).isEqualTo(UPDATED_FABRICANTE);
    }

    @Test
    @Transactional
    void patchNonExistingFabricantes() throws Exception {
        int databaseSizeBeforeUpdate = fabricantesRepository.findAll().size();
        fabricantes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFabricantesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fabricantes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fabricantes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fabricantes in the database
        List<Fabricantes> fabricantesList = fabricantesRepository.findAll();
        assertThat(fabricantesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFabricantes() throws Exception {
        int databaseSizeBeforeUpdate = fabricantesRepository.findAll().size();
        fabricantes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFabricantesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fabricantes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fabricantes in the database
        List<Fabricantes> fabricantesList = fabricantesRepository.findAll();
        assertThat(fabricantesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFabricantes() throws Exception {
        int databaseSizeBeforeUpdate = fabricantesRepository.findAll().size();
        fabricantes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFabricantesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(fabricantes))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fabricantes in the database
        List<Fabricantes> fabricantesList = fabricantesRepository.findAll();
        assertThat(fabricantesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFabricantes() throws Exception {
        // Initialize the database
        fabricantesRepository.saveAndFlush(fabricantes);

        int databaseSizeBeforeDelete = fabricantesRepository.findAll().size();

        // Delete the fabricantes
        restFabricantesMockMvc
            .perform(delete(ENTITY_API_URL_ID, fabricantes.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Fabricantes> fabricantesList = fabricantesRepository.findAll();
        assertThat(fabricantesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
