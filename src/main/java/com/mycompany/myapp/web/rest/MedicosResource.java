package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Medicos;
import com.mycompany.myapp.repository.MedicosRepository;
import com.mycompany.myapp.repository.search.MedicosSearchRepository;
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
 * REST controller for managing Medicos.
 */
@RestController
@RequestMapping("/api")
public class MedicosResource {

    private final Logger log = LoggerFactory.getLogger(MedicosResource.class);
        
    @Inject
    private MedicosRepository medicosRepository;
    
    @Inject
    private MedicosSearchRepository medicosSearchRepository;
    
    /**
     * POST  /medicos : Create a new medicos.
     *
     * @param medicos the medicos to create
     * @return the ResponseEntity with status 201 (Created) and with body the new medicos, or with status 400 (Bad Request) if the medicos has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/medicos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Medicos> createMedicos(@Valid @RequestBody Medicos medicos) throws URISyntaxException {
        log.debug("REST request to save Medicos : {}", medicos);
        if (medicos.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("medicos", "idexists", "A new medicos cannot already have an ID")).body(null);
        }
        Medicos result = medicosRepository.save(medicos);
        medicosSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/medicos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("medicos", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /medicos : Updates an existing medicos.
     *
     * @param medicos the medicos to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated medicos,
     * or with status 400 (Bad Request) if the medicos is not valid,
     * or with status 500 (Internal Server Error) if the medicos couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/medicos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Medicos> updateMedicos(@Valid @RequestBody Medicos medicos) throws URISyntaxException {
        log.debug("REST request to update Medicos : {}", medicos);
        if (medicos.getId() == null) {
            return createMedicos(medicos);
        }
        Medicos result = medicosRepository.save(medicos);
        medicosSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("medicos", medicos.getId().toString()))
            .body(result);
    }

    /**
     * GET  /medicos : get all the medicos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of medicos in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/medicos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Medicos>> getAllMedicos(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Medicos");
        Page<Medicos> page = medicosRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/medicos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /medicos/:id : get the "id" medicos.
     *
     * @param id the id of the medicos to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the medicos, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/medicos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Medicos> getMedicos(@PathVariable Long id) {
        log.debug("REST request to get Medicos : {}", id);
        Medicos medicos = medicosRepository.findOne(id);
        return Optional.ofNullable(medicos)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /medicos/:id : delete the "id" medicos.
     *
     * @param id the id of the medicos to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/medicos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMedicos(@PathVariable Long id) {
        log.debug("REST request to delete Medicos : {}", id);
        medicosRepository.delete(id);
        medicosSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("medicos", id.toString())).build();
    }

    /**
     * SEARCH  /_search/medicos?query=:query : search for the medicos corresponding
     * to the query.
     *
     * @param query the query of the medicos search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/medicos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Medicos>> searchMedicos(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Medicos for query {}", query);
        Page<Medicos> page = medicosSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/medicos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
