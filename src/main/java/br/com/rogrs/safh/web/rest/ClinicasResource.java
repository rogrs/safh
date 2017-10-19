package br.com.rogrs.safh.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.rogrs.safh.domain.Clinicas;

import br.com.rogrs.safh.repository.ClinicasRepository;
import br.com.rogrs.safh.repository.search.ClinicasSearchRepository;
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
 * REST controller for managing Clinicas.
 */
@RestController
@RequestMapping("/api")
public class ClinicasResource {

    private final Logger log = LoggerFactory.getLogger(ClinicasResource.class);

    private static final String ENTITY_NAME = "clinicas";

    private final ClinicasRepository clinicasRepository;

    private final ClinicasSearchRepository clinicasSearchRepository;

    public ClinicasResource(ClinicasRepository clinicasRepository, ClinicasSearchRepository clinicasSearchRepository) {
        this.clinicasRepository = clinicasRepository;
        this.clinicasSearchRepository = clinicasSearchRepository;
    }

    /**
     * POST  /clinicas : Create a new clinicas.
     *
     * @param clinicas the clinicas to create
     * @return the ResponseEntity with status 201 (Created) and with body the new clinicas, or with status 400 (Bad Request) if the clinicas has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/clinicas")
    @Timed
    public ResponseEntity<Clinicas> createClinicas(@Valid @RequestBody Clinicas clinicas) throws URISyntaxException {
        log.debug("REST request to save Clinicas : {}", clinicas);
        if (clinicas.getId() != null) {
            throw new BadRequestAlertException("A new clinicas cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Clinicas result = clinicasRepository.save(clinicas);
        clinicasSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/clinicas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /clinicas : Updates an existing clinicas.
     *
     * @param clinicas the clinicas to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated clinicas,
     * or with status 400 (Bad Request) if the clinicas is not valid,
     * or with status 500 (Internal Server Error) if the clinicas couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/clinicas")
    @Timed
    public ResponseEntity<Clinicas> updateClinicas(@Valid @RequestBody Clinicas clinicas) throws URISyntaxException {
        log.debug("REST request to update Clinicas : {}", clinicas);
        if (clinicas.getId() == null) {
            return createClinicas(clinicas);
        }
        Clinicas result = clinicasRepository.save(clinicas);
        clinicasSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, clinicas.getId().toString()))
            .body(result);
    }

    /**
     * GET  /clinicas : get all the clinicas.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of clinicas in body
     */
    @GetMapping("/clinicas")
    @Timed
    public ResponseEntity<List<Clinicas>> getAllClinicas(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Clinicas");
        Page<Clinicas> page = clinicasRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/clinicas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /clinicas/:id : get the "id" clinicas.
     *
     * @param id the id of the clinicas to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the clinicas, or with status 404 (Not Found)
     */
    @GetMapping("/clinicas/{id}")
    @Timed
    public ResponseEntity<Clinicas> getClinicas(@PathVariable Long id) {
        log.debug("REST request to get Clinicas : {}", id);
        Clinicas clinicas = clinicasRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(clinicas));
    }

    /**
     * DELETE  /clinicas/:id : delete the "id" clinicas.
     *
     * @param id the id of the clinicas to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/clinicas/{id}")
    @Timed
    public ResponseEntity<Void> deleteClinicas(@PathVariable Long id) {
        log.debug("REST request to delete Clinicas : {}", id);
        clinicasRepository.delete(id);
        clinicasSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/clinicas?query=:query : search for the clinicas corresponding
     * to the query.
     *
     * @param query the query of the clinicas search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/clinicas")
    @Timed
    public ResponseEntity<List<Clinicas>> searchClinicas(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Clinicas for query {}", query);
        Page<Clinicas> page = clinicasSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/clinicas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
