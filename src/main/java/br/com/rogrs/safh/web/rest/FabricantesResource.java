package br.com.rogrs.safh.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.rogrs.safh.domain.Fabricantes;

import br.com.rogrs.safh.repository.FabricantesRepository;
import br.com.rogrs.safh.repository.search.FabricantesSearchRepository;
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
 * REST controller for managing Fabricantes.
 */
@RestController
@RequestMapping("/api")
public class FabricantesResource {

    private final Logger log = LoggerFactory.getLogger(FabricantesResource.class);

    private static final String ENTITY_NAME = "fabricantes";

    private final FabricantesRepository fabricantesRepository;

    private final FabricantesSearchRepository fabricantesSearchRepository;

    public FabricantesResource(FabricantesRepository fabricantesRepository, FabricantesSearchRepository fabricantesSearchRepository) {
        this.fabricantesRepository = fabricantesRepository;
        this.fabricantesSearchRepository = fabricantesSearchRepository;
    }

    /**
     * POST  /fabricantes : Create a new fabricantes.
     *
     * @param fabricantes the fabricantes to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fabricantes, or with status 400 (Bad Request) if the fabricantes has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/fabricantes")
    @Timed
    public ResponseEntity<Fabricantes> createFabricantes(@Valid @RequestBody Fabricantes fabricantes) throws URISyntaxException {
        log.debug("REST request to save Fabricantes : {}", fabricantes);
        if (fabricantes.getId() != null) {
            throw new BadRequestAlertException("A new fabricantes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Fabricantes result = fabricantesRepository.save(fabricantes);
        fabricantesSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/fabricantes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /fabricantes : Updates an existing fabricantes.
     *
     * @param fabricantes the fabricantes to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fabricantes,
     * or with status 400 (Bad Request) if the fabricantes is not valid,
     * or with status 500 (Internal Server Error) if the fabricantes couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/fabricantes")
    @Timed
    public ResponseEntity<Fabricantes> updateFabricantes(@Valid @RequestBody Fabricantes fabricantes) throws URISyntaxException {
        log.debug("REST request to update Fabricantes : {}", fabricantes);
        if (fabricantes.getId() == null) {
            return createFabricantes(fabricantes);
        }
        Fabricantes result = fabricantesRepository.save(fabricantes);
        fabricantesSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, fabricantes.getId().toString()))
            .body(result);
    }

    /**
     * GET  /fabricantes : get all the fabricantes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of fabricantes in body
     */
    @GetMapping("/fabricantes")
    @Timed
    public ResponseEntity<List<Fabricantes>> getAllFabricantes(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Fabricantes");
        Page<Fabricantes> page = fabricantesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/fabricantes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /fabricantes/:id : get the "id" fabricantes.
     *
     * @param id the id of the fabricantes to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fabricantes, or with status 404 (Not Found)
     */
    @GetMapping("/fabricantes/{id}")
    @Timed
    public ResponseEntity<Fabricantes> getFabricantes(@PathVariable Long id) {
        log.debug("REST request to get Fabricantes : {}", id);
        Fabricantes fabricantes = fabricantesRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(fabricantes));
    }

    /**
     * DELETE  /fabricantes/:id : delete the "id" fabricantes.
     *
     * @param id the id of the fabricantes to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/fabricantes/{id}")
    @Timed
    public ResponseEntity<Void> deleteFabricantes(@PathVariable Long id) {
        log.debug("REST request to delete Fabricantes : {}", id);
        fabricantesRepository.delete(id);
        fabricantesSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/fabricantes?query=:query : search for the fabricantes corresponding
     * to the query.
     *
     * @param query the query of the fabricantes search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/fabricantes")
    @Timed
    public ResponseEntity<List<Fabricantes>> searchFabricantes(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Fabricantes for query {}", query);
        Page<Fabricantes> page = fabricantesSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/fabricantes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
