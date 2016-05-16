package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.SafhApp;
import com.mycompany.myapp.domain.Posologias;
import com.mycompany.myapp.repository.PosologiasRepository;
import com.mycompany.myapp.repository.search.PosologiasSearchRepository;

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
 * Test class for the PosologiasResource REST controller.
 *
 * @see PosologiasResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SafhApp.class)
@WebAppConfiguration
@IntegrationTest
public class PosologiasResourceIntTest {

    private static final String DEFAULT_DESCRICAO = "AAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBB";

    @Inject
    private PosologiasRepository posologiasRepository;

    @Inject
    private PosologiasSearchRepository posologiasSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPosologiasMockMvc;

    private Posologias posologias;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PosologiasResource posologiasResource = new PosologiasResource();
        ReflectionTestUtils.setField(posologiasResource, "posologiasSearchRepository", posologiasSearchRepository);
        ReflectionTestUtils.setField(posologiasResource, "posologiasRepository", posologiasRepository);
        this.restPosologiasMockMvc = MockMvcBuilders.standaloneSetup(posologiasResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        posologiasSearchRepository.deleteAll();
        posologias = new Posologias();
        posologias.setDescricao(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    public void createPosologias() throws Exception {
        int databaseSizeBeforeCreate = posologiasRepository.findAll().size();

        // Create the Posologias

        restPosologiasMockMvc.perform(post("/api/posologias")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(posologias)))
                .andExpect(status().isCreated());

        // Validate the Posologias in the database
        List<Posologias> posologias = posologiasRepository.findAll();
        assertThat(posologias).hasSize(databaseSizeBeforeCreate + 1);
        Posologias testPosologias = posologias.get(posologias.size() - 1);
        assertThat(testPosologias.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);

        // Validate the Posologias in ElasticSearch
        Posologias posologiasEs = posologiasSearchRepository.findOne(testPosologias.getId());
        assertThat(posologiasEs).isEqualToComparingFieldByField(testPosologias);
    }

    @Test
    @Transactional
    public void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = posologiasRepository.findAll().size();
        // set the field null
        posologias.setDescricao(null);

        // Create the Posologias, which fails.

        restPosologiasMockMvc.perform(post("/api/posologias")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(posologias)))
                .andExpect(status().isBadRequest());

        List<Posologias> posologias = posologiasRepository.findAll();
        assertThat(posologias).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPosologias() throws Exception {
        // Initialize the database
        posologiasRepository.saveAndFlush(posologias);

        // Get all the posologias
        restPosologiasMockMvc.perform(get("/api/posologias?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(posologias.getId().intValue())))
                .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())));
    }

    @Test
    @Transactional
    public void getPosologias() throws Exception {
        // Initialize the database
        posologiasRepository.saveAndFlush(posologias);

        // Get the posologias
        restPosologiasMockMvc.perform(get("/api/posologias/{id}", posologias.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(posologias.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPosologias() throws Exception {
        // Get the posologias
        restPosologiasMockMvc.perform(get("/api/posologias/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePosologias() throws Exception {
        // Initialize the database
        posologiasRepository.saveAndFlush(posologias);
        posologiasSearchRepository.save(posologias);
        int databaseSizeBeforeUpdate = posologiasRepository.findAll().size();

        // Update the posologias
        Posologias updatedPosologias = new Posologias();
        updatedPosologias.setId(posologias.getId());
        updatedPosologias.setDescricao(UPDATED_DESCRICAO);

        restPosologiasMockMvc.perform(put("/api/posologias")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPosologias)))
                .andExpect(status().isOk());

        // Validate the Posologias in the database
        List<Posologias> posologias = posologiasRepository.findAll();
        assertThat(posologias).hasSize(databaseSizeBeforeUpdate);
        Posologias testPosologias = posologias.get(posologias.size() - 1);
        assertThat(testPosologias.getDescricao()).isEqualTo(UPDATED_DESCRICAO);

        // Validate the Posologias in ElasticSearch
        Posologias posologiasEs = posologiasSearchRepository.findOne(testPosologias.getId());
        assertThat(posologiasEs).isEqualToComparingFieldByField(testPosologias);
    }

    @Test
    @Transactional
    public void deletePosologias() throws Exception {
        // Initialize the database
        posologiasRepository.saveAndFlush(posologias);
        posologiasSearchRepository.save(posologias);
        int databaseSizeBeforeDelete = posologiasRepository.findAll().size();

        // Get the posologias
        restPosologiasMockMvc.perform(delete("/api/posologias/{id}", posologias.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean posologiasExistsInEs = posologiasSearchRepository.exists(posologias.getId());
        assertThat(posologiasExistsInEs).isFalse();

        // Validate the database is empty
        List<Posologias> posologias = posologiasRepository.findAll();
        assertThat(posologias).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPosologias() throws Exception {
        // Initialize the database
        posologiasRepository.saveAndFlush(posologias);
        posologiasSearchRepository.save(posologias);

        // Search the posologias
        restPosologiasMockMvc.perform(get("/api/_search/posologias?query=id:" + posologias.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(posologias.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())));
    }
}
