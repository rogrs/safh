package br.com.rogrs.web.rest;

import br.com.rogrs.domain.Posologias;
import br.com.rogrs.repository.PosologiasRepository;
import br.com.rogrs.repository.search.PosologiasSearchRepository;
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
 * REST controller for managing {@link br.com.rogrs.domain.Posologias}.
 */
@RestController
@RequestMapping("/api")
public class PosologiasResource {

    private final Logger log = LoggerFactory.getLogger(PosologiasResource.class);

    private static final String ENTITY_NAME = "posologias";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PosologiasRepository posologiasRepository;

    private final PosologiasSearchRepository posologiasSearchRepository;

    public PosologiasResource(PosologiasRepository posologiasRepository, PosologiasSearchRepository posologiasSearchRepository) {
        this.posologiasRepository = posologiasRepository;
        this.posologiasSearchRepository = posologiasSearchRepository;
    }

    /**
     * {@code POST  /posologias} : Create a new posologias.
     *
     * @param posologias the posologias to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new posologias, or with status {@code 400 (Bad Request)} if the posologias has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/posologias")
    public ResponseEntity<Posologias> createPosologias(@Valid @RequestBody Posologias posologias) throws URISyntaxException {
        log.debug("REST request to save Posologias : {}", posologias);
        if (posologias.getId() != null) {
            throw new BadRequestAlertException("A new posologias cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Posologias result = posologiasRepository.save(posologias);
        posologiasSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/posologias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /posologias} : Updates an existing posologias.
     *
     * @param posologias the posologias to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated posologias,
     * or with status {@code 400 (Bad Request)} if the posologias is not valid,
     * or with status {@code 500 (Internal Server Error)} if the posologias couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/posologias")
    public ResponseEntity<Posologias> updatePosologias(@Valid @RequestBody Posologias posologias) throws URISyntaxException {
        log.debug("REST request to update Posologias : {}", posologias);
        if (posologias.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Posologias result = posologiasRepository.save(posologias);
        posologiasSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, posologias.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /posologias} : get all the posologias.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of posologias in body.
     */
    @GetMapping("/posologias")
    public List<Posologias> getAllPosologias() {
        log.debug("REST request to get all Posologias");
        return posologiasRepository.findAll();
    }

    /**
     * {@code GET  /posologias/:id} : get the "id" posologias.
     *
     * @param id the id of the posologias to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the posologias, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/posologias/{id}")
    public ResponseEntity<Posologias> getPosologias(@PathVariable String id) {
        log.debug("REST request to get Posologias : {}", id);
        Optional<Posologias> posologias = posologiasRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(posologias);
    }

    /**
     * {@code DELETE  /posologias/:id} : delete the "id" posologias.
     *
     * @param id the id of the posologias to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/posologias/{id}")
    public ResponseEntity<Void> deletePosologias(@PathVariable String id) {
        log.debug("REST request to delete Posologias : {}", id);
        posologiasRepository.deleteById(id);
        posologiasSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }

    /**
     * {@code SEARCH  /_search/posologias?query=:query} : search for the posologias corresponding
     * to the query.
     *
     * @param query the query of the posologias search.
     * @return the result of the search.
     */
    @GetMapping("/_search/posologias")
    public List<Posologias> searchPosologias(@RequestParam String query) {
        log.debug("REST request to search Posologias for query {}", query);
        return StreamSupport
            .stream(posologiasSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
