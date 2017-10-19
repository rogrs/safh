package br.com.rogrs.safh.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.rogrs.safh.domain.Enfermarias;

import br.com.rogrs.safh.repository.EnfermariasRepository;
import br.com.rogrs.safh.repository.search.EnfermariasSearchRepository;
import br.com.rogrs.safh.web.rest.errors.BadRequestAlertException;
import br.com.rogrs.safh.web.rest.util.HeaderUtil;
import br.com.rogrs.safh.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    private static final String ENTITY_NAME = "enfermarias";

    private final EnfermariasRepository enfermariasRepository;

    private final EnfermariasSearchRepository enfermariasSearchRepository;

    public EnfermariasResource(EnfermariasRepository enfermariasRepository, EnfermariasSearchRepository enfermariasSearchRepository) {
        this.enfermariasRepository = enfermariasRepository;
        this.enfermariasSearchRepository = enfermariasSearchRepository;
    }

    /**
     * POST  /enfermarias : Create a new enfermarias.
     *
     * @param enfermarias the enfermarias to create
     * @return the ResponseEntity with status 201 (Created) and with body the new enfermarias, or with status 400 (Bad Request) if the enfermarias has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/enfermarias")
    @Timed
    public ResponseEntity<Enfermarias> createEnfermarias(@Valid @RequestBody Enfermarias enfermarias) throws URISyntaxException {
        log.debug("REST request to save Enfermarias : {}", enfermarias);
        if (enfermarias.getId() != null) {
            throw new BadRequestAlertException("A new enfermarias cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Enfermarias result = enfermariasRepository.save(enfermarias);
        enfermariasSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/enfermarias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /enfermarias : Updates an existing enfermarias.
     *
     * @param enfermarias the enfermarias to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated enfermarias,
     * or with status 400 (Bad Request) if the enfermarias is not valid,
     * or with status 500 (Internal Server Error) if the enfermarias couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/enfermarias")
    @Timed
    public ResponseEntity<Enfermarias> updateEnfermarias(@Valid @RequestBody Enfermarias enfermarias) throws URISyntaxException {
        log.debug("REST request to update Enfermarias : {}", enfermarias);
        if (enfermarias.getId() == null) {
            return createEnfermarias(enfermarias);
        }
        Enfermarias result = enfermariasRepository.save(enfermarias);
        enfermariasSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, enfermarias.getId().toString()))
            .body(result);
    }

    /**
     * GET  /enfermarias : get all the enfermarias.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of enfermarias in body
     */
    @GetMapping("/enfermarias")
    @Timed
    public ResponseEntity<List<Enfermarias>> getAllEnfermarias(@ApiParam Pageable pageable) {
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
    @GetMapping("/enfermarias/{id}")
    @Timed
    public ResponseEntity<Enfermarias> getEnfermarias(@PathVariable Long id) {
        log.debug("REST request to get Enfermarias : {}", id);
        Enfermarias enfermarias = enfermariasRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(enfermarias));
    }

    /**
     * DELETE  /enfermarias/:id : delete the "id" enfermarias.
     *
     * @param id the id of the enfermarias to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/enfermarias/{id}")
    @Timed
    public ResponseEntity<Void> deleteEnfermarias(@PathVariable Long id) {
        log.debug("REST request to delete Enfermarias : {}", id);
        enfermariasRepository.delete(id);
        enfermariasSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/enfermarias?query=:query : search for the enfermarias corresponding
     * to the query.
     *
     * @param query the query of the enfermarias search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/enfermarias")
    @Timed
    public ResponseEntity<List<Enfermarias>> searchEnfermarias(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Enfermarias for query {}", query);
        Page<Enfermarias> page = enfermariasSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/enfermarias");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
