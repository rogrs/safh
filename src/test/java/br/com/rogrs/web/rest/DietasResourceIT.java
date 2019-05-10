package br.com.rogrs.web.rest;

import br.com.rogrs.SafhApp;
import br.com.rogrs.domain.Dietas;
import br.com.rogrs.repository.DietasRepository;
import br.com.rogrs.repository.search.DietasSearchRepository;
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
 * Integration tests for the {@Link DietasResource} REST controller.
 */
@SpringBootTest(classes = SafhApp.class)
public class DietasResourceIT {

    private static final String DEFAULT_DIETA = "AAAAAAAAAA";
    private static final String UPDATED_DIETA = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    @Autowired
    private DietasRepository dietasRepository;

    /**
     * This repository is mocked in the br.com.rogrs.repository.search test package.
     *
     * @see br.com.rogrs.repository.search.DietasSearchRepositoryMockConfiguration
     */
    @Autowired
    private DietasSearchRepository mockDietasSearchRepository;

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

    private MockMvc restDietasMockMvc;

    private Dietas dietas;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DietasResource dietasResource = new DietasResource(dietasRepository, mockDietasSearchRepository);
        this.restDietasMockMvc = MockMvcBuilders.standaloneSetup(dietasResource)
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
    public static Dietas createEntity(EntityManager em) {
        Dietas dietas = new Dietas()
            .dieta(DEFAULT_DIETA)
            .descricao(DEFAULT_DESCRICAO);
        return dietas;
    }

    @BeforeEach
    public void initTest() {
        dietas = createEntity(em);
    }

    @Test
    @Transactional
    public void createDietas() throws Exception {
        int databaseSizeBeforeCreate = dietasRepository.findAll().size();

        // Create the Dietas
        restDietasMockMvc.perform(post("/api/dietas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dietas)))
            .andExpect(status().isCreated());

        // Validate the Dietas in the database
        List<Dietas> dietasList = dietasRepository.findAll();
        assertThat(dietasList).hasSize(databaseSizeBeforeCreate + 1);
        Dietas testDietas = dietasList.get(dietasList.size() - 1);
        assertThat(testDietas.getDieta()).isEqualTo(DEFAULT_DIETA);
        assertThat(testDietas.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);

        // Validate the Dietas in Elasticsearch
        verify(mockDietasSearchRepository, times(1)).save(testDietas);
    }

    @Test
    @Transactional
    public void createDietasWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dietasRepository.findAll().size();

        // Create the Dietas with an existing ID
        dietas.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDietasMockMvc.perform(post("/api/dietas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dietas)))
            .andExpect(status().isBadRequest());

        // Validate the Dietas in the database
        List<Dietas> dietasList = dietasRepository.findAll();
        assertThat(dietasList).hasSize(databaseSizeBeforeCreate);

        // Validate the Dietas in Elasticsearch
        verify(mockDietasSearchRepository, times(0)).save(dietas);
    }


    @Test
    @Transactional
    public void checkDietaIsRequired() throws Exception {
        int databaseSizeBeforeTest = dietasRepository.findAll().size();
        // set the field null
        dietas.setDieta(null);

        // Create the Dietas, which fails.

        restDietasMockMvc.perform(post("/api/dietas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dietas)))
            .andExpect(status().isBadRequest());

        List<Dietas> dietasList = dietasRepository.findAll();
        assertThat(dietasList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDietas() throws Exception {
        // Initialize the database
        dietasRepository.saveAndFlush(dietas);

        // Get all the dietasList
        restDietasMockMvc.perform(get("/api/dietas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dietas.getId().intValue())))
            .andExpect(jsonPath("$.[*].dieta").value(hasItem(DEFAULT_DIETA.toString())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())));
    }
    
    @Test
    @Transactional
    public void getDietas() throws Exception {
        // Initialize the database
        dietasRepository.saveAndFlush(dietas);

        // Get the dietas
        restDietasMockMvc.perform(get("/api/dietas/{id}", dietas.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dietas.getId().intValue()))
            .andExpect(jsonPath("$.dieta").value(DEFAULT_DIETA.toString()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDietas() throws Exception {
        // Get the dietas
        restDietasMockMvc.perform(get("/api/dietas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDietas() throws Exception {
        // Initialize the database
        dietasRepository.saveAndFlush(dietas);

        int databaseSizeBeforeUpdate = dietasRepository.findAll().size();

        // Update the dietas
        Dietas updatedDietas = dietasRepository.findById(dietas.getId()).get();
        // Disconnect from session so that the updates on updatedDietas are not directly saved in db
        em.detach(updatedDietas);
        updatedDietas
            .dieta(UPDATED_DIETA)
            .descricao(UPDATED_DESCRICAO);

        restDietasMockMvc.perform(put("/api/dietas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDietas)))
            .andExpect(status().isOk());

        // Validate the Dietas in the database
        List<Dietas> dietasList = dietasRepository.findAll();
        assertThat(dietasList).hasSize(databaseSizeBeforeUpdate);
        Dietas testDietas = dietasList.get(dietasList.size() - 1);
        assertThat(testDietas.getDieta()).isEqualTo(UPDATED_DIETA);
        assertThat(testDietas.getDescricao()).isEqualTo(UPDATED_DESCRICAO);

        // Validate the Dietas in Elasticsearch
        verify(mockDietasSearchRepository, times(1)).save(testDietas);
    }

    @Test
    @Transactional
    public void updateNonExistingDietas() throws Exception {
        int databaseSizeBeforeUpdate = dietasRepository.findAll().size();

        // Create the Dietas

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDietasMockMvc.perform(put("/api/dietas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dietas)))
            .andExpect(status().isBadRequest());

        // Validate the Dietas in the database
        List<Dietas> dietasList = dietasRepository.findAll();
        assertThat(dietasList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Dietas in Elasticsearch
        verify(mockDietasSearchRepository, times(0)).save(dietas);
    }

    @Test
    @Transactional
    public void deleteDietas() throws Exception {
        // Initialize the database
        dietasRepository.saveAndFlush(dietas);

        int databaseSizeBeforeDelete = dietasRepository.findAll().size();

        // Delete the dietas
        restDietasMockMvc.perform(delete("/api/dietas/{id}", dietas.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Dietas> dietasList = dietasRepository.findAll();
        assertThat(dietasList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Dietas in Elasticsearch
        verify(mockDietasSearchRepository, times(1)).deleteById(dietas.getId());
    }

    @Test
    @Transactional
    public void searchDietas() throws Exception {
        // Initialize the database
        dietasRepository.saveAndFlush(dietas);
        when(mockDietasSearchRepository.search(queryStringQuery("id:" + dietas.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(dietas), PageRequest.of(0, 1), 1));
        // Search the dietas
        restDietasMockMvc.perform(get("/api/_search/dietas?query=id:" + dietas.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dietas.getId().intValue())))
            .andExpect(jsonPath("$.[*].dieta").value(hasItem(DEFAULT_DIETA)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dietas.class);
        Dietas dietas1 = new Dietas();
        dietas1.setId(1L);
        Dietas dietas2 = new Dietas();
        dietas2.setId(dietas1.getId());
        assertThat(dietas1).isEqualTo(dietas2);
        dietas2.setId(2L);
        assertThat(dietas1).isNotEqualTo(dietas2);
        dietas1.setId(null);
        assertThat(dietas1).isNotEqualTo(dietas2);
    }
}
