package br.com.rogrs.safh.web.rest;

import br.com.rogrs.safh.SafhApp;
import br.com.rogrs.safh.domain.Enfermarias;
import br.com.rogrs.safh.repository.EnfermariasRepository;
import br.com.rogrs.safh.repository.search.EnfermariasSearchRepository;

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
 * Test class for the EnfermariasResource REST controller.
 *
 * @see EnfermariasResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SafhApp.class)
@WebAppConfiguration
@IntegrationTest
public class EnfermariasResourceIntTest {

    private static final String DEFAULT_ENFERMARIA = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ENFERMARIA = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private EnfermariasRepository enfermariasRepository;

    @Inject
    private EnfermariasSearchRepository enfermariasSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restEnfermariasMockMvc;

    private Enfermarias enfermarias;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EnfermariasResource enfermariasResource = new EnfermariasResource();
        ReflectionTestUtils.setField(enfermariasResource, "enfermariasSearchRepository", enfermariasSearchRepository);
        ReflectionTestUtils.setField(enfermariasResource, "enfermariasRepository", enfermariasRepository);
        this.restEnfermariasMockMvc = MockMvcBuilders.standaloneSetup(enfermariasResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        enfermariasSearchRepository.deleteAll();
        enfermarias = new Enfermarias();
        enfermarias.setEnfermaria(DEFAULT_ENFERMARIA);
    }

    @Test
    @Transactional
    public void createEnfermarias() throws Exception {
        int databaseSizeBeforeCreate = enfermariasRepository.findAll().size();

        // Create the Enfermarias

        restEnfermariasMockMvc.perform(post("/api/enfermarias")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(enfermarias)))
                .andExpect(status().isCreated());

        // Validate the Enfermarias in the database
        List<Enfermarias> enfermarias = enfermariasRepository.findAll();
        assertThat(enfermarias).hasSize(databaseSizeBeforeCreate + 1);
        Enfermarias testEnfermarias = enfermarias.get(enfermarias.size() - 1);
        assertThat(testEnfermarias.getEnfermaria()).isEqualTo(DEFAULT_ENFERMARIA);

        // Validate the Enfermarias in ElasticSearch
        Enfermarias enfermariasEs = enfermariasSearchRepository.findOne(testEnfermarias.getId());
        assertThat(enfermariasEs).isEqualToComparingFieldByField(testEnfermarias);
    }

    @Test
    @Transactional
    public void checkEnfermariaIsRequired() throws Exception {
        int databaseSizeBeforeTest = enfermariasRepository.findAll().size();
        // set the field null
        enfermarias.setEnfermaria(null);

        // Create the Enfermarias, which fails.

        restEnfermariasMockMvc.perform(post("/api/enfermarias")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(enfermarias)))
                .andExpect(status().isBadRequest());

        List<Enfermarias> enfermarias = enfermariasRepository.findAll();
        assertThat(enfermarias).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEnfermarias() throws Exception {
        // Initialize the database
        enfermariasRepository.saveAndFlush(enfermarias);

        // Get all the enfermarias
        restEnfermariasMockMvc.perform(get("/api/enfermarias?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(enfermarias.getId().intValue())))
                .andExpect(jsonPath("$.[*].enfermaria").value(hasItem(DEFAULT_ENFERMARIA.toString())));
    }

    @Test
    @Transactional
    public void getEnfermarias() throws Exception {
        // Initialize the database
        enfermariasRepository.saveAndFlush(enfermarias);

        // Get the enfermarias
        restEnfermariasMockMvc.perform(get("/api/enfermarias/{id}", enfermarias.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(enfermarias.getId().intValue()))
            .andExpect(jsonPath("$.enfermaria").value(DEFAULT_ENFERMARIA.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEnfermarias() throws Exception {
        // Get the enfermarias
        restEnfermariasMockMvc.perform(get("/api/enfermarias/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEnfermarias() throws Exception {
        // Initialize the database
        enfermariasRepository.saveAndFlush(enfermarias);
        enfermariasSearchRepository.save(enfermarias);
        int databaseSizeBeforeUpdate = enfermariasRepository.findAll().size();

        // Update the enfermarias
        Enfermarias updatedEnfermarias = new Enfermarias();
        updatedEnfermarias.setId(enfermarias.getId());
        updatedEnfermarias.setEnfermaria(UPDATED_ENFERMARIA);

        restEnfermariasMockMvc.perform(put("/api/enfermarias")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEnfermarias)))
                .andExpect(status().isOk());

        // Validate the Enfermarias in the database
        List<Enfermarias> enfermarias = enfermariasRepository.findAll();
        assertThat(enfermarias).hasSize(databaseSizeBeforeUpdate);
        Enfermarias testEnfermarias = enfermarias.get(enfermarias.size() - 1);
        assertThat(testEnfermarias.getEnfermaria()).isEqualTo(UPDATED_ENFERMARIA);

        // Validate the Enfermarias in ElasticSearch
        Enfermarias enfermariasEs = enfermariasSearchRepository.findOne(testEnfermarias.getId());
        assertThat(enfermariasEs).isEqualToComparingFieldByField(testEnfermarias);
    }

    @Test
    @Transactional
    public void deleteEnfermarias() throws Exception {
        // Initialize the database
        enfermariasRepository.saveAndFlush(enfermarias);
        enfermariasSearchRepository.save(enfermarias);
        int databaseSizeBeforeDelete = enfermariasRepository.findAll().size();

        // Get the enfermarias
        restEnfermariasMockMvc.perform(delete("/api/enfermarias/{id}", enfermarias.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean enfermariasExistsInEs = enfermariasSearchRepository.exists(enfermarias.getId());
        assertThat(enfermariasExistsInEs).isFalse();

        // Validate the database is empty
        List<Enfermarias> enfermarias = enfermariasRepository.findAll();
        assertThat(enfermarias).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEnfermarias() throws Exception {
        // Initialize the database
        enfermariasRepository.saveAndFlush(enfermarias);
        enfermariasSearchRepository.save(enfermarias);

        // Search the enfermarias
        restEnfermariasMockMvc.perform(get("/api/_search/enfermarias?query=id:" + enfermarias.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(enfermarias.getId().intValue())))
            .andExpect(jsonPath("$.[*].enfermaria").value(hasItem(DEFAULT_ENFERMARIA.toString())));
    }
}
