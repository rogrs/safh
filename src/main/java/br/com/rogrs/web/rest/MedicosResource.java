package br.com.rogrs.web.rest;

import br.com.rogrs.domain.Medicos;
import br.com.rogrs.repository.MedicosRepository;
import br.com.rogrs.repository.search.MedicosSearchRepository;
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
 * REST controller for managing {@link br.com.rogrs.domain.Medicos}.
 */
@RestController
@RequestMapping("/api")
public class MedicosResource {

    private final Logger log = LoggerFactory.getLogger(MedicosResource.class);

    private static final String ENTITY_NAME = "medicos";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MedicosRepository medicosRepository;

    private final MedicosSearchRepository medicosSearchRepository;

    public MedicosResource(MedicosRepository medicosRepository, MedicosSearchRepository medicosSearchRepository) {
        this.medicosRepository = medicosRepository;
        this.medicosSearchRepository = medicosSearchRepository;
    }

    /**
     * {@code POST  /medicos} : Create a new medicos.
     *
     * @param medicos the medicos to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new medicos, or with status {@code 400 (Bad Request)} if the medicos has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/medicos")
    public ResponseEntity<Medicos> createMedicos(@Valid @RequestBody Medicos medicos) throws URISyntaxException {
        log.debug("REST request to save Medicos : {}", medicos);
        if (medicos.getId() != null) {
            throw new BadRequestAlertException("A new medicos cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Medicos result = medicosRepository.save(medicos);
        medicosSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/medicos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /medicos} : Updates an existing medicos.
     *
     * @param medicos the medicos to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medicos,
     * or with status {@code 400 (Bad Request)} if the medicos is not valid,
     * or with status {@code 500 (Internal Server Error)} if the medicos couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/medicos")
    public ResponseEntity<Medicos> updateMedicos(@Valid @RequestBody Medicos medicos) throws URISyntaxException {
        log.debug("REST request to update Medicos : {}", medicos);
        if (medicos.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Medicos result = medicosRepository.save(medicos);
        medicosSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, medicos.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /medicos} : get all the medicos.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of medicos in body.
     */
    @GetMapping("/medicos")
    public List<Medicos> getAllMedicos() {
        log.debug("REST request to get all Medicos");
        return medicosRepository.findAll();
    }

    /**
     * {@code GET  /medicos/:id} : get the "id" medicos.
     *
     * @param id the id of the medicos to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the medicos, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/medicos/{id}")
    public ResponseEntity<Medicos> getMedicos(@PathVariable String id) {
        log.debug("REST request to get Medicos : {}", id);
        Optional<Medicos> medicos = medicosRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(medicos);
    }

    /**
     * {@code DELETE  /medicos/:id} : delete the "id" medicos.
     *
     * @param id the id of the medicos to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/medicos/{id}")
    public ResponseEntity<Void> deleteMedicos(@PathVariable String id) {
        log.debug("REST request to delete Medicos : {}", id);
        medicosRepository.deleteById(id);
        medicosSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }

    /**
     * {@code SEARCH  /_search/medicos?query=:query} : search for the medicos corresponding
     * to the query.
     *
     * @param query the query of the medicos search.
     * @return the result of the search.
     */
    @GetMapping("/_search/medicos")
    public List<Medicos> searchMedicos(@RequestParam String query) {
        log.debug("REST request to search Medicos for query {}", query);
        return StreamSupport
            .stream(medicosSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
