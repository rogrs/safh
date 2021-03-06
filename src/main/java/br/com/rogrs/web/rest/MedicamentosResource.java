package br.com.rogrs.web.rest;

import br.com.rogrs.domain.Medicamentos;
import br.com.rogrs.repository.MedicamentosRepository;
import br.com.rogrs.repository.search.MedicamentosSearchRepository;
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
 * REST controller for managing {@link br.com.rogrs.domain.Medicamentos}.
 */
@RestController
@RequestMapping("/api")
public class MedicamentosResource {

    private final Logger log = LoggerFactory.getLogger(MedicamentosResource.class);

    private static final String ENTITY_NAME = "medicamentos";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MedicamentosRepository medicamentosRepository;

    private final MedicamentosSearchRepository medicamentosSearchRepository;

    public MedicamentosResource(MedicamentosRepository medicamentosRepository, MedicamentosSearchRepository medicamentosSearchRepository) {
        this.medicamentosRepository = medicamentosRepository;
        this.medicamentosSearchRepository = medicamentosSearchRepository;
    }

    /**
     * {@code POST  /medicamentos} : Create a new medicamentos.
     *
     * @param medicamentos the medicamentos to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new medicamentos, or with status {@code 400 (Bad Request)} if the medicamentos has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/medicamentos")
    public ResponseEntity<Medicamentos> createMedicamentos(@Valid @RequestBody Medicamentos medicamentos) throws URISyntaxException {
        log.debug("REST request to save Medicamentos : {}", medicamentos);
        if (medicamentos.getId() != null) {
            throw new BadRequestAlertException("A new medicamentos cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Medicamentos result = medicamentosRepository.save(medicamentos);
        medicamentosSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/medicamentos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /medicamentos} : Updates an existing medicamentos.
     *
     * @param medicamentos the medicamentos to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medicamentos,
     * or with status {@code 400 (Bad Request)} if the medicamentos is not valid,
     * or with status {@code 500 (Internal Server Error)} if the medicamentos couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/medicamentos")
    public ResponseEntity<Medicamentos> updateMedicamentos(@Valid @RequestBody Medicamentos medicamentos) throws URISyntaxException {
        log.debug("REST request to update Medicamentos : {}", medicamentos);
        if (medicamentos.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Medicamentos result = medicamentosRepository.save(medicamentos);
        medicamentosSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, medicamentos.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /medicamentos} : get all the medicamentos.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of medicamentos in body.
     */
    @GetMapping("/medicamentos")
    public List<Medicamentos> getAllMedicamentos() {
        log.debug("REST request to get all Medicamentos");
        return medicamentosRepository.findAll();
    }

    /**
     * {@code GET  /medicamentos/:id} : get the "id" medicamentos.
     *
     * @param id the id of the medicamentos to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the medicamentos, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/medicamentos/{id}")
    public ResponseEntity<Medicamentos> getMedicamentos(@PathVariable String id) {
        log.debug("REST request to get Medicamentos : {}", id);
        Optional<Medicamentos> medicamentos = medicamentosRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(medicamentos);
    }

    /**
     * {@code DELETE  /medicamentos/:id} : delete the "id" medicamentos.
     *
     * @param id the id of the medicamentos to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/medicamentos/{id}")
    public ResponseEntity<Void> deleteMedicamentos(@PathVariable String id) {
        log.debug("REST request to delete Medicamentos : {}", id);
        medicamentosRepository.deleteById(id);
        medicamentosSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }

    /**
     * {@code SEARCH  /_search/medicamentos?query=:query} : search for the medicamentos corresponding
     * to the query.
     *
     * @param query the query of the medicamentos search.
     * @return the result of the search.
     */
    @GetMapping("/_search/medicamentos")
    public List<Medicamentos> searchMedicamentos(@RequestParam String query) {
        log.debug("REST request to search Medicamentos for query {}", query);
        return StreamSupport
            .stream(medicamentosSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
