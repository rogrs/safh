package br.com.rogrs.web.rest;

import br.com.rogrs.domain.Enfermarias;
import br.com.rogrs.repository.EnfermariasRepository;
import br.com.rogrs.repository.search.EnfermariasSearchRepository;
import br.com.rogrs.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional; 
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
 * REST controller for managing {@link br.com.rogrs.domain.Enfermarias}.
 */
@RestController
@RequestMapping("/api")
public class EnfermariasResource {

    private final Logger log = LoggerFactory.getLogger(EnfermariasResource.class);

    private static final String ENTITY_NAME = "enfermarias";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EnfermariasRepository enfermariasRepository;

    private final EnfermariasSearchRepository enfermariasSearchRepository;

    public EnfermariasResource(EnfermariasRepository enfermariasRepository, EnfermariasSearchRepository enfermariasSearchRepository) {
        this.enfermariasRepository = enfermariasRepository;
        this.enfermariasSearchRepository = enfermariasSearchRepository;
    }

    /**
     * {@code POST  /enfermarias} : Create a new enfermarias.
     *
     * @param enfermarias the enfermarias to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new enfermarias, or with status {@code 400 (Bad Request)} if the enfermarias has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/enfermarias")
    public ResponseEntity<Enfermarias> createEnfermarias(@Valid @RequestBody Enfermarias enfermarias) throws URISyntaxException {
        log.debug("REST request to save Enfermarias : {}", enfermarias);
        if (enfermarias.getId() != null) {
            throw new BadRequestAlertException("A new enfermarias cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Enfermarias result = enfermariasRepository.save(enfermarias);
        enfermariasSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/enfermarias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /enfermarias} : Updates an existing enfermarias.
     *
     * @param enfermarias the enfermarias to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated enfermarias,
     * or with status {@code 400 (Bad Request)} if the enfermarias is not valid,
     * or with status {@code 500 (Internal Server Error)} if the enfermarias couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/enfermarias")
    public ResponseEntity<Enfermarias> updateEnfermarias(@Valid @RequestBody Enfermarias enfermarias) throws URISyntaxException {
        log.debug("REST request to update Enfermarias : {}", enfermarias);
        if (enfermarias.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Enfermarias result = enfermariasRepository.save(enfermarias);
        enfermariasSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, enfermarias.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /enfermarias} : get all the enfermarias.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of enfermarias in body.
     */
    @GetMapping("/enfermarias")
    public List<Enfermarias> getAllEnfermarias() {
        log.debug("REST request to get all Enfermarias");
        return enfermariasRepository.findAll();
    }

    /**
     * {@code GET  /enfermarias/:id} : get the "id" enfermarias.
     *
     * @param id the id of the enfermarias to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the enfermarias, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/enfermarias/{id}")
    public ResponseEntity<Enfermarias> getEnfermarias(@PathVariable String id) {
        log.debug("REST request to get Enfermarias : {}", id);
        Optional<Enfermarias> enfermarias = enfermariasRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(enfermarias);
    }

    /**
     * {@code DELETE  /enfermarias/:id} : delete the "id" enfermarias.
     *
     * @param id the id of the enfermarias to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/enfermarias/{id}")
    public ResponseEntity<Void> deleteEnfermarias(@PathVariable String id) {
        log.debug("REST request to delete Enfermarias : {}", id);
        enfermariasRepository.deleteById(id);
        enfermariasSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }

    /**
     * {@code SEARCH  /_search/enfermarias?query=:query} : search for the enfermarias corresponding
     * to the query.
     *
     * @param query the query of the enfermarias search.
     * @return the result of the search.
     */
    @GetMapping("/_search/enfermarias")
    public List<Enfermarias> searchEnfermarias(@RequestParam String query) {
        log.debug("REST request to search Enfermarias for query {}", query);
        return StreamSupport
            .stream(enfermariasSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
