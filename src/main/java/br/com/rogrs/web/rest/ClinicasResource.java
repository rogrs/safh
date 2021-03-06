package br.com.rogrs.web.rest;

import br.com.rogrs.domain.Clinicas;
import br.com.rogrs.repository.ClinicasRepository;
import br.com.rogrs.repository.search.ClinicasSearchRepository;
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
 * REST controller for managing {@link br.com.rogrs.domain.Clinicas}.
 */
@RestController
@RequestMapping("/api")
public class ClinicasResource {

    private final Logger log = LoggerFactory.getLogger(ClinicasResource.class);

    private static final String ENTITY_NAME = "clinicas";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClinicasRepository clinicasRepository;

    private final ClinicasSearchRepository clinicasSearchRepository;

    public ClinicasResource(ClinicasRepository clinicasRepository, ClinicasSearchRepository clinicasSearchRepository) {
        this.clinicasRepository = clinicasRepository;
        this.clinicasSearchRepository = clinicasSearchRepository;
    }

    /**
     * {@code POST  /clinicas} : Create a new clinicas.
     *
     * @param clinicas the clinicas to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new clinicas, or with status {@code 400 (Bad Request)} if the clinicas has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/clinicas")
    public ResponseEntity<Clinicas> createClinicas(@Valid @RequestBody Clinicas clinicas) throws URISyntaxException {
        log.debug("REST request to save Clinicas : {}", clinicas);
        if (clinicas.getId() != null) {
            throw new BadRequestAlertException("A new clinicas cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Clinicas result = clinicasRepository.save(clinicas);
        clinicasSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/clinicas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /clinicas} : Updates an existing clinicas.
     *
     * @param clinicas the clinicas to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clinicas,
     * or with status {@code 400 (Bad Request)} if the clinicas is not valid,
     * or with status {@code 500 (Internal Server Error)} if the clinicas couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/clinicas")
    public ResponseEntity<Clinicas> updateClinicas(@Valid @RequestBody Clinicas clinicas) throws URISyntaxException {
        log.debug("REST request to update Clinicas : {}", clinicas);
        if (clinicas.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Clinicas result = clinicasRepository.save(clinicas);
        clinicasSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, clinicas.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /clinicas} : get all the clinicas.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of clinicas in body.
     */
    @GetMapping("/clinicas")
    public List<Clinicas> getAllClinicas() {
        log.debug("REST request to get all Clinicas");
        return clinicasRepository.findAll();
    }

    /**
     * {@code GET  /clinicas/:id} : get the "id" clinicas.
     *
     * @param id the id of the clinicas to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the clinicas, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/clinicas/{id}")
    public ResponseEntity<Clinicas> getClinicas(@PathVariable String id) {
        log.debug("REST request to get Clinicas : {}", id);
        Optional<Clinicas> clinicas = clinicasRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(clinicas);
    }

    /**
     * {@code DELETE  /clinicas/:id} : delete the "id" clinicas.
     *
     * @param id the id of the clinicas to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/clinicas/{id}")
    public ResponseEntity<Void> deleteClinicas(@PathVariable String id) {
        log.debug("REST request to delete Clinicas : {}", id);
        clinicasRepository.deleteById(id);
        clinicasSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }

    /**
     * {@code SEARCH  /_search/clinicas?query=:query} : search for the clinicas corresponding
     * to the query.
     *
     * @param query the query of the clinicas search.
     * @return the result of the search.
     */
    @GetMapping("/_search/clinicas")
    public List<Clinicas> searchClinicas(@RequestParam String query) {
        log.debug("REST request to search Clinicas for query {}", query);
        return StreamSupport
            .stream(clinicasSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
