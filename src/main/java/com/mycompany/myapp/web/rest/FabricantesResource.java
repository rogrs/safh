package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Fabricantes;
import com.mycompany.myapp.repository.FabricantesRepository;
import com.mycompany.myapp.repository.search.FabricantesSearchRepository;
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
 * REST controller for managing Fabricantes.
 */
@RestController
@RequestMapping("/api")
public class FabricantesResource {

    private final Logger log = LoggerFactory.getLogger(FabricantesResource.class);
        
    @Inject
    private FabricantesRepository fabricantesRepository;
    
    @Inject
    private FabricantesSearchRepository fabricantesSearchRepository;
    
    /**
     * POST  /fabricantes : Create a new fabricantes.
     *
     * @param fabricantes the fabricantes to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fabricantes, or with status 400 (Bad Request) if the fabricantes has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/fabricantes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Fabricantes> createFabricantes(@Valid @RequestBody Fabricantes fabricantes) throws URISyntaxException {
        log.debug("REST request to save Fabricantes : {}", fabricantes);
        if (fabricantes.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("fabricantes", "idexists", "A new fabricantes cannot already have an ID")).body(null);
        }
        Fabricantes result = fabricantesRepository.save(fabricantes);
        fabricantesSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/fabricantes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("fabricantes", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /fabricantes : Updates an existing fabricantes.
     *
     * @param fabricantes the fabricantes to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fabricantes,
     * or with status 400 (Bad Request) if the fabricantes is not valid,
     * or with status 500 (Internal Server Error) if the fabricantes couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/fabricantes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Fabricantes> updateFabricantes(@Valid @RequestBody Fabricantes fabricantes) throws URISyntaxException {
        log.debug("REST request to update Fabricantes : {}", fabricantes);
        if (fabricantes.getId() == null) {
            return createFabricantes(fabricantes);
        }
        Fabricantes result = fabricantesRepository.save(fabricantes);
        fabricantesSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("fabricantes", fabricantes.getId().toString()))
            .body(result);
    }

    /**
     * GET  /fabricantes : get all the fabricantes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of fabricantes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/fabricantes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Fabricantes>> getAllFabricantes(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Fabricantes");
        Page<Fabricantes> page = fabricantesRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/fabricantes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /fabricantes/:id : get the "id" fabricantes.
     *
     * @param id the id of the fabricantes to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fabricantes, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/fabricantes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Fabricantes> getFabricantes(@PathVariable Long id) {
        log.debug("REST request to get Fabricantes : {}", id);
        Fabricantes fabricantes = fabricantesRepository.findOne(id);
        return Optional.ofNullable(fabricantes)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /fabricantes/:id : delete the "id" fabricantes.
     *
     * @param id the id of the fabricantes to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/fabricantes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFabricantes(@PathVariable Long id) {
        log.debug("REST request to delete Fabricantes : {}", id);
        fabricantesRepository.delete(id);
        fabricantesSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("fabricantes", id.toString())).build();
    }

    /**
     * SEARCH  /_search/fabricantes?query=:query : search for the fabricantes corresponding
     * to the query.
     *
     * @param query the query of the fabricantes search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/fabricantes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Fabricantes>> searchFabricantes(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Fabricantes for query {}", query);
        Page<Fabricantes> page = fabricantesSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/fabricantes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
