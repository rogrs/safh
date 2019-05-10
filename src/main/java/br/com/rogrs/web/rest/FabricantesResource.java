package br.com.rogrs.web.rest;

import br.com.rogrs.domain.Fabricantes;
import br.com.rogrs.repository.FabricantesRepository;
import br.com.rogrs.repository.search.FabricantesSearchRepository;
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
 * REST controller for managing {@link br.com.rogrs.domain.Fabricantes}.
 */
@RestController
@RequestMapping("/api")
public class FabricantesResource {

    private final Logger log = LoggerFactory.getLogger(FabricantesResource.class);

    private static final String ENTITY_NAME = "fabricantes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FabricantesRepository fabricantesRepository;

    private final FabricantesSearchRepository fabricantesSearchRepository;

    public FabricantesResource(FabricantesRepository fabricantesRepository, FabricantesSearchRepository fabricantesSearchRepository) {
        this.fabricantesRepository = fabricantesRepository;
        this.fabricantesSearchRepository = fabricantesSearchRepository;
    }

    /**
     * {@code POST  /fabricantes} : Create a new fabricantes.
     *
     * @param fabricantes the fabricantes to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fabricantes, or with status {@code 400 (Bad Request)} if the fabricantes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fabricantes")
    public ResponseEntity<Fabricantes> createFabricantes(@Valid @RequestBody Fabricantes fabricantes) throws URISyntaxException {
        log.debug("REST request to save Fabricantes : {}", fabricantes);
        if (fabricantes.getId() != null) {
            throw new BadRequestAlertException("A new fabricantes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Fabricantes result = fabricantesRepository.save(fabricantes);
        fabricantesSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/fabricantes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fabricantes} : Updates an existing fabricantes.
     *
     * @param fabricantes the fabricantes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fabricantes,
     * or with status {@code 400 (Bad Request)} if the fabricantes is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fabricantes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fabricantes")
    public ResponseEntity<Fabricantes> updateFabricantes(@Valid @RequestBody Fabricantes fabricantes) throws URISyntaxException {
        log.debug("REST request to update Fabricantes : {}", fabricantes);
        if (fabricantes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Fabricantes result = fabricantesRepository.save(fabricantes);
        fabricantesSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, fabricantes.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /fabricantes} : get all the fabricantes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fabricantes in body.
     */
    @GetMapping("/fabricantes")
    public ResponseEntity<List<Fabricantes>> getAllFabricantes(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of Fabricantes");
        Page<Fabricantes> page = fabricantesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /fabricantes/:id} : get the "id" fabricantes.
     *
     * @param id the id of the fabricantes to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fabricantes, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fabricantes/{id}")
    public ResponseEntity<Fabricantes> getFabricantes(@PathVariable Long id) {
        log.debug("REST request to get Fabricantes : {}", id);
        Optional<Fabricantes> fabricantes = fabricantesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(fabricantes);
    }

    /**
     * {@code DELETE  /fabricantes/:id} : delete the "id" fabricantes.
     *
     * @param id the id of the fabricantes to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fabricantes/{id}")
    public ResponseEntity<Void> deleteFabricantes(@PathVariable Long id) {
        log.debug("REST request to delete Fabricantes : {}", id);
        fabricantesRepository.deleteById(id);
        fabricantesSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/fabricantes?query=:query} : search for the fabricantes corresponding
     * to the query.
     *
     * @param query the query of the fabricantes search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/fabricantes")
    public ResponseEntity<List<Fabricantes>> searchFabricantes(@RequestParam String query, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to search for a page of Fabricantes for query {}", query);
        Page<Fabricantes> page = fabricantesSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
