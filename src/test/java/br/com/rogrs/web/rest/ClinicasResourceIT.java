package br.com.rogrs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.rogrs.IntegrationTest;
import br.com.rogrs.domain.Clinicas;
import br.com.rogrs.repository.ClinicasRepository;
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
 * Integration tests for the {@link ClinicasResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClinicasResourceIT {

    private static final String DEFAULT_CLINICA = "AAAAAAAAAA";
    private static final String UPDATED_CLINICA = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/clinicas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ClinicasRepository clinicasRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClinicasMockMvc;

    private Clinicas clinicas;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Clinicas createEntity(EntityManager em) {
        Clinicas clinicas = new Clinicas().clinica(DEFAULT_CLINICA).descricao(DEFAULT_DESCRICAO);
        return clinicas;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Clinicas createUpdatedEntity(EntityManager em) {
        Clinicas clinicas = new Clinicas().clinica(UPDATED_CLINICA).descricao(UPDATED_DESCRICAO);
        return clinicas;
    }

    @BeforeEach
    public void initTest() {
        clinicas = createEntity(em);
    }

    @Test
    @Transactional
    void createClinicas() throws Exception {
        int databaseSizeBeforeCreate = clinicasRepository.findAll().size();
        // Create the Clinicas
        restClinicasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clinicas)))
            .andExpect(status().isCreated());

        // Validate the Clinicas in the database
        List<Clinicas> clinicasList = clinicasRepository.findAll();
        assertThat(clinicasList).hasSize(databaseSizeBeforeCreate + 1);
        Clinicas testClinicas = clinicasList.get(clinicasList.size() - 1);
        assertThat(testClinicas.getClinica()).isEqualTo(DEFAULT_CLINICA);
        assertThat(testClinicas.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    void createClinicasWithExistingId() throws Exception {
        // Create the Clinicas with an existing ID
        clinicas.setId(1L);

        int databaseSizeBeforeCreate = clinicasRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClinicasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clinicas)))
            .andExpect(status().isBadRequest());

        // Validate the Clinicas in the database
        List<Clinicas> clinicasList = clinicasRepository.findAll();
        assertThat(clinicasList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkClinicaIsRequired() throws Exception {
        int databaseSizeBeforeTest = clinicasRepository.findAll().size();
        // set the field null
        clinicas.setClinica(null);

        // Create the Clinicas, which fails.

        restClinicasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clinicas)))
            .andExpect(status().isBadRequest());

        List<Clinicas> clinicasList = clinicasRepository.findAll();
        assertThat(clinicasList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllClinicas() throws Exception {
        // Initialize the database
        clinicasRepository.saveAndFlush(clinicas);

        // Get all the clinicasList
        restClinicasMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clinicas.getId().intValue())))
            .andExpect(jsonPath("$.[*].clinica").value(hasItem(DEFAULT_CLINICA)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));
    }

    @Test
    @Transactional
    void getClinicas() throws Exception {
        // Initialize the database
        clinicasRepository.saveAndFlush(clinicas);

        // Get the clinicas
        restClinicasMockMvc
            .perform(get(ENTITY_API_URL_ID, clinicas.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(clinicas.getId().intValue()))
            .andExpect(jsonPath("$.clinica").value(DEFAULT_CLINICA))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO));
    }

    @Test
    @Transactional
    void getNonExistingClinicas() throws Exception {
        // Get the clinicas
        restClinicasMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewClinicas() throws Exception {
        // Initialize the database
        clinicasRepository.saveAndFlush(clinicas);

        int databaseSizeBeforeUpdate = clinicasRepository.findAll().size();

        // Update the clinicas
        Clinicas updatedClinicas = clinicasRepository.findById(clinicas.getId()).get();
        // Disconnect from session so that the updates on updatedClinicas are not directly saved in db
        em.detach(updatedClinicas);
        updatedClinicas.clinica(UPDATED_CLINICA).descricao(UPDATED_DESCRICAO);

        restClinicasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedClinicas.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedClinicas))
            )
            .andExpect(status().isOk());

        // Validate the Clinicas in the database
        List<Clinicas> clinicasList = clinicasRepository.findAll();
        assertThat(clinicasList).hasSize(databaseSizeBeforeUpdate);
        Clinicas testClinicas = clinicasList.get(clinicasList.size() - 1);
        assertThat(testClinicas.getClinica()).isEqualTo(UPDATED_CLINICA);
        assertThat(testClinicas.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void putNonExistingClinicas() throws Exception {
        int databaseSizeBeforeUpdate = clinicasRepository.findAll().size();
        clinicas.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClinicasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clinicas.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clinicas))
            )
            .andExpect(status().isBadRequest());

        // Validate the Clinicas in the database
        List<Clinicas> clinicasList = clinicasRepository.findAll();
        assertThat(clinicasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClinicas() throws Exception {
        int databaseSizeBeforeUpdate = clinicasRepository.findAll().size();
        clinicas.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClinicasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clinicas))
            )
            .andExpect(status().isBadRequest());

        // Validate the Clinicas in the database
        List<Clinicas> clinicasList = clinicasRepository.findAll();
        assertThat(clinicasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClinicas() throws Exception {
        int databaseSizeBeforeUpdate = clinicasRepository.findAll().size();
        clinicas.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClinicasMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clinicas)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Clinicas in the database
        List<Clinicas> clinicasList = clinicasRepository.findAll();
        assertThat(clinicasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClinicasWithPatch() throws Exception {
        // Initialize the database
        clinicasRepository.saveAndFlush(clinicas);

        int databaseSizeBeforeUpdate = clinicasRepository.findAll().size();

        // Update the clinicas using partial update
        Clinicas partialUpdatedClinicas = new Clinicas();
        partialUpdatedClinicas.setId(clinicas.getId());

        partialUpdatedClinicas.clinica(UPDATED_CLINICA);

        restClinicasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClinicas.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClinicas))
            )
            .andExpect(status().isOk());

        // Validate the Clinicas in the database
        List<Clinicas> clinicasList = clinicasRepository.findAll();
        assertThat(clinicasList).hasSize(databaseSizeBeforeUpdate);
        Clinicas testClinicas = clinicasList.get(clinicasList.size() - 1);
        assertThat(testClinicas.getClinica()).isEqualTo(UPDATED_CLINICA);
        assertThat(testClinicas.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    void fullUpdateClinicasWithPatch() throws Exception {
        // Initialize the database
        clinicasRepository.saveAndFlush(clinicas);

        int databaseSizeBeforeUpdate = clinicasRepository.findAll().size();

        // Update the clinicas using partial update
        Clinicas partialUpdatedClinicas = new Clinicas();
        partialUpdatedClinicas.setId(clinicas.getId());

        partialUpdatedClinicas.clinica(UPDATED_CLINICA).descricao(UPDATED_DESCRICAO);

        restClinicasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClinicas.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClinicas))
            )
            .andExpect(status().isOk());

        // Validate the Clinicas in the database
        List<Clinicas> clinicasList = clinicasRepository.findAll();
        assertThat(clinicasList).hasSize(databaseSizeBeforeUpdate);
        Clinicas testClinicas = clinicasList.get(clinicasList.size() - 1);
        assertThat(testClinicas.getClinica()).isEqualTo(UPDATED_CLINICA);
        assertThat(testClinicas.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void patchNonExistingClinicas() throws Exception {
        int databaseSizeBeforeUpdate = clinicasRepository.findAll().size();
        clinicas.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClinicasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, clinicas.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(clinicas))
            )
            .andExpect(status().isBadRequest());

        // Validate the Clinicas in the database
        List<Clinicas> clinicasList = clinicasRepository.findAll();
        assertThat(clinicasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClinicas() throws Exception {
        int databaseSizeBeforeUpdate = clinicasRepository.findAll().size();
        clinicas.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClinicasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(clinicas))
            )
            .andExpect(status().isBadRequest());

        // Validate the Clinicas in the database
        List<Clinicas> clinicasList = clinicasRepository.findAll();
        assertThat(clinicasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClinicas() throws Exception {
        int databaseSizeBeforeUpdate = clinicasRepository.findAll().size();
        clinicas.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClinicasMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(clinicas)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Clinicas in the database
        List<Clinicas> clinicasList = clinicasRepository.findAll();
        assertThat(clinicasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClinicas() throws Exception {
        // Initialize the database
        clinicasRepository.saveAndFlush(clinicas);

        int databaseSizeBeforeDelete = clinicasRepository.findAll().size();

        // Delete the clinicas
        restClinicasMockMvc
            .perform(delete(ENTITY_API_URL_ID, clinicas.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Clinicas> clinicasList = clinicasRepository.findAll();
        assertThat(clinicasList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
