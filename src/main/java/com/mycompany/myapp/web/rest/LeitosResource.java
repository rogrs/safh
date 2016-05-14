package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Leitos;
import com.mycompany.myapp.repository.LeitosRepository;
import com.mycompany.myapp.repository.search.LeitosSearchRepository;
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
 * REST controller for managing Leitos.
 */
@RestController
@RequestMapping("/api")
public class LeitosResource {

    private final Logger log = LoggerFactory.getLogger(LeitosResource.class);
        
    @Inject
    private LeitosRepository leitosRepository;
    
    @Inject
    private LeitosSearchRepository leitosSearchRepository;
    
    /**
     * POST  /leitos : Create a new leitos.
     *
     * @param leitos the leitos to create
     * @return the ResponseEntity with status 201 (Created) and with body the new leitos, or with status 400 (Bad Request) if the leitos has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/leitos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Leitos> createLeitos(@Valid @RequestBody Leitos leitos) throws URISyntaxException {
        log.debug("REST request to save Leitos : {}", leitos);
        if (leitos.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("leitos", "idexists", "A new leitos cannot already have an ID")).body(null);
        }
        Leitos result = leitosRepository.save(leitos);
        leitosSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/leitos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("leitos", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /leitos : Updates an existing leitos.
     *
     * @param leitos the leitos to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated leitos,
     * or with status 400 (Bad Request) if the leitos is not valid,
     * or with status 500 (Internal Server Error) if the leitos couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/leitos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Leitos> updateLeitos(@Valid @RequestBody Leitos leitos) throws URISyntaxException {
        log.debug("REST request to update Leitos : {}", leitos);
        if (leitos.getId() == null) {
            return createLeitos(leitos);
        }
        Leitos result = leitosRepository.save(leitos);
        leitosSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("leitos", leitos.getId().toString()))
            .body(result);
    }

    /**
     * GET  /leitos : get all the leitos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of leitos in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/leitos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Leitos>> getAllLeitos(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Leitos");
        Page<Leitos> page = leitosRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/leitos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /leitos/:id : get the "id" leitos.
     *
     * @param id the id of the leitos to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the leitos, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/leitos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Leitos> getLeitos(@PathVariable Long id) {
        log.debug("REST request to get Leitos : {}", id);
        Leitos leitos = leitosRepository.findOne(id);
        return Optional.ofNullable(leitos)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /leitos/:id : delete the "id" leitos.
     *
     * @param id the id of the leitos to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/leitos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLeitos(@PathVariable Long id) {
        log.debug("REST request to delete Leitos : {}", id);
        leitosRepository.delete(id);
        leitosSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("leitos", id.toString())).build();
    }

    /**
     * SEARCH  /_search/leitos?query=:query : search for the leitos corresponding
     * to the query.
     *
     * @param query the query of the leitos search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/leitos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Leitos>> searchLeitos(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Leitos for query {}", query);
        Page<Leitos> page = leitosSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/leitos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
