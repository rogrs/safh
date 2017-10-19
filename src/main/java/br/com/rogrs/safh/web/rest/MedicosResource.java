package br.com.rogrs.safh.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.rogrs.safh.domain.Medicos;

import br.com.rogrs.safh.repository.MedicosRepository;
import br.com.rogrs.safh.repository.search.MedicosSearchRepository;
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
 * REST controller for managing Medicos.
 */
@RestController
@RequestMapping("/api")
public class MedicosResource {

    private final Logger log = LoggerFactory.getLogger(MedicosResource.class);

    private static final String ENTITY_NAME = "medicos";

    private final MedicosRepository medicosRepository;

    private final MedicosSearchRepository medicosSearchRepository;

    public MedicosResource(MedicosRepository medicosRepository, MedicosSearchRepository medicosSearchRepository) {
        this.medicosRepository = medicosRepository;
        this.medicosSearchRepository = medicosSearchRepository;
    }

    /**
     * POST  /medicos : Create a new medicos.
     *
     * @param medicos the medicos to create
     * @return the ResponseEntity with status 201 (Created) and with body the new medicos, or with status 400 (Bad Request) if the medicos has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/medicos")
    @Timed
    public ResponseEntity<Medicos> createMedicos(@Valid @RequestBody Medicos medicos) throws URISyntaxException {
        log.debug("REST request to save Medicos : {}", medicos);
        if (medicos.getId() != null) {
            throw new BadRequestAlertException("A new medicos cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Medicos result = medicosRepository.save(medicos);
        medicosSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/medicos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /medicos : Updates an existing medicos.
     *
     * @param medicos the medicos to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated medicos,
     * or with status 400 (Bad Request) if the medicos is not valid,
     * or with status 500 (Internal Server Error) if the medicos couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/medicos")
    @Timed
    public ResponseEntity<Medicos> updateMedicos(@Valid @RequestBody Medicos medicos) throws URISyntaxException {
        log.debug("REST request to update Medicos : {}", medicos);
        if (medicos.getId() == null) {
            return createMedicos(medicos);
        }
        Medicos result = medicosRepository.save(medicos);
        medicosSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, medicos.getId().toString()))
            .body(result);
    }

    /**
     * GET  /medicos : get all the medicos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of medicos in body
     */
    @GetMapping("/medicos")
    @Timed
    public ResponseEntity<List<Medicos>> getAllMedicos(@ApiParam Pageable pageable) {
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
    @GetMapping("/medicos/{id}")
    @Timed
    public ResponseEntity<Medicos> getMedicos(@PathVariable Long id) {
        log.debug("REST request to get Medicos : {}", id);
        Medicos medicos = medicosRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(medicos));
    }

    /**
     * DELETE  /medicos/:id : delete the "id" medicos.
     *
     * @param id the id of the medicos to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/medicos/{id}")
    @Timed
    public ResponseEntity<Void> deleteMedicos(@PathVariable Long id) {
        log.debug("REST request to delete Medicos : {}", id);
        medicosRepository.delete(id);
        medicosSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/medicos?query=:query : search for the medicos corresponding
     * to the query.
     *
     * @param query the query of the medicos search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/medicos")
    @Timed
    public ResponseEntity<List<Medicos>> searchMedicos(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Medicos for query {}", query);
        Page<Medicos> page = medicosSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/medicos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
