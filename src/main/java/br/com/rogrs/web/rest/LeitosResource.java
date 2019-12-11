package br.com.rogrs.web.rest;

import br.com.rogrs.domain.Leitos;
import br.com.rogrs.repository.LeitosRepository;
import br.com.rogrs.repository.search.LeitosSearchRepository;
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
 * REST controller for managing {@link br.com.rogrs.domain.Leitos}.
 */
@RestController
@RequestMapping("/api")
public class LeitosResource {

    private final Logger log = LoggerFactory.getLogger(LeitosResource.class);

    private static final String ENTITY_NAME = "leitos";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LeitosRepository leitosRepository;

    private final LeitosSearchRepository leitosSearchRepository;

    public LeitosResource(LeitosRepository leitosRepository, LeitosSearchRepository leitosSearchRepository) {
        this.leitosRepository = leitosRepository;
        this.leitosSearchRepository = leitosSearchRepository;
    }

    /**
     * {@code POST  /leitos} : Create a new leitos.
     *
     * @param leitos the leitos to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new leitos, or with status {@code 400 (Bad Request)} if the leitos has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/leitos")
    public ResponseEntity<Leitos> createLeitos(@Valid @RequestBody Leitos leitos) throws URISyntaxException {
        log.debug("REST request to save Leitos : {}", leitos);
        if (leitos.getId() != null) {
            throw new BadRequestAlertException("A new leitos cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Leitos result = leitosRepository.save(leitos);
        leitosSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/leitos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /leitos} : Updates an existing leitos.
     *
     * @param leitos the leitos to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leitos,
     * or with status {@code 400 (Bad Request)} if the leitos is not valid,
     * or with status {@code 500 (Internal Server Error)} if the leitos couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/leitos")
    public ResponseEntity<Leitos> updateLeitos(@Valid @RequestBody Leitos leitos) throws URISyntaxException {
        log.debug("REST request to update Leitos : {}", leitos);
        if (leitos.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Leitos result = leitosRepository.save(leitos);
        leitosSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leitos.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /leitos} : get all the leitos.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of leitos in body.
     */
    @GetMapping("/leitos")
    public List<Leitos> getAllLeitos() {
        log.debug("REST request to get all Leitos");
        return leitosRepository.findAll();
    }

    /**
     * {@code GET  /leitos/:id} : get the "id" leitos.
     *
     * @param id the id of the leitos to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the leitos, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/leitos/{id}")
    public ResponseEntity<Leitos> getLeitos(@PathVariable String id) {
        log.debug("REST request to get Leitos : {}", id);
        Optional<Leitos> leitos = leitosRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(leitos);
    }

    /**
     * {@code DELETE  /leitos/:id} : delete the "id" leitos.
     *
     * @param id the id of the leitos to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/leitos/{id}")
    public ResponseEntity<Void> deleteLeitos(@PathVariable String id) {
        log.debug("REST request to delete Leitos : {}", id);
        leitosRepository.deleteById(id);
        leitosSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }

    /**
     * {@code SEARCH  /_search/leitos?query=:query} : search for the leitos corresponding
     * to the query.
     *
     * @param query the query of the leitos search.
     * @return the result of the search.
     */
    @GetMapping("/_search/leitos")
    public List<Leitos> searchLeitos(@RequestParam String query) {
        log.debug("REST request to search Leitos for query {}", query);
        return StreamSupport
            .stream(leitosSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
