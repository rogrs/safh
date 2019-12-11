package br.com.rogrs.web.rest;

import br.com.rogrs.SafhApp;
import br.com.rogrs.domain.Especialidades;
import br.com.rogrs.repository.EspecialidadesRepository;
import br.com.rogrs.repository.search.EspecialidadesSearchRepository;
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
 * Integration tests for the {@link EspecialidadesResource} REST controller.
 */
@SpringBootTest(classes = SafhApp.class)
public class EspecialidadesResourceIT {

    private static final String DEFAULT_ESPECIALIDADE = "AAAAAAAAAA";
    private static final String UPDATED_ESPECIALIDADE = "BBBBBBBBBB";

    @Autowired
    private EspecialidadesRepository especialidadesRepository;

    /**
     * This repository is mocked in the br.com.rogrs.repository.search test package.
     *
     * @see br.com.rogrs.repository.search.EspecialidadesSearchRepositoryMockConfiguration
     */
    @Autowired
    private EspecialidadesSearchRepository mockEspecialidadesSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restEspecialidadesMockMvc;

    private Especialidades especialidades;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EspecialidadesResource especialidadesResource = new EspecialidadesResource(especialidadesRepository, mockEspecialidadesSearchRepository);
        this.restEspecialidadesMockMvc = MockMvcBuilders.standaloneSetup(especialidadesResource)
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
    public static Especialidades createEntity() {
        Especialidades especialidades = new Especialidades()
            .especialidade(DEFAULT_ESPECIALIDADE);
        return especialidades;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Especialidades createUpdatedEntity() {
        Especialidades especialidades = new Especialidades()
            .especialidade(UPDATED_ESPECIALIDADE);
        return especialidades;
    }

    @BeforeEach
    public void initTest() {
        especialidadesRepository.deleteAll();
        especialidades = createEntity();
    }

    @Test
    public void createEspecialidades() throws Exception {
        int databaseSizeBeforeCreate = especialidadesRepository.findAll().size();

        // Create the Especialidades
        restEspecialidadesMockMvc.perform(post("/api/especialidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(especialidades)))
            .andExpect(status().isCreated());

        // Validate the Especialidades in the database
        List<Especialidades> especialidadesList = especialidadesRepository.findAll();
        assertThat(especialidadesList).hasSize(databaseSizeBeforeCreate + 1);
        Especialidades testEspecialidades = especialidadesList.get(especialidadesList.size() - 1);
        assertThat(testEspecialidades.getEspecialidade()).isEqualTo(DEFAULT_ESPECIALIDADE);

        // Validate the Especialidades in Elasticsearch
        verify(mockEspecialidadesSearchRepository, times(1)).save(testEspecialidades);
    }

    @Test
    public void createEspecialidadesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = especialidadesRepository.findAll().size();

        // Create the Especialidades with an existing ID
        especialidades.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restEspecialidadesMockMvc.perform(post("/api/especialidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(especialidades)))
            .andExpect(status().isBadRequest());

        // Validate the Especialidades in the database
        List<Especialidades> especialidadesList = especialidadesRepository.findAll();
        assertThat(especialidadesList).hasSize(databaseSizeBeforeCreate);

        // Validate the Especialidades in Elasticsearch
        verify(mockEspecialidadesSearchRepository, times(0)).save(especialidades);
    }


    @Test
    public void checkEspecialidadeIsRequired() throws Exception {
        int databaseSizeBeforeTest = especialidadesRepository.findAll().size();
        // set the field null
        especialidades.setEspecialidade(null);

        // Create the Especialidades, which fails.

        restEspecialidadesMockMvc.perform(post("/api/especialidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(especialidades)))
            .andExpect(status().isBadRequest());

        List<Especialidades> especialidadesList = especialidadesRepository.findAll();
        assertThat(especialidadesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllEspecialidades() throws Exception {
        // Initialize the database
        especialidadesRepository.save(especialidades);

        // Get all the especialidadesList
        restEspecialidadesMockMvc.perform(get("/api/especialidades?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(especialidades.getId())))
            .andExpect(jsonPath("$.[*].especialidade").value(hasItem(DEFAULT_ESPECIALIDADE)));
    }
    
    @Test
    public void getEspecialidades() throws Exception {
        // Initialize the database
        especialidadesRepository.save(especialidades);

        // Get the especialidades
        restEspecialidadesMockMvc.perform(get("/api/especialidades/{id}", especialidades.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(especialidades.getId()))
            .andExpect(jsonPath("$.especialidade").value(DEFAULT_ESPECIALIDADE));
    }

    @Test
    public void getNonExistingEspecialidades() throws Exception {
        // Get the especialidades
        restEspecialidadesMockMvc.perform(get("/api/especialidades/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateEspecialidades() throws Exception {
        // Initialize the database
        especialidadesRepository.save(especialidades);

        int databaseSizeBeforeUpdate = especialidadesRepository.findAll().size();

        // Update the especialidades
        Especialidades updatedEspecialidades = especialidadesRepository.findById(especialidades.getId()).get();
        updatedEspecialidades
            .especialidade(UPDATED_ESPECIALIDADE);

        restEspecialidadesMockMvc.perform(put("/api/especialidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEspecialidades)))
            .andExpect(status().isOk());

        // Validate the Especialidades in the database
        List<Especialidades> especialidadesList = especialidadesRepository.findAll();
        assertThat(especialidadesList).hasSize(databaseSizeBeforeUpdate);
        Especialidades testEspecialidades = especialidadesList.get(especialidadesList.size() - 1);
        assertThat(testEspecialidades.getEspecialidade()).isEqualTo(UPDATED_ESPECIALIDADE);

        // Validate the Especialidades in Elasticsearch
        verify(mockEspecialidadesSearchRepository, times(1)).save(testEspecialidades);
    }

    @Test
    public void updateNonExistingEspecialidades() throws Exception {
        int databaseSizeBeforeUpdate = especialidadesRepository.findAll().size();

        // Create the Especialidades

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEspecialidadesMockMvc.perform(put("/api/especialidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(especialidades)))
            .andExpect(status().isBadRequest());

        // Validate the Especialidades in the database
        List<Especialidades> especialidadesList = especialidadesRepository.findAll();
        assertThat(especialidadesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Especialidades in Elasticsearch
        verify(mockEspecialidadesSearchRepository, times(0)).save(especialidades);
    }

    @Test
    public void deleteEspecialidades() throws Exception {
        // Initialize the database
        especialidadesRepository.save(especialidades);

        int databaseSizeBeforeDelete = especialidadesRepository.findAll().size();

        // Delete the especialidades
        restEspecialidadesMockMvc.perform(delete("/api/especialidades/{id}", especialidades.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Especialidades> especialidadesList = especialidadesRepository.findAll();
        assertThat(especialidadesList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Especialidades in Elasticsearch
        verify(mockEspecialidadesSearchRepository, times(1)).deleteById(especialidades.getId());
    }

    @Test
    public void searchEspecialidades() throws Exception {
        // Initialize the database
        especialidadesRepository.save(especialidades);
        when(mockEspecialidadesSearchRepository.search(queryStringQuery("id:" + especialidades.getId())))
            .thenReturn(Collections.singletonList(especialidades));
        // Search the especialidades
        restEspecialidadesMockMvc.perform(get("/api/_search/especialidades?query=id:" + especialidades.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(especialidades.getId())))
            .andExpect(jsonPath("$.[*].especialidade").value(hasItem(DEFAULT_ESPECIALIDADE)));
    }
}
