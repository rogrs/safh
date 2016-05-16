package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.SafhApp;
import com.mycompany.myapp.domain.Fabricantes;
import com.mycompany.myapp.repository.FabricantesRepository;
import com.mycompany.myapp.repository.search.FabricantesSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the FabricantesResource REST controller.
 *
 * @see FabricantesResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SafhApp.class)
@WebAppConfiguration
@IntegrationTest
public class FabricantesResourceIntTest {

    private static final String DEFAULT_DESCRICAO = "AAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBB";

    @Inject
    private FabricantesRepository fabricantesRepository;

    @Inject
    private FabricantesSearchRepository fabricantesSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restFabricantesMockMvc;

    private Fabricantes fabricantes;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FabricantesResource fabricantesResource = new FabricantesResource();
        ReflectionTestUtils.setField(fabricantesResource, "fabricantesSearchRepository", fabricantesSearchRepository);
        ReflectionTestUtils.setField(fabricantesResource, "fabricantesRepository", fabricantesRepository);
        this.restFabricantesMockMvc = MockMvcBuilders.standaloneSetup(fabricantesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        fabricantesSearchRepository.deleteAll();
        fabricantes = new Fabricantes();
        fabricantes.setDescricao(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    public void createFabricantes() throws Exception {
        int databaseSizeBeforeCreate = fabricantesRepository.findAll().size();

        // Create the Fabricantes

        restFabricantesMockMvc.perform(post("/api/fabricantes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fabricantes)))
                .andExpect(status().isCreated());

        // Validate the Fabricantes in the database
        List<Fabricantes> fabricantes = fabricantesRepository.findAll();
        assertThat(fabricantes).hasSize(databaseSizeBeforeCreate + 1);
        Fabricantes testFabricantes = fabricantes.get(fabricantes.size() - 1);
        assertThat(testFabricantes.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);

        // Validate the Fabricantes in ElasticSearch
        Fabricantes fabricantesEs = fabricantesSearchRepository.findOne(testFabricantes.getId());
        assertThat(fabricantesEs).isEqualToComparingFieldByField(testFabricantes);
    }

    @Test
    @Transactional
    public void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = fabricantesRepository.findAll().size();
        // set the field null
        fabricantes.setDescricao(null);

        // Create the Fabricantes, which fails.

        restFabricantesMockMvc.perform(post("/api/fabricantes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fabricantes)))
                .andExpect(status().isBadRequest());

        List<Fabricantes> fabricantes = fabricantesRepository.findAll();
        assertThat(fabricantes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFabricantes() throws Exception {
        // Initialize the database
        fabricantesRepository.saveAndFlush(fabricantes);

        // Get all the fabricantes
        restFabricantesMockMvc.perform(get("/api/fabricantes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(fabricantes.getId().intValue())))
                .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())));
    }

    @Test
    @Transactional
    public void getFabricantes() throws Exception {
        // Initialize the database
        fabricantesRepository.saveAndFlush(fabricantes);

        // Get the fabricantes
        restFabricantesMockMvc.perform(get("/api/fabricantes/{id}", fabricantes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(fabricantes.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFabricantes() throws Exception {
        // Get the fabricantes
        restFabricantesMockMvc.perform(get("/api/fabricantes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFabricantes() throws Exception {
        // Initialize the database
        fabricantesRepository.saveAndFlush(fabricantes);
        fabricantesSearchRepository.save(fabricantes);
        int databaseSizeBeforeUpdate = fabricantesRepository.findAll().size();

        // Update the fabricantes
        Fabricantes updatedFabricantes = new Fabricantes();
        updatedFabricantes.setId(fabricantes.getId());
        updatedFabricantes.setDescricao(UPDATED_DESCRICAO);

        restFabricantesMockMvc.perform(put("/api/fabricantes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedFabricantes)))
                .andExpect(status().isOk());

        // Validate the Fabricantes in the database
        List<Fabricantes> fabricantes = fabricantesRepository.findAll();
        assertThat(fabricantes).hasSize(databaseSizeBeforeUpdate);
        Fabricantes testFabricantes = fabricantes.get(fabricantes.size() - 1);
        assertThat(testFabricantes.getDescricao()).isEqualTo(UPDATED_DESCRICAO);

        // Validate the Fabricantes in ElasticSearch
        Fabricantes fabricantesEs = fabricantesSearchRepository.findOne(testFabricantes.getId());
        assertThat(fabricantesEs).isEqualToComparingFieldByField(testFabricantes);
    }

    @Test
    @Transactional
    public void deleteFabricantes() throws Exception {
        // Initialize the database
        fabricantesRepository.saveAndFlush(fabricantes);
        fabricantesSearchRepository.save(fabricantes);
        int databaseSizeBeforeDelete = fabricantesRepository.findAll().size();

        // Get the fabricantes
        restFabricantesMockMvc.perform(delete("/api/fabricantes/{id}", fabricantes.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean fabricantesExistsInEs = fabricantesSearchRepository.exists(fabricantes.getId());
        assertThat(fabricantesExistsInEs).isFalse();

        // Validate the database is empty
        List<Fabricantes> fabricantes = fabricantesRepository.findAll();
        assertThat(fabricantes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchFabricantes() throws Exception {
        // Initialize the database
        fabricantesRepository.saveAndFlush(fabricantes);
        fabricantesSearchRepository.save(fabricantes);

        // Search the fabricantes
        restFabricantesMockMvc.perform(get("/api/_search/fabricantes?query=id:" + fabricantes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fabricantes.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())));
    }
}
