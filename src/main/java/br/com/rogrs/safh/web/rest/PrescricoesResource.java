package br.com.rogrs.safh.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.rogrs.safh.domain.Prescricoes;

import br.com.rogrs.safh.repository.PrescricoesRepository;
import br.com.rogrs.safh.repository.search.PrescricoesSearchRepository;
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
 * REST controller for managing Prescricoes.
 */
@RestController
@RequestMapping("/api")
public class PrescricoesResource {

    private final Logger log = LoggerFactory.getLogger(PrescricoesResource.class);

    private static final String ENTITY_NAME = "prescricoes";

    private final PrescricoesRepository prescricoesRepository;

    private final PrescricoesSearchRepository prescricoesSearchRepository;

    public PrescricoesResource(PrescricoesRepository prescricoesRepository, PrescricoesSearchRepository prescricoesSearchRepository) {
        this.prescricoesRepository = prescricoesRepository;
        this.prescricoesSearchRepository = prescricoesSearchRepository;
    }

    /**
     * POST  /prescricoes : Create a new prescricoes.
     *
     * @param prescricoes the prescricoes to create
     * @return the ResponseEntity with status 201 (Created) and with body the new prescricoes, or with status 400 (Bad Request) if the prescricoes has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/prescricoes")
    @Timed
    public ResponseEntity<Prescricoes> createPrescricoes(@Valid @RequestBody Prescricoes prescricoes) throws URISyntaxException {
        log.debug("REST request to save Prescricoes : {}", prescricoes);
        if (prescricoes.getId() != null) {
            throw new BadRequestAlertException("A new prescricoes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Prescricoes result = prescricoesRepository.save(prescricoes);
        prescricoesSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/prescricoes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /prescricoes : Updates an existing prescricoes.
     *
     * @param prescricoes the prescricoes to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated prescricoes,
     * or with status 400 (Bad Request) if the prescricoes is not valid,
     * or with status 500 (Internal Server Error) if the prescricoes couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/prescricoes")
    @Timed
    public ResponseEntity<Prescricoes> updatePrescricoes(@Valid @RequestBody Prescricoes prescricoes) throws URISyntaxException {
        log.debug("REST request to update Prescricoes : {}", prescricoes);
        if (prescricoes.getId() == null) {
            return createPrescricoes(prescricoes);
        }
        Prescricoes result = prescricoesRepository.save(prescricoes);
        prescricoesSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, prescricoes.getId().toString()))
            .body(result);
    }

    /**
     * GET  /prescricoes : get all the prescricoes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of prescricoes in body
     */
    @GetMapping("/prescricoes")
    @Timed
    public ResponseEntity<List<Prescricoes>> getAllPrescricoes(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Prescricoes");
        Page<Prescricoes> page = prescricoesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/prescricoes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /prescricoes/:id : get the "id" prescricoes.
     *
     * @param id the id of the prescricoes to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the prescricoes, or with status 404 (Not Found)
     */
    @GetMapping("/prescricoes/{id}")
    @Timed
    public ResponseEntity<Prescricoes> getPrescricoes(@PathVariable Long id) {
        log.debug("REST request to get Prescricoes : {}", id);
        Prescricoes prescricoes = prescricoesRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(prescricoes));
    }

    /**
     * DELETE  /prescricoes/:id : delete the "id" prescricoes.
     *
     * @param id the id of the prescricoes to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/prescricoes/{id}")
    @Timed
    public ResponseEntity<Void> deletePrescricoes(@PathVariable Long id) {
        log.debug("REST request to delete Prescricoes : {}", id);
        prescricoesRepository.delete(id);
        prescricoesSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/prescricoes?query=:query : search for the prescricoes corresponding
     * to the query.
     *
     * @param query the query of the prescricoes search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/prescricoes")
    @Timed
    public ResponseEntity<List<Prescricoes>> searchPrescricoes(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Prescricoes for query {}", query);
        Page<Prescricoes> page = prescricoesSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/prescricoes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
