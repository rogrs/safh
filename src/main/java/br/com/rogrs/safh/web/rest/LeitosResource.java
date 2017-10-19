package br.com.rogrs.safh.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.rogrs.safh.domain.Leitos;

import br.com.rogrs.safh.repository.LeitosRepository;
import br.com.rogrs.safh.repository.search.LeitosSearchRepository;
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
 * REST controller for managing Leitos.
 */
@RestController
@RequestMapping("/api")
public class LeitosResource {

    private final Logger log = LoggerFactory.getLogger(LeitosResource.class);

    private static final String ENTITY_NAME = "leitos";

    private final LeitosRepository leitosRepository;

    private final LeitosSearchRepository leitosSearchRepository;

    public LeitosResource(LeitosRepository leitosRepository, LeitosSearchRepository leitosSearchRepository) {
        this.leitosRepository = leitosRepository;
        this.leitosSearchRepository = leitosSearchRepository;
    }

    /**
     * POST  /leitos : Create a new leitos.
     *
     * @param leitos the leitos to create
     * @return the ResponseEntity with status 201 (Created) and with body the new leitos, or with status 400 (Bad Request) if the leitos has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/leitos")
    @Timed
    public ResponseEntity<Leitos> createLeitos(@Valid @RequestBody Leitos leitos) throws URISyntaxException {
        log.debug("REST request to save Leitos : {}", leitos);
        if (leitos.getId() != null) {
            throw new BadRequestAlertException("A new leitos cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Leitos result = leitosRepository.save(leitos);
        leitosSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/leitos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /leitos : Updates an existing leitos.
     *
     * @param leitos the leitos to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated leitos,
     * or with status 400 (Bad Request) if the leitos is not valid,
     * or with status 500 (Internal Server Error) if the leitos couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/leitos")
    @Timed
    public ResponseEntity<Leitos> updateLeitos(@Valid @RequestBody Leitos leitos) throws URISyntaxException {
        log.debug("REST request to update Leitos : {}", leitos);
        if (leitos.getId() == null) {
            return createLeitos(leitos);
        }
        Leitos result = leitosRepository.save(leitos);
        leitosSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, leitos.getId().toString()))
            .body(result);
    }

    /**
     * GET  /leitos : get all the leitos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of leitos in body
     */
    @GetMapping("/leitos")
    @Timed
    public ResponseEntity<List<Leitos>> getAllLeitos(@ApiParam Pageable pageable) {
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
    @GetMapping("/leitos/{id}")
    @Timed
    public ResponseEntity<Leitos> getLeitos(@PathVariable Long id) {
        log.debug("REST request to get Leitos : {}", id);
        Leitos leitos = leitosRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(leitos));
    }

    /**
     * DELETE  /leitos/:id : delete the "id" leitos.
     *
     * @param id the id of the leitos to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/leitos/{id}")
    @Timed
    public ResponseEntity<Void> deleteLeitos(@PathVariable Long id) {
        log.debug("REST request to delete Leitos : {}", id);
        leitosRepository.delete(id);
        leitosSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/leitos?query=:query : search for the leitos corresponding
     * to the query.
     *
     * @param query the query of the leitos search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/leitos")
    @Timed
    public ResponseEntity<List<Leitos>> searchLeitos(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Leitos for query {}", query);
        Page<Leitos> page = leitosSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/leitos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
