package br.com.rogrs.safh.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.rogrs.safh.domain.Dietas;
import br.com.rogrs.safh.repository.DietasRepository;
import br.com.rogrs.safh.repository.search.DietasSearchRepository;
import br.com.rogrs.safh.web.rest.util.HeaderUtil;
import br.com.rogrs.safh.web.rest.util.PaginationUtil;
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
 * REST controller for managing Dietas.
 */
@RestController
@RequestMapping("/api")
public class DietasResource {

    private final Logger log = LoggerFactory.getLogger(DietasResource.class);
        
    @Inject
    private DietasRepository dietasRepository;
    
    @Inject
    private DietasSearchRepository dietasSearchRepository;
    
    /**
     * POST  /dietas : Create a new dietas.
     *
     * @param dietas the dietas to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dietas, or with status 400 (Bad Request) if the dietas has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/dietas",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Dietas> createDietas(@Valid @RequestBody Dietas dietas) throws URISyntaxException {
        log.debug("REST request to save Dietas : {}", dietas);
        if (dietas.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("dietas", "idexists", "A new dietas cannot already have an ID")).body(null);
        }
        Dietas result = dietasRepository.save(dietas);
        dietasSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/dietas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("dietas", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /dietas : Updates an existing dietas.
     *
     * @param dietas the dietas to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated dietas,
     * or with status 400 (Bad Request) if the dietas is not valid,
     * or with status 500 (Internal Server Error) if the dietas couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/dietas",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Dietas> updateDietas(@Valid @RequestBody Dietas dietas) throws URISyntaxException {
        log.debug("REST request to update Dietas : {}", dietas);
        if (dietas.getId() == null) {
            return createDietas(dietas);
        }
        Dietas result = dietasRepository.save(dietas);
        dietasSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("dietas", dietas.getId().toString()))
            .body(result);
    }

    /**
     * GET  /dietas : get all the dietas.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of dietas in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/dietas",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Dietas>> getAllDietas(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Dietas");
        Page<Dietas> page = dietasRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/dietas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /dietas/:id : get the "id" dietas.
     *
     * @param id the id of the dietas to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dietas, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/dietas/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Dietas> getDietas(@PathVariable Long id) {
        log.debug("REST request to get Dietas : {}", id);
        Dietas dietas = dietasRepository.findOne(id);
        return Optional.ofNullable(dietas)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /dietas/:id : delete the "id" dietas.
     *
     * @param id the id of the dietas to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/dietas/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDietas(@PathVariable Long id) {
        log.debug("REST request to delete Dietas : {}", id);
        dietasRepository.delete(id);
        dietasSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("dietas", id.toString())).build();
    }

    /**
     * SEARCH  /_search/dietas?query=:query : search for the dietas corresponding
     * to the query.
     *
     * @param query the query of the dietas search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/dietas",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Dietas>> searchDietas(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Dietas for query {}", query);
        Page<Dietas> page = dietasSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/dietas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
