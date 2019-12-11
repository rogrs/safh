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
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;


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
 * Integration tests for the {@link LeitosResource} REST controller.
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
    public static Leitos createEntity() {
        Leitos leitos = new Leitos()
            .leito(DEFAULT_LEITO)
            .tipo(DEFAULT_TIPO);
        return leitos;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Leitos createUpdatedEntity() {
        Leitos leitos = new Leitos()
            .leito(UPDATED_LEITO)
            .tipo(UPDATED_TIPO);
        return leitos;
    }

    @BeforeEach
    public void initTest() {
        leitosRepository.deleteAll();
        leitos = createEntity();
    }

    @Test
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
    public void createLeitosWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = leitosRepository.findAll().size();

        // Create the Leitos with an existing ID
        leitos.setId("existing_id");

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
    public void getAllLeitos() throws Exception {
        // Initialize the database
        leitosRepository.save(leitos);

        // Get all the leitosList
        restLeitosMockMvc.perform(get("/api/leitos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leitos.getId())))
            .andExpect(jsonPath("$.[*].leito").value(hasItem(DEFAULT_LEITO)))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO)));
    }
    
    @Test
    public void getLeitos() throws Exception {
        // Initialize the database
        leitosRepository.save(leitos);

        // Get the leitos
        restLeitosMockMvc.perform(get("/api/leitos/{id}", leitos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(leitos.getId()))
            .andExpect(jsonPath("$.leito").value(DEFAULT_LEITO))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO));
    }

    @Test
    public void getNonExistingLeitos() throws Exception {
        // Get the leitos
        restLeitosMockMvc.perform(get("/api/leitos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateLeitos() throws Exception {
        // Initialize the database
        leitosRepository.save(leitos);

        int databaseSizeBeforeUpdate = leitosRepository.findAll().size();

        // Update the leitos
        Leitos updatedLeitos = leitosRepository.findById(leitos.getId()).get();
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
    public void deleteLeitos() throws Exception {
        // Initialize the database
        leitosRepository.save(leitos);

        int databaseSizeBeforeDelete = leitosRepository.findAll().size();

        // Delete the leitos
        restLeitosMockMvc.perform(delete("/api/leitos/{id}", leitos.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Leitos> leitosList = leitosRepository.findAll();
        assertThat(leitosList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Leitos in Elasticsearch
        verify(mockLeitosSearchRepository, times(1)).deleteById(leitos.getId());
    }

    @Test
    public void searchLeitos() throws Exception {
        // Initialize the database
        leitosRepository.save(leitos);
        when(mockLeitosSearchRepository.search(queryStringQuery("id:" + leitos.getId())))
            .thenReturn(Collections.singletonList(leitos));
        // Search the leitos
        restLeitosMockMvc.perform(get("/api/_search/leitos?query=id:" + leitos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leitos.getId())))
            .andExpect(jsonPath("$.[*].leito").value(hasItem(DEFAULT_LEITO)))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO)));
    }
}
