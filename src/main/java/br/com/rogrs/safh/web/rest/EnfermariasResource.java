package br.com.rogrs.safh.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.rogrs.safh.domain.Enfermarias;
import br.com.rogrs.safh.repository.EnfermariasRepository;
import br.com.rogrs.safh.repository.search.EnfermariasSearchRepository;
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
 * REST controller for managing Enfermarias.
 */
@RestController
@RequestMapping("/api")
public class EnfermariasResource {

    private final Logger log = LoggerFactory.getLogger(EnfermariasResource.class);
        
    @Inject
    private EnfermariasRepository enfermariasRepository;
    
    @Inject
    private EnfermariasSearchRepository enfermariasSearchRepository;
    
    /**
     * POST  /enfermarias : Create a new enfermarias.
     *
     * @param enfermarias the enfermarias to create
     * @return the ResponseEntity with status 201 (Created) and with body the new enfermarias, or with status 400 (Bad Request) if the enfermarias has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/enfermarias",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Enfermarias> createEnfermarias(@Valid @RequestBody Enfermarias enfermarias) throws URISyntaxException {
        log.debug("REST request to save Enfermarias : {}", enfermarias);
        if (enfermarias.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("enfermarias", "idexists", "A new enfermarias cannot already have an ID")).body(null);
        }
        Enfermarias result = enfermariasRepository.save(enfermarias);
        enfermariasSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/enfermarias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("enfermarias", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /enfermarias : Updates an existing enfermarias.
     *
     * @param enfermarias the enfermarias to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated enfermarias,
     * or with status 400 (Bad Request) if the enfermarias is not valid,
     * or with status 500 (Internal Server Error) if the enfermarias couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/enfermarias",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Enfermarias> updateEnfermarias(@Valid @RequestBody Enfermarias enfermarias) throws URISyntaxException {
        log.debug("REST request to update Enfermarias : {}", enfermarias);
        if (enfermarias.getId() == null) {
            return createEnfermarias(enfermarias);
        }
        Enfermarias result = enfermariasRepository.save(enfermarias);
        enfermariasSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("enfermarias", enfermarias.getId().toString()))
            .body(result);
    }

    /**
     * GET  /enfermarias : get all the enfermarias.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of enfermarias in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/enfermarias",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Enfermarias>> getAllEnfermarias(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Enfermarias");
        Page<Enfermarias> page = enfermariasRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/enfermarias");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /enfermarias/:id : get the "id" enfermarias.
     *
     * @param id the id of the enfermarias to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the enfermarias, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/enfermarias/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Enfermarias> getEnfermarias(@PathVariable Long id) {
        log.debug("REST request to get Enfermarias : {}", id);
        Enfermarias enfermarias = enfermariasRepository.findOne(id);
        return Optional.ofNullable(enfermarias)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /enfermarias/:id : delete the "id" enfermarias.
     *
     * @param id the id of the enfermarias to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/enfermarias/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEnfermarias(@PathVariable Long id) {
        log.debug("REST request to delete Enfermarias : {}", id);
        enfermariasRepository.delete(id);
        enfermariasSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("enfermarias", id.toString())).build();
    }

    /**
     * SEARCH  /_search/enfermarias?query=:query : search for the enfermarias corresponding
     * to the query.
     *
     * @param query the query of the enfermarias search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/enfermarias",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Enfermarias>> searchEnfermarias(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Enfermarias for query {}", query);
        Page<Enfermarias> page = enfermariasSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/enfermarias");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
