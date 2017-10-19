package br.com.rogrs.safh.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.rogrs.safh.domain.Medicamentos;

import br.com.rogrs.safh.repository.MedicamentosRepository;
import br.com.rogrs.safh.repository.search.MedicamentosSearchRepository;
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
 * REST controller for managing Medicamentos.
 */
@RestController
@RequestMapping("/api")
public class MedicamentosResource {

    private final Logger log = LoggerFactory.getLogger(MedicamentosResource.class);

    private static final String ENTITY_NAME = "medicamentos";

    private final MedicamentosRepository medicamentosRepository;

    private final MedicamentosSearchRepository medicamentosSearchRepository;

    public MedicamentosResource(MedicamentosRepository medicamentosRepository, MedicamentosSearchRepository medicamentosSearchRepository) {
        this.medicamentosRepository = medicamentosRepository;
        this.medicamentosSearchRepository = medicamentosSearchRepository;
    }

    /**
     * POST  /medicamentos : Create a new medicamentos.
     *
     * @param medicamentos the medicamentos to create
     * @return the ResponseEntity with status 201 (Created) and with body the new medicamentos, or with status 400 (Bad Request) if the medicamentos has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/medicamentos")
    @Timed
    public ResponseEntity<Medicamentos> createMedicamentos(@Valid @RequestBody Medicamentos medicamentos) throws URISyntaxException {
        log.debug("REST request to save Medicamentos : {}", medicamentos);
        if (medicamentos.getId() != null) {
            throw new BadRequestAlertException("A new medicamentos cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Medicamentos result = medicamentosRepository.save(medicamentos);
        medicamentosSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/medicamentos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /medicamentos : Updates an existing medicamentos.
     *
     * @param medicamentos the medicamentos to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated medicamentos,
     * or with status 400 (Bad Request) if the medicamentos is not valid,
     * or with status 500 (Internal Server Error) if the medicamentos couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/medicamentos")
    @Timed
    public ResponseEntity<Medicamentos> updateMedicamentos(@Valid @RequestBody Medicamentos medicamentos) throws URISyntaxException {
        log.debug("REST request to update Medicamentos : {}", medicamentos);
        if (medicamentos.getId() == null) {
            return createMedicamentos(medicamentos);
        }
        Medicamentos result = medicamentosRepository.save(medicamentos);
        medicamentosSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, medicamentos.getId().toString()))
            .body(result);
    }

    /**
     * GET  /medicamentos : get all the medicamentos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of medicamentos in body
     */
    @GetMapping("/medicamentos")
    @Timed
    public ResponseEntity<List<Medicamentos>> getAllMedicamentos(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Medicamentos");
        Page<Medicamentos> page = medicamentosRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/medicamentos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /medicamentos/:id : get the "id" medicamentos.
     *
     * @param id the id of the medicamentos to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the medicamentos, or with status 404 (Not Found)
     */
    @GetMapping("/medicamentos/{id}")
    @Timed
    public ResponseEntity<Medicamentos> getMedicamentos(@PathVariable Long id) {
        log.debug("REST request to get Medicamentos : {}", id);
        Medicamentos medicamentos = medicamentosRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(medicamentos));
    }

    /**
     * DELETE  /medicamentos/:id : delete the "id" medicamentos.
     *
     * @param id the id of the medicamentos to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/medicamentos/{id}")
    @Timed
    public ResponseEntity<Void> deleteMedicamentos(@PathVariable Long id) {
        log.debug("REST request to delete Medicamentos : {}", id);
        medicamentosRepository.delete(id);
        medicamentosSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/medicamentos?query=:query : search for the medicamentos corresponding
     * to the query.
     *
     * @param query the query of the medicamentos search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/medicamentos")
    @Timed
    public ResponseEntity<List<Medicamentos>> searchMedicamentos(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Medicamentos for query {}", query);
        Page<Medicamentos> page = medicamentosSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/medicamentos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
