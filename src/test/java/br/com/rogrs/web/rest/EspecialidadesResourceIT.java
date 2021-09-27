package br.com.rogrs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.rogrs.IntegrationTest;
import br.com.rogrs.domain.Especialidades;
import br.com.rogrs.repository.EspecialidadesRepository;
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
 * Integration tests for the {@link EspecialidadesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EspecialidadesResourceIT {

    private static final String DEFAULT_ESPECIALIDADE = "AAAAAAAAAA";
    private static final String UPDATED_ESPECIALIDADE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/especialidades";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EspecialidadesRepository especialidadesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEspecialidadesMockMvc;

    private Especialidades especialidades;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Especialidades createEntity(EntityManager em) {
        Especialidades especialidades = new Especialidades().especialidade(DEFAULT_ESPECIALIDADE);
        return especialidades;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Especialidades createUpdatedEntity(EntityManager em) {
        Especialidades especialidades = new Especialidades().especialidade(UPDATED_ESPECIALIDADE);
        return especialidades;
    }

    @BeforeEach
    public void initTest() {
        especialidades = createEntity(em);
    }

    @Test
    @Transactional
    void createEspecialidades() throws Exception {
        int databaseSizeBeforeCreate = especialidadesRepository.findAll().size();
        // Create the Especialidades
        restEspecialidadesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(especialidades))
            )
            .andExpect(status().isCreated());

        // Validate the Especialidades in the database
        List<Especialidades> especialidadesList = especialidadesRepository.findAll();
        assertThat(especialidadesList).hasSize(databaseSizeBeforeCreate + 1);
        Especialidades testEspecialidades = especialidadesList.get(especialidadesList.size() - 1);
        assertThat(testEspecialidades.getEspecialidade()).isEqualTo(DEFAULT_ESPECIALIDADE);
    }

    @Test
    @Transactional
    void createEspecialidadesWithExistingId() throws Exception {
        // Create the Especialidades with an existing ID
        especialidades.setId(1L);

        int databaseSizeBeforeCreate = especialidadesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEspecialidadesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(especialidades))
            )
            .andExpect(status().isBadRequest());

        // Validate the Especialidades in the database
        List<Especialidades> especialidadesList = especialidadesRepository.findAll();
        assertThat(especialidadesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEspecialidadeIsRequired() throws Exception {
        int databaseSizeBeforeTest = especialidadesRepository.findAll().size();
        // set the field null
        especialidades.setEspecialidade(null);

        // Create the Especialidades, which fails.

        restEspecialidadesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(especialidades))
            )
            .andExpect(status().isBadRequest());

        List<Especialidades> especialidadesList = especialidadesRepository.findAll();
        assertThat(especialidadesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEspecialidades() throws Exception {
        // Initialize the database
        especialidadesRepository.saveAndFlush(especialidades);

        // Get all the especialidadesList
        restEspecialidadesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(especialidades.getId().intValue())))
            .andExpect(jsonPath("$.[*].especialidade").value(hasItem(DEFAULT_ESPECIALIDADE)));
    }

    @Test
    @Transactional
    void getEspecialidades() throws Exception {
        // Initialize the database
        especialidadesRepository.saveAndFlush(especialidades);

        // Get the especialidades
        restEspecialidadesMockMvc
            .perform(get(ENTITY_API_URL_ID, especialidades.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(especialidades.getId().intValue()))
            .andExpect(jsonPath("$.especialidade").value(DEFAULT_ESPECIALIDADE));
    }

    @Test
    @Transactional
    void getNonExistingEspecialidades() throws Exception {
        // Get the especialidades
        restEspecialidadesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEspecialidades() throws Exception {
        // Initialize the database
        especialidadesRepository.saveAndFlush(especialidades);

        int databaseSizeBeforeUpdate = especialidadesRepository.findAll().size();

        // Update the especialidades
        Especialidades updatedEspecialidades = especialidadesRepository.findById(especialidades.getId()).get();
        // Disconnect from session so that the updates on updatedEspecialidades are not directly saved in db
        em.detach(updatedEspecialidades);
        updatedEspecialidades.especialidade(UPDATED_ESPECIALIDADE);

        restEspecialidadesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEspecialidades.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEspecialidades))
            )
            .andExpect(status().isOk());

        // Validate the Especialidades in the database
        List<Especialidades> especialidadesList = especialidadesRepository.findAll();
        assertThat(especialidadesList).hasSize(databaseSizeBeforeUpdate);
        Especialidades testEspecialidades = especialidadesList.get(especialidadesList.size() - 1);
        assertThat(testEspecialidades.getEspecialidade()).isEqualTo(UPDATED_ESPECIALIDADE);
    }

    @Test
    @Transactional
    void putNonExistingEspecialidades() throws Exception {
        int databaseSizeBeforeUpdate = especialidadesRepository.findAll().size();
        especialidades.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEspecialidadesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, especialidades.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(especialidades))
            )
            .andExpect(status().isBadRequest());

        // Validate the Especialidades in the database
        List<Especialidades> especialidadesList = especialidadesRepository.findAll();
        assertThat(especialidadesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEspecialidades() throws Exception {
        int databaseSizeBeforeUpdate = especialidadesRepository.findAll().size();
        especialidades.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEspecialidadesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(especialidades))
            )
            .andExpect(status().isBadRequest());

        // Validate the Especialidades in the database
        List<Especialidades> especialidadesList = especialidadesRepository.findAll();
        assertThat(especialidadesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEspecialidades() throws Exception {
        int databaseSizeBeforeUpdate = especialidadesRepository.findAll().size();
        especialidades.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEspecialidadesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(especialidades)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Especialidades in the database
        List<Especialidades> especialidadesList = especialidadesRepository.findAll();
        assertThat(especialidadesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEspecialidadesWithPatch() throws Exception {
        // Initialize the database
        especialidadesRepository.saveAndFlush(especialidades);

        int databaseSizeBeforeUpdate = especialidadesRepository.findAll().size();

        // Update the especialidades using partial update
        Especialidades partialUpdatedEspecialidades = new Especialidades();
        partialUpdatedEspecialidades.setId(especialidades.getId());

        restEspecialidadesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEspecialidades.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEspecialidades))
            )
            .andExpect(status().isOk());

        // Validate the Especialidades in the database
        List<Especialidades> especialidadesList = especialidadesRepository.findAll();
        assertThat(especialidadesList).hasSize(databaseSizeBeforeUpdate);
        Especialidades testEspecialidades = especialidadesList.get(especialidadesList.size() - 1);
        assertThat(testEspecialidades.getEspecialidade()).isEqualTo(DEFAULT_ESPECIALIDADE);
    }

    @Test
    @Transactional
    void fullUpdateEspecialidadesWithPatch() throws Exception {
        // Initialize the database
        especialidadesRepository.saveAndFlush(especialidades);

        int databaseSizeBeforeUpdate = especialidadesRepository.findAll().size();

        // Update the especialidades using partial update
        Especialidades partialUpdatedEspecialidades = new Especialidades();
        partialUpdatedEspecialidades.setId(especialidades.getId());

        partialUpdatedEspecialidades.especialidade(UPDATED_ESPECIALIDADE);

        restEspecialidadesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEspecialidades.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEspecialidades))
            )
            .andExpect(status().isOk());

        // Validate the Especialidades in the database
        List<Especialidades> especialidadesList = especialidadesRepository.findAll();
        assertThat(especialidadesList).hasSize(databaseSizeBeforeUpdate);
        Especialidades testEspecialidades = especialidadesList.get(especialidadesList.size() - 1);
        assertThat(testEspecialidades.getEspecialidade()).isEqualTo(UPDATED_ESPECIALIDADE);
    }

    @Test
    @Transactional
    void patchNonExistingEspecialidades() throws Exception {
        int databaseSizeBeforeUpdate = especialidadesRepository.findAll().size();
        especialidades.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEspecialidadesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, especialidades.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(especialidades))
            )
            .andExpect(status().isBadRequest());

        // Validate the Especialidades in the database
        List<Especialidades> especialidadesList = especialidadesRepository.findAll();
        assertThat(especialidadesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEspecialidades() throws Exception {
        int databaseSizeBeforeUpdate = especialidadesRepository.findAll().size();
        especialidades.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEspecialidadesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(especialidades))
            )
            .andExpect(status().isBadRequest());

        // Validate the Especialidades in the database
        List<Especialidades> especialidadesList = especialidadesRepository.findAll();
        assertThat(especialidadesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEspecialidades() throws Exception {
        int databaseSizeBeforeUpdate = especialidadesRepository.findAll().size();
        especialidades.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEspecialidadesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(especialidades))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Especialidades in the database
        List<Especialidades> especialidadesList = especialidadesRepository.findAll();
        assertThat(especialidadesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEspecialidades() throws Exception {
        // Initialize the database
        especialidadesRepository.saveAndFlush(especialidades);

        int databaseSizeBeforeDelete = especialidadesRepository.findAll().size();

        // Delete the especialidades
        restEspecialidadesMockMvc
            .perform(delete(ENTITY_API_URL_ID, especialidades.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Especialidades> especialidadesList = especialidadesRepository.findAll();
        assertThat(especialidadesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
