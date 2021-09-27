package br.com.rogrs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.rogrs.IntegrationTest;
import br.com.rogrs.domain.Leitos;
import br.com.rogrs.repository.LeitosRepository;
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
 * Integration tests for the {@link LeitosResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LeitosResourceIT {

    private static final String DEFAULT_LEITO = "AAAAAAAAAA";
    private static final String UPDATED_LEITO = "BBBBBBBBBB";

    private static final String DEFAULT_TIPO = "AAAAAAAAAA";
    private static final String UPDATED_TIPO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/leitos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LeitosRepository leitosRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLeitosMockMvc;

    private Leitos leitos;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Leitos createEntity(EntityManager em) {
        Leitos leitos = new Leitos().leito(DEFAULT_LEITO).tipo(DEFAULT_TIPO);
        return leitos;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Leitos createUpdatedEntity(EntityManager em) {
        Leitos leitos = new Leitos().leito(UPDATED_LEITO).tipo(UPDATED_TIPO);
        return leitos;
    }

    @BeforeEach
    public void initTest() {
        leitos = createEntity(em);
    }

    @Test
    @Transactional
    void createLeitos() throws Exception {
        int databaseSizeBeforeCreate = leitosRepository.findAll().size();
        // Create the Leitos
        restLeitosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leitos)))
            .andExpect(status().isCreated());

        // Validate the Leitos in the database
        List<Leitos> leitosList = leitosRepository.findAll();
        assertThat(leitosList).hasSize(databaseSizeBeforeCreate + 1);
        Leitos testLeitos = leitosList.get(leitosList.size() - 1);
        assertThat(testLeitos.getLeito()).isEqualTo(DEFAULT_LEITO);
        assertThat(testLeitos.getTipo()).isEqualTo(DEFAULT_TIPO);
    }

    @Test
    @Transactional
    void createLeitosWithExistingId() throws Exception {
        // Create the Leitos with an existing ID
        leitos.setId(1L);

        int databaseSizeBeforeCreate = leitosRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeitosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leitos)))
            .andExpect(status().isBadRequest());

        // Validate the Leitos in the database
        List<Leitos> leitosList = leitosRepository.findAll();
        assertThat(leitosList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLeitoIsRequired() throws Exception {
        int databaseSizeBeforeTest = leitosRepository.findAll().size();
        // set the field null
        leitos.setLeito(null);

        // Create the Leitos, which fails.

        restLeitosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leitos)))
            .andExpect(status().isBadRequest());

        List<Leitos> leitosList = leitosRepository.findAll();
        assertThat(leitosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLeitos() throws Exception {
        // Initialize the database
        leitosRepository.saveAndFlush(leitos);

        // Get all the leitosList
        restLeitosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leitos.getId().intValue())))
            .andExpect(jsonPath("$.[*].leito").value(hasItem(DEFAULT_LEITO)))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO)));
    }

    @Test
    @Transactional
    void getLeitos() throws Exception {
        // Initialize the database
        leitosRepository.saveAndFlush(leitos);

        // Get the leitos
        restLeitosMockMvc
            .perform(get(ENTITY_API_URL_ID, leitos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(leitos.getId().intValue()))
            .andExpect(jsonPath("$.leito").value(DEFAULT_LEITO))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO));
    }

    @Test
    @Transactional
    void getNonExistingLeitos() throws Exception {
        // Get the leitos
        restLeitosMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLeitos() throws Exception {
        // Initialize the database
        leitosRepository.saveAndFlush(leitos);

        int databaseSizeBeforeUpdate = leitosRepository.findAll().size();

        // Update the leitos
        Leitos updatedLeitos = leitosRepository.findById(leitos.getId()).get();
        // Disconnect from session so that the updates on updatedLeitos are not directly saved in db
        em.detach(updatedLeitos);
        updatedLeitos.leito(UPDATED_LEITO).tipo(UPDATED_TIPO);

        restLeitosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLeitos.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLeitos))
            )
            .andExpect(status().isOk());

        // Validate the Leitos in the database
        List<Leitos> leitosList = leitosRepository.findAll();
        assertThat(leitosList).hasSize(databaseSizeBeforeUpdate);
        Leitos testLeitos = leitosList.get(leitosList.size() - 1);
        assertThat(testLeitos.getLeito()).isEqualTo(UPDATED_LEITO);
        assertThat(testLeitos.getTipo()).isEqualTo(UPDATED_TIPO);
    }

    @Test
    @Transactional
    void putNonExistingLeitos() throws Exception {
        int databaseSizeBeforeUpdate = leitosRepository.findAll().size();
        leitos.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeitosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leitos.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leitos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Leitos in the database
        List<Leitos> leitosList = leitosRepository.findAll();
        assertThat(leitosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLeitos() throws Exception {
        int databaseSizeBeforeUpdate = leitosRepository.findAll().size();
        leitos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeitosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leitos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Leitos in the database
        List<Leitos> leitosList = leitosRepository.findAll();
        assertThat(leitosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLeitos() throws Exception {
        int databaseSizeBeforeUpdate = leitosRepository.findAll().size();
        leitos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeitosMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leitos)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Leitos in the database
        List<Leitos> leitosList = leitosRepository.findAll();
        assertThat(leitosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLeitosWithPatch() throws Exception {
        // Initialize the database
        leitosRepository.saveAndFlush(leitos);

        int databaseSizeBeforeUpdate = leitosRepository.findAll().size();

        // Update the leitos using partial update
        Leitos partialUpdatedLeitos = new Leitos();
        partialUpdatedLeitos.setId(leitos.getId());

        partialUpdatedLeitos.leito(UPDATED_LEITO).tipo(UPDATED_TIPO);

        restLeitosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeitos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLeitos))
            )
            .andExpect(status().isOk());

        // Validate the Leitos in the database
        List<Leitos> leitosList = leitosRepository.findAll();
        assertThat(leitosList).hasSize(databaseSizeBeforeUpdate);
        Leitos testLeitos = leitosList.get(leitosList.size() - 1);
        assertThat(testLeitos.getLeito()).isEqualTo(UPDATED_LEITO);
        assertThat(testLeitos.getTipo()).isEqualTo(UPDATED_TIPO);
    }

    @Test
    @Transactional
    void fullUpdateLeitosWithPatch() throws Exception {
        // Initialize the database
        leitosRepository.saveAndFlush(leitos);

        int databaseSizeBeforeUpdate = leitosRepository.findAll().size();

        // Update the leitos using partial update
        Leitos partialUpdatedLeitos = new Leitos();
        partialUpdatedLeitos.setId(leitos.getId());

        partialUpdatedLeitos.leito(UPDATED_LEITO).tipo(UPDATED_TIPO);

        restLeitosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeitos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLeitos))
            )
            .andExpect(status().isOk());

        // Validate the Leitos in the database
        List<Leitos> leitosList = leitosRepository.findAll();
        assertThat(leitosList).hasSize(databaseSizeBeforeUpdate);
        Leitos testLeitos = leitosList.get(leitosList.size() - 1);
        assertThat(testLeitos.getLeito()).isEqualTo(UPDATED_LEITO);
        assertThat(testLeitos.getTipo()).isEqualTo(UPDATED_TIPO);
    }

    @Test
    @Transactional
    void patchNonExistingLeitos() throws Exception {
        int databaseSizeBeforeUpdate = leitosRepository.findAll().size();
        leitos.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeitosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, leitos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leitos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Leitos in the database
        List<Leitos> leitosList = leitosRepository.findAll();
        assertThat(leitosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLeitos() throws Exception {
        int databaseSizeBeforeUpdate = leitosRepository.findAll().size();
        leitos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeitosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leitos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Leitos in the database
        List<Leitos> leitosList = leitosRepository.findAll();
        assertThat(leitosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLeitos() throws Exception {
        int databaseSizeBeforeUpdate = leitosRepository.findAll().size();
        leitos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeitosMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(leitos)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Leitos in the database
        List<Leitos> leitosList = leitosRepository.findAll();
        assertThat(leitosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLeitos() throws Exception {
        // Initialize the database
        leitosRepository.saveAndFlush(leitos);

        int databaseSizeBeforeDelete = leitosRepository.findAll().size();

        // Delete the leitos
        restLeitosMockMvc
            .perform(delete(ENTITY_API_URL_ID, leitos.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Leitos> leitosList = leitosRepository.findAll();
        assertThat(leitosList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
