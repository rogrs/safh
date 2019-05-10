package br.com.rogrs.web.rest;

import br.com.rogrs.domain.Dietas;
import br.com.rogrs.repository.DietasRepository;
import br.com.rogrs.repository.search.DietasSearchRepository;
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
 * REST controller for managing {@link br.com.rogrs.domain.Dietas}.
 */
@RestController
@RequestMapping("/api")
public class DietasResource {

    private final Logger log = LoggerFactory.getLogger(DietasResource.class);

    private static final String ENTITY_NAME = "dietas";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DietasRepository dietasRepository;

    private final DietasSearchRepository dietasSearchRepository;

    public DietasResource(DietasRepository dietasRepository, DietasSearchRepository dietasSearchRepository) {
        this.dietasRepository = dietasRepository;
        this.dietasSearchRepository = dietasSearchRepository;
    }

    /**
     * {@code POST  /dietas} : Create a new dietas.
     *
     * @param dietas the dietas to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dietas, or with status {@code 400 (Bad Request)} if the dietas has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dietas")
    public ResponseEntity<Dietas> createDietas(@Valid @RequestBody Dietas dietas) throws URISyntaxException {
        log.debug("REST request to save Dietas : {}", dietas);
        if (dietas.getId() != null) {
            throw new BadRequestAlertException("A new dietas cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Dietas result = dietasRepository.save(dietas);
        dietasSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/dietas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /dietas} : Updates an existing dietas.
     *
     * @param dietas the dietas to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dietas,
     * or with status {@code 400 (Bad Request)} if the dietas is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dietas couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dietas")
    public ResponseEntity<Dietas> updateDietas(@Valid @RequestBody Dietas dietas) throws URISyntaxException {
        log.debug("REST request to update Dietas : {}", dietas);
        if (dietas.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Dietas result = dietasRepository.save(dietas);
        dietasSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dietas.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /dietas} : get all the dietas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dietas in body.
     */
    @GetMapping("/dietas")
    public ResponseEntity<List<Dietas>> getAllDietas(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of Dietas");
        Page<Dietas> page = dietasRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /dietas/:id} : get the "id" dietas.
     *
     * @param id the id of the dietas to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dietas, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dietas/{id}")
    public ResponseEntity<Dietas> getDietas(@PathVariable Long id) {
        log.debug("REST request to get Dietas : {}", id);
        Optional<Dietas> dietas = dietasRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(dietas);
    }

    /**
     * {@code DELETE  /dietas/:id} : delete the "id" dietas.
     *
     * @param id the id of the dietas to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dietas/{id}")
    public ResponseEntity<Void> deleteDietas(@PathVariable Long id) {
        log.debug("REST request to delete Dietas : {}", id);
        dietasRepository.deleteById(id);
        dietasSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/dietas?query=:query} : search for the dietas corresponding
     * to the query.
     *
     * @param query the query of the dietas search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/dietas")
    public ResponseEntity<List<Dietas>> searchDietas(@RequestParam String query, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to search for a page of Dietas for query {}", query);
        Page<Dietas> page = dietasSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}