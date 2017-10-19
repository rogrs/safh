package br.com.rogrs.safh.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.rogrs.safh.domain.InternacoesDetalhes;
import br.com.rogrs.safh.service.InternacoesDetalhesService;
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
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing InternacoesDetalhes.
 */
@RestController
@RequestMapping("/api")
public class InternacoesDetalhesResource {

    private final Logger log = LoggerFactory.getLogger(InternacoesDetalhesResource.class);

    private static final String ENTITY_NAME = "internacoesDetalhes";

    private final InternacoesDetalhesService internacoesDetalhesService;

    public InternacoesDetalhesResource(InternacoesDetalhesService internacoesDetalhesService) {
        this.internacoesDetalhesService = internacoesDetalhesService;
    }

    /**
     * POST  /internacoes-detalhes : Create a new internacoesDetalhes.
     *
     * @param internacoesDetalhes the internacoesDetalhes to create
     * @return the ResponseEntity with status 201 (Created) and with body the new internacoesDetalhes, or with status 400 (Bad Request) if the internacoesDetalhes has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/internacoes-detalhes")
    @Timed
    public ResponseEntity<InternacoesDetalhes> createInternacoesDetalhes(@Valid @RequestBody InternacoesDetalhes internacoesDetalhes) throws URISyntaxException {
        log.debug("REST request to save InternacoesDetalhes : {}", internacoesDetalhes);
        if (internacoesDetalhes.getId() != null) {
            throw new BadRequestAlertException("A new internacoesDetalhes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InternacoesDetalhes result = internacoesDetalhesService.save(internacoesDetalhes);
        return ResponseEntity.created(new URI("/api/internacoes-detalhes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /internacoes-detalhes : Updates an existing internacoesDetalhes.
     *
     * @param internacoesDetalhes the internacoesDetalhes to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated internacoesDetalhes,
     * or with status 400 (Bad Request) if the internacoesDetalhes is not valid,
     * or with status 500 (Internal Server Error) if the internacoesDetalhes couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/internacoes-detalhes")
    @Timed
    public ResponseEntity<InternacoesDetalhes> updateInternacoesDetalhes(@Valid @RequestBody InternacoesDetalhes internacoesDetalhes) throws URISyntaxException {
        log.debug("REST request to update InternacoesDetalhes : {}", internacoesDetalhes);
        if (internacoesDetalhes.getId() == null) {
            return createInternacoesDetalhes(internacoesDetalhes);
        }
        InternacoesDetalhes result = internacoesDetalhesService.save(internacoesDetalhes);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, internacoesDetalhes.getId().toString()))
            .body(result);
    }

    /**
     * GET  /internacoes-detalhes : get all the internacoesDetalhes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of internacoesDetalhes in body
     */
    @GetMapping("/internacoes-detalhes")
    @Timed
    public ResponseEntity<List<InternacoesDetalhes>> getAllInternacoesDetalhes(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of InternacoesDetalhes");
        Page<InternacoesDetalhes> page = internacoesDetalhesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/internacoes-detalhes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /internacoes-detalhes/:id : get the "id" internacoesDetalhes.
     *
     * @param id the id of the internacoesDetalhes to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the internacoesDetalhes, or with status 404 (Not Found)
     */
    @GetMapping("/internacoes-detalhes/{id}")
    @Timed
    public ResponseEntity<InternacoesDetalhes> getInternacoesDetalhes(@PathVariable Long id) {
        log.debug("REST request to get InternacoesDetalhes : {}", id);
        InternacoesDetalhes internacoesDetalhes = internacoesDetalhesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(internacoesDetalhes));
    }

    /**
     * DELETE  /internacoes-detalhes/:id : delete the "id" internacoesDetalhes.
     *
     * @param id the id of the internacoesDetalhes to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/internacoes-detalhes/{id}")
    @Timed
    public ResponseEntity<Void> deleteInternacoesDetalhes(@PathVariable Long id) {
        log.debug("REST request to delete InternacoesDetalhes : {}", id);
        internacoesDetalhesService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/internacoes-detalhes?query=:query : search for the internacoesDetalhes corresponding
     * to the query.
     *
     * @param query the query of the internacoesDetalhes search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/internacoes-detalhes")
    @Timed
    public ResponseEntity<List<InternacoesDetalhes>> searchInternacoesDetalhes(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of InternacoesDetalhes for query {}", query);
        Page<InternacoesDetalhes> page = internacoesDetalhesService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/internacoes-detalhes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
