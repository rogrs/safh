package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Pacientes;
import com.mycompany.myapp.repository.PacientesRepository;
import com.mycompany.myapp.repository.search.PacientesSearchRepository;
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
 * REST controller for managing Pacientes.
 */
@RestController
@RequestMapping("/api")
public class PacientesResource {

    private final Logger log = LoggerFactory.getLogger(PacientesResource.class);
        
    @Inject
    private PacientesRepository pacientesRepository;
    
    @Inject
    private PacientesSearchRepository pacientesSearchRepository;
    
    /**
     * POST  /pacientes : Create a new pacientes.
     *
     * @param pacientes the pacientes to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pacientes, or with status 400 (Bad Request) if the pacientes has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/pacientes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Pacientes> createPacientes(@Valid @RequestBody Pacientes pacientes) throws URISyntaxException {
        log.debug("REST request to save Pacientes : {}", pacientes);
        if (pacientes.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("pacientes", "idexists", "A new pacientes cannot already have an ID")).body(null);
        }
        Pacientes result = pacientesRepository.save(pacientes);
        pacientesSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/pacientes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("pacientes", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pacientes : Updates an existing pacientes.
     *
     * @param pacientes the pacientes to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pacientes,
     * or with status 400 (Bad Request) if the pacientes is not valid,
     * or with status 500 (Internal Server Error) if the pacientes couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/pacientes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Pacientes> updatePacientes(@Valid @RequestBody Pacientes pacientes) throws URISyntaxException {
        log.debug("REST request to update Pacientes : {}", pacientes);
        if (pacientes.getId() == null) {
            return createPacientes(pacientes);
        }
        Pacientes result = pacientesRepository.save(pacientes);
        pacientesSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("pacientes", pacientes.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pacientes : get all the pacientes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pacientes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/pacientes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Pacientes>> getAllPacientes(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Pacientes");
        Page<Pacientes> page = pacientesRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pacientes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /pacientes/:id : get the "id" pacientes.
     *
     * @param id the id of the pacientes to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pacientes, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/pacientes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Pacientes> getPacientes(@PathVariable Long id) {
        log.debug("REST request to get Pacientes : {}", id);
        Pacientes pacientes = pacientesRepository.findOne(id);
        return Optional.ofNullable(pacientes)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /pacientes/:id : delete the "id" pacientes.
     *
     * @param id the id of the pacientes to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/pacientes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePacientes(@PathVariable Long id) {
        log.debug("REST request to delete Pacientes : {}", id);
        pacientesRepository.delete(id);
        pacientesSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("pacientes", id.toString())).build();
    }

    /**
     * SEARCH  /_search/pacientes?query=:query : search for the pacientes corresponding
     * to the query.
     *
     * @param query the query of the pacientes search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/pacientes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Pacientes>> searchPacientes(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Pacientes for query {}", query);
        Page<Pacientes> page = pacientesSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/pacientes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
