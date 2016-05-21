package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.EvolucaoPaciente;
import com.mycompany.myapp.repository.EvolucaoPacienteRepository;
import com.mycompany.myapp.repository.search.EvolucaoPacienteSearchRepository;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing EvolucaoPaciente.
 */
@RestController
@RequestMapping("/api")
public class EvolucaoPacienteResource {

    private final Logger log = LoggerFactory.getLogger(EvolucaoPacienteResource.class);
        
    @Inject
    private EvolucaoPacienteRepository evolucaoPacienteRepository;
    
    @Inject
    private EvolucaoPacienteSearchRepository evolucaoPacienteSearchRepository;
    
    /**
     * POST  /evolucao-pacientes : Create a new evolucaoPaciente.
     *
     * @param evolucaoPaciente the evolucaoPaciente to create
     * @return the ResponseEntity with status 201 (Created) and with body the new evolucaoPaciente, or with status 400 (Bad Request) if the evolucaoPaciente has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/evolucao-pacientes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EvolucaoPaciente> createEvolucaoPaciente(@Valid @RequestBody EvolucaoPaciente evolucaoPaciente) throws URISyntaxException {
        log.debug("REST request to save EvolucaoPaciente : {}", evolucaoPaciente);
        if (evolucaoPaciente.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("evolucaoPaciente", "idexists", "A new evolucaoPaciente cannot already have an ID")).body(null);
        }
        EvolucaoPaciente result = evolucaoPacienteRepository.save(evolucaoPaciente);
        evolucaoPacienteSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/evolucao-pacientes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("evolucaoPaciente", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /evolucao-pacientes : Updates an existing evolucaoPaciente.
     *
     * @param evolucaoPaciente the evolucaoPaciente to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated evolucaoPaciente,
     * or with status 400 (Bad Request) if the evolucaoPaciente is not valid,
     * or with status 500 (Internal Server Error) if the evolucaoPaciente couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/evolucao-pacientes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EvolucaoPaciente> updateEvolucaoPaciente(@Valid @RequestBody EvolucaoPaciente evolucaoPaciente) throws URISyntaxException {
        log.debug("REST request to update EvolucaoPaciente : {}", evolucaoPaciente);
        if (evolucaoPaciente.getId() == null) {
            return createEvolucaoPaciente(evolucaoPaciente);
        }
        EvolucaoPaciente result = evolucaoPacienteRepository.save(evolucaoPaciente);
        evolucaoPacienteSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("evolucaoPaciente", evolucaoPaciente.getId().toString()))
            .body(result);
    }

    /**
     * GET  /evolucao-pacientes : get all the evolucaoPacientes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of evolucaoPacientes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/evolucao-pacientes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<EvolucaoPaciente>> getAllEvolucaoPacientes(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of EvolucaoPacientes");
        Page<EvolucaoPaciente> page = evolucaoPacienteRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/evolucao-pacientes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /evolucao-pacientes/:id : get the "id" evolucaoPaciente.
     *
     * @param id the id of the evolucaoPaciente to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the evolucaoPaciente, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/evolucao-pacientes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EvolucaoPaciente> getEvolucaoPaciente(@PathVariable Long id) {
        log.debug("REST request to get EvolucaoPaciente : {}", id);
        EvolucaoPaciente evolucaoPaciente = evolucaoPacienteRepository.findOne(id);
        return Optional.ofNullable(evolucaoPaciente)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /evolucao-pacientes/:id : delete the "id" evolucaoPaciente.
     *
     * @param id the id of the evolucaoPaciente to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/evolucao-pacientes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEvolucaoPaciente(@PathVariable Long id) {
        log.debug("REST request to delete EvolucaoPaciente : {}", id);
        evolucaoPacienteRepository.delete(id);
        evolucaoPacienteSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("evolucaoPaciente", id.toString())).build();
    }

    /**
     * SEARCH  /_search/evolucao-pacientes?query=:query : search for the evolucaoPaciente corresponding
     * to the query.
     *
     * @param query the query of the evolucaoPaciente search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/evolucao-pacientes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<EvolucaoPaciente>> searchEvolucaoPacientes(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of EvolucaoPacientes for query {}", query);
        Page<EvolucaoPaciente> page = evolucaoPacienteSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/evolucao-pacientes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
