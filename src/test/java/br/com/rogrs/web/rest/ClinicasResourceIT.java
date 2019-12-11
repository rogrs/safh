package br.com.rogrs.web.rest;

import br.com.rogrs.SafhApp;
import br.com.rogrs.domain.Clinicas;
import br.com.rogrs.repository.ClinicasRepository;
import br.com.rogrs.repository.search.ClinicasSearchRepository;
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
 * Integration tests for the {@link ClinicasResource} REST controller.
 */
@SpringBootTest(classes = SafhApp.class)
public class ClinicasResourceIT {

    private static final String DEFAULT_CLINICA = "AAAAAAAAAA";
    private static final String UPDATED_CLINICA = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    @Autowired
    private ClinicasRepository clinicasRepository;

    /**
     * This repository is mocked in the br.com.rogrs.repository.search test package.
     *
     * @see br.com.rogrs.repository.search.ClinicasSearchRepositoryMockConfiguration
     */
    @Autowired
    private ClinicasSearchRepository mockClinicasSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restClinicasMockMvc;

    private Clinicas clinicas;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ClinicasResource clinicasResource = new ClinicasResource(clinicasRepository, mockClinicasSearchRepository);
        this.restClinicasMockMvc = MockMvcBuilders.standaloneSetup(clinicasResource)
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
    public static Clinicas createEntity() {
        Clinicas clinicas = new Clinicas()
            .clinica(DEFAULT_CLINICA)
            .descricao(DEFAULT_DESCRICAO);
        return clinicas;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Clinicas createUpdatedEntity() {
        Clinicas clinicas = new Clinicas()
            .clinica(UPDATED_CLINICA)
            .descricao(UPDATED_DESCRICAO);
        return clinicas;
    }

    @BeforeEach
    public void initTest() {
        clinicasRepository.deleteAll();
        clinicas = createEntity();
    }

    @Test
    public void createClinicas() throws Exception {
        int databaseSizeBeforeCreate = clinicasRepository.findAll().size();

        // Create the Clinicas
        restClinicasMockMvc.perform(post("/api/clinicas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clinicas)))
            .andExpect(status().isCreated());

        // Validate the Clinicas in the database
        List<Clinicas> clinicasList = clinicasRepository.findAll();
        assertThat(clinicasList).hasSize(databaseSizeBeforeCreate + 1);
        Clinicas testClinicas = clinicasList.get(clinicasList.size() - 1);
        assertThat(testClinicas.getClinica()).isEqualTo(DEFAULT_CLINICA);
        assertThat(testClinicas.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);

        // Validate the Clinicas in Elasticsearch
        verify(mockClinicasSearchRepository, times(1)).save(testClinicas);
    }

    @Test
    public void createClinicasWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = clinicasRepository.findAll().size();

        // Create the Clinicas with an existing ID
        clinicas.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restClinicasMockMvc.perform(post("/api/clinicas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clinicas)))
            .andExpect(status().isBadRequest());

        // Validate the Clinicas in the database
        List<Clinicas> clinicasList = clinicasRepository.findAll();
        assertThat(clinicasList).hasSize(databaseSizeBeforeCreate);

        // Validate the Clinicas in Elasticsearch
        verify(mockClinicasSearchRepository, times(0)).save(clinicas);
    }


    @Test
    public void checkClinicaIsRequired() throws Exception {
        int databaseSizeBeforeTest = clinicasRepository.findAll().size();
        // set the field null
        clinicas.setClinica(null);

        // Create the Clinicas, which fails.

        restClinicasMockMvc.perform(post("/api/clinicas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clinicas)))
            .andExpect(status().isBadRequest());

        List<Clinicas> clinicasList = clinicasRepository.findAll();
        assertThat(clinicasList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllClinicas() throws Exception {
        // Initialize the database
        clinicasRepository.save(clinicas);

        // Get all the clinicasList
        restClinicasMockMvc.perform(get("/api/clinicas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clinicas.getId())))
            .andExpect(jsonPath("$.[*].clinica").value(hasItem(DEFAULT_CLINICA)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));
    }
    
    @Test
    public void getClinicas() throws Exception {
        // Initialize the database
        clinicasRepository.save(clinicas);

        // Get the clinicas
        restClinicasMockMvc.perform(get("/api/clinicas/{id}", clinicas.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(clinicas.getId()))
            .andExpect(jsonPath("$.clinica").value(DEFAULT_CLINICA))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO));
    }

    @Test
    public void getNonExistingClinicas() throws Exception {
        // Get the clinicas
        restClinicasMockMvc.perform(get("/api/clinicas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateClinicas() throws Exception {
        // Initialize the database
        clinicasRepository.save(clinicas);

        int databaseSizeBeforeUpdate = clinicasRepository.findAll().size();

        // Update the clinicas
        Clinicas updatedClinicas = clinicasRepository.findById(clinicas.getId()).get();
        updatedClinicas
            .clinica(UPDATED_CLINICA)
            .descricao(UPDATED_DESCRICAO);

        restClinicasMockMvc.perform(put("/api/clinicas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedClinicas)))
            .andExpect(status().isOk());

        // Validate the Clinicas in the database
        List<Clinicas> clinicasList = clinicasRepository.findAll();
        assertThat(clinicasList).hasSize(databaseSizeBeforeUpdate);
        Clinicas testClinicas = clinicasList.get(clinicasList.size() - 1);
        assertThat(testClinicas.getClinica()).isEqualTo(UPDATED_CLINICA);
        assertThat(testClinicas.getDescricao()).isEqualTo(UPDATED_DESCRICAO);

        // Validate the Clinicas in Elasticsearch
        verify(mockClinicasSearchRepository, times(1)).save(testClinicas);
    }

    @Test
    public void updateNonExistingClinicas() throws Exception {
        int databaseSizeBeforeUpdate = clinicasRepository.findAll().size();

        // Create the Clinicas

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClinicasMockMvc.perform(put("/api/clinicas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clinicas)))
            .andExpect(status().isBadRequest());

        // Validate the Clinicas in the database
        List<Clinicas> clinicasList = clinicasRepository.findAll();
        assertThat(clinicasList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Clinicas in Elasticsearch
        verify(mockClinicasSearchRepository, times(0)).save(clinicas);
    }

    @Test
    public void deleteClinicas() throws Exception {
        // Initialize the database
        clinicasRepository.save(clinicas);

        int databaseSizeBeforeDelete = clinicasRepository.findAll().size();

        // Delete the clinicas
        restClinicasMockMvc.perform(delete("/api/clinicas/{id}", clinicas.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Clinicas> clinicasList = clinicasRepository.findAll();
        assertThat(clinicasList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Clinicas in Elasticsearch
        verify(mockClinicasSearchRepository, times(1)).deleteById(clinicas.getId());
    }

    @Test
    public void searchClinicas() throws Exception {
        // Initialize the database
        clinicasRepository.save(clinicas);
        when(mockClinicasSearchRepository.search(queryStringQuery("id:" + clinicas.getId())))
            .thenReturn(Collections.singletonList(clinicas));
        // Search the clinicas
        restClinicasMockMvc.perform(get("/api/_search/clinicas?query=id:" + clinicas.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clinicas.getId())))
            .andExpect(jsonPath("$.[*].clinica").value(hasItem(DEFAULT_CLINICA)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));
    }
}
