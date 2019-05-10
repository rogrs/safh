package br.com.rogrs.web.rest;

import br.com.rogrs.domain.Prescricoes;
import br.com.rogrs.repository.PrescricoesRepository;
import br.com.rogrs.repository.search.PrescricoesSearchRepository;
import br.com.rogrs.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
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
 * REST controller for managing {@link br.com.rogrs.domain.Prescricoes}.
 */
@RestController
@RequestMapping("/api")
public class PrescricoesResource {

    private final Logger log = LoggerFactory.getLogger(PrescricoesResource.class);

    private static final String ENTITY_NAME = "prescricoes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PrescricoesRepository prescricoesRepository;

    private final PrescricoesSearchRepository prescricoesSearchRepository;

    public PrescricoesResource(PrescricoesRepository prescricoesRepository, PrescricoesSearchRepository prescricoesSearchRepository) {
        this.prescricoesRepository = prescricoesRepository;
        this.prescricoesSearchRepository = prescricoesSearchRepository;
    }

    /**
     * {@code POST  /prescricoes} : Create a new prescricoes.
     *
     * @param prescricoes the prescricoes to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new prescricoes, or with status {@code 400 (Bad Request)} if the prescricoes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/prescricoes")
    public ResponseEntity<Prescricoes> createPrescricoes(@Valid @RequestBody Prescricoes prescricoes) throws URISyntaxException {
        log.debug("REST request to save Prescricoes : {}", prescricoes);
        if (prescricoes.getId() != null) {
            throw new BadRequestAlertException("A new prescricoes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Prescricoes result = prescricoesRepository.save(prescricoes);
        prescricoesSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/prescricoes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /prescricoes} : Updates an existing prescricoes.
     *
     * @param prescricoes the prescricoes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prescricoes,
     * or with status {@code 400 (Bad Request)} if the prescricoes is not valid,
     * or with status {@code 500 (Internal Server Error)} if the prescricoes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/prescricoes")
    public ResponseEntity<Prescricoes> updatePrescricoes(@Valid @RequestBody Prescricoes prescricoes) throws URISyntaxException {
        log.debug("REST request to update Prescricoes : {}", prescricoes);
        if (prescricoes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Prescricoes result = prescricoesRepository.save(prescricoes);
        prescricoesSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, prescricoes.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /prescricoes} : get all the prescricoes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of prescricoes in body.
     */
    @GetMapping("/prescricoes")
    public ResponseEntity<List<Prescricoes>> getAllPrescricoes(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of Prescricoes");
        Page<Prescricoes> page = prescricoesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /prescricoes/:id} : get the "id" prescricoes.
     *
     * @param id the id of the prescricoes to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the prescricoes, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/prescricoes/{id}")
    public ResponseEntity<Prescricoes> getPrescricoes(@PathVariable Long id) {
        log.debug("REST request to get Prescricoes : {}", id);
        Optional<Prescricoes> prescricoes = prescricoesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(prescricoes);
    }

    /**
     * {@code DELETE  /prescricoes/:id} : delete the "id" prescricoes.
     *
     * @param id the id of the prescricoes to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/prescricoes/{id}")
    public ResponseEntity<Void> deletePrescricoes(@PathVariable Long id) {
        log.debug("REST request to delete Prescricoes : {}", id);
        prescricoesRepository.deleteById(id);
        prescricoesSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/prescricoes?query=:query} : search for the prescricoes corresponding
     * to the query.
     *
     * @param query the query of the prescricoes search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/prescricoes")
    public ResponseEntity<List<Prescricoes>> searchPrescricoes(@RequestParam String query, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to search for a page of Prescricoes for query {}", query);
        Page<Prescricoes> page = prescricoesSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
