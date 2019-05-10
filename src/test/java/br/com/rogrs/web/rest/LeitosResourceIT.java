package br.com.rogrs.web.rest;

import br.com.rogrs.SafhApp;
import br.com.rogrs.domain.Leitos;
import br.com.rogrs.repository.LeitosRepository;
import br.com.rogrs.repository.search.LeitosSearchRepository;
import br.com.rogrs.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static br.com.rogrs.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link LeitosResource} REST controller.
 */
@SpringBootTest(classes = SafhApp.class)
public class LeitosResourceIT {

    private static final String DEFAULT_LEITO = "AAAAAAAAAA";
    private static final String UPDATED_LEITO = "BBBBBBBBBB";

    private static final String DEFAULT_TIPO = "AAAAAAAAAA";
    private static final String UPDATED_TIPO = "BBBBBBBBBB";

    @Autowired
    private LeitosRepository leitosRepository;

    /**
     * This repository is mocked in the br.com.rogrs.repository.search test package.
     *
     * @see br.com.rogrs.repository.search.LeitosSearchRepositoryMockConfiguration
     */
    @Autowired
    private LeitosSearchRepository mockLeitosSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restLeitosMockMvc;

    private Leitos leitos;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LeitosResource leitosResource = new LeitosResource(leitosRepository, mockLeitosSearchRepository);
        this.restLeitosMockMvc = MockMvcBuilders.standaloneSetup(leitosResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Leitos createEntity(EntityManager em) {
        Leitos leitos = new Leitos()
            .leito(DEFAULT_LEITO)
            .tipo(DEFAULT_TIPO);
        return leitos;
    }

    @BeforeEach
    public void initTest() {
        leitos = createEntity(em);
    }

    @Test
    @Transactional
    public void createLeitos() throws Exception {
        int databaseSizeBeforeCreate = leitosRepository.findAll().size();

        // Create the Leitos
        restLeitosMockMvc.perform(post("/api/leitos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leitos)))
            .andExpect(status().isCreated());

        // Validate the Leitos in the database
        List<Leitos> leitosList = leitosRepository.findAll();
        assertThat(leitosList).hasSize(databaseSizeBeforeCreate + 1);
        Leitos testLeitos = leitosList.get(leitosList.size() - 1);
        assertThat(testLeitos.getLeito()).isEqualTo(DEFAULT_LEITO);
        assertThat(testLeitos.getTipo()).isEqualTo(DEFAULT_TIPO);

        // Validate the Leitos in Elasticsearch
        verify(mockLeitosSearchRepository, times(1)).save(testLeitos);
    }

    @Test
    @Transactional
    public void createLeitosWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = leitosRepository.findAll().size();

        // Create the Leitos with an existing ID
        leitos.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeitosMockMvc.perform(post("/api/leitos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leitos)))
            .andExpect(status().isBadRequest());

        // Validate the Leitos in the database
        List<Leitos> leitosList = leitosRepository.findAll();
        assertThat(leitosList).hasSize(databaseSizeBeforeCreate);

        // Validate the Leitos in Elasticsearch
        verify(mockLeitosSearchRepository, times(0)).save(leitos);
    }


    @Test
    @Transactional
    public void checkLeitoIsRequired() throws Exception {
        int databaseSizeBeforeTest = leitosRepository.findAll().size();
        // set the field null
        leitos.setLeito(null);

        // Create the Leitos, which fails.

        restLeitosMockMvc.perform(post("/api/leitos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leitos)))
            .andExpect(status().isBadRequest());

        List<Leitos> leitosList = leitosRepository.findAll();
        assertThat(leitosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLeitos() throws Exception {
        // Initialize the database
        leitosRepository.saveAndFlush(leitos);

        // Get all the leitosList
        restLeitosMockMvc.perform(get("/api/leitos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leitos.getId().intValue())))
            .andExpect(jsonPath("$.[*].leito").value(hasItem(DEFAULT_LEITO.toString())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())));
    }
    
    @Test
    @Transactional
    public void getLeitos() throws Exception {
        // Initialize the database
        leitosRepository.saveAndFlush(leitos);

        // Get the leitos
        restLeitosMockMvc.perform(get("/api/leitos/{id}", leitos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(leitos.getId().intValue()))
            .andExpect(jsonPath("$.leito").value(DEFAULT_LEITO.toString()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLeitos() throws Exception {
        // Get the leitos
        restLeitosMockMvc.perform(get("/api/leitos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLeitos() throws Exception {
        // Initialize the database
        leitosRepository.saveAndFlush(leitos);

        int databaseSizeBeforeUpdate = leitosRepository.findAll().size();

        // Update the leitos
        Leitos updatedLeitos = leitosRepository.findById(leitos.getId()).get();
        // Disconnect from session so that the updates on updatedLeitos are not directly saved in db
        em.detach(updatedLeitos);
        updatedLeitos
            .leito(UPDATED_LEITO)
            .tipo(UPDATED_TIPO);

        restLeitosMockMvc.perform(put("/api/leitos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLeitos)))
            .andExpect(status().isOk());

        // Validate the Leitos in the database
        List<Leitos> leitosList = leitosRepository.findAll();
        assertThat(leitosList).hasSize(databaseSizeBeforeUpdate);
        Leitos testLeitos = leitosList.get(leitosList.size() - 1);
        assertThat(testLeitos.getLeito()).isEqualTo(UPDATED_LEITO);
        assertThat(testLeitos.getTipo()).isEqualTo(UPDATED_TIPO);

        // Validate the Leitos in Elasticsearch
        verify(mockLeitosSearchRepository, times(1)).save(testLeitos);
    }

    @Test
    @Transactional
    public void updateNonExistingLeitos() throws Exception {
        int databaseSizeBeforeUpdate = leitosRepository.findAll().size();

        // Create the Leitos

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeitosMockMvc.perform(put("/api/leitos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leitos)))
            .andExpect(status().isBadRequest());

        // Validate the Leitos in the database
        List<Leitos> leitosList = leitosRepository.findAll();
        assertThat(leitosList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Leitos in Elasticsearch
        verify(mockLeitosSearchRepository, times(0)).save(leitos);
    }

    @Test
    @Transactional
    public void deleteLeitos() throws Exception {
        // Initialize the database
        leitosRepository.saveAndFlush(leitos);

        int databaseSizeBeforeDelete = leitosRepository.findAll().size();

        // Delete the leitos
        restLeitosMockMvc.perform(delete("/api/leitos/{id}", leitos.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Leitos> leitosList = leitosRepository.findAll();
        assertThat(leitosList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Leitos in Elasticsearch
        verify(mockLeitosSearchRepository, times(1)).deleteById(leitos.getId());
    }

    @Test
    @Transactional
    public void searchLeitos() throws Exception {
        // Initialize the database
        leitosRepository.saveAndFlush(leitos);
        when(mockLeitosSearchRepository.search(queryStringQuery("id:" + leitos.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(leitos), PageRequest.of(0, 1), 1));
        // Search the leitos
        restLeitosMockMvc.perform(get("/api/_search/leitos?query=id:" + leitos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leitos.getId().intValue())))
            .andExpect(jsonPath("$.[*].leito").value(hasItem(DEFAULT_LEITO)))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Leitos.class);
        Leitos leitos1 = new Leitos();
        leitos1.setId(1L);
        Leitos leitos2 = new Leitos();
        leitos2.setId(leitos1.getId());
        assertThat(leitos1).isEqualTo(leitos2);
        leitos2.setId(2L);
        assertThat(leitos1).isNotEqualTo(leitos2);
        leitos1.setId(null);
        assertThat(leitos1).isNotEqualTo(leitos2);
    }
}
