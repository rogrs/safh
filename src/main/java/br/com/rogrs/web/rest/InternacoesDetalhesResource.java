package br.com.rogrs.web.rest;

import br.com.rogrs.domain.InternacoesDetalhes;
import br.com.rogrs.service.InternacoesDetalhesService;
import br.com.rogrs.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
 * REST controller for managing {@link br.com.rogrs.domain.InternacoesDetalhes}.
 */
@RestController
@RequestMapping("/api")
public class InternacoesDetalhesResource {

    private final Logger log = LoggerFactory.getLogger(InternacoesDetalhesResource.class);

    private static final String ENTITY_NAME = "internacoesDetalhes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InternacoesDetalhesService internacoesDetalhesService;

    public InternacoesDetalhesResource(InternacoesDetalhesService internacoesDetalhesService) {
        this.internacoesDetalhesService = internacoesDetalhesService;
    }

    /**
     * {@code POST  /internacoes-detalhes} : Create a new internacoesDetalhes.
     *
     * @param internacoesDetalhes the internacoesDetalhes to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new internacoesDetalhes, or with status {@code 400 (Bad Request)} if the internacoesDetalhes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/internacoes-detalhes")
    public ResponseEntity<InternacoesDetalhes> createInternacoesDetalhes(@Valid @RequestBody InternacoesDetalhes internacoesDetalhes) throws URISyntaxException {
        log.debug("REST request to save InternacoesDetalhes : {}", internacoesDetalhes);
        if (internacoesDetalhes.getId() != null) {
            throw new BadRequestAlertException("A new internacoesDetalhes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InternacoesDetalhes result = internacoesDetalhesService.save(internacoesDetalhes);
        return ResponseEntity.created(new URI("/api/internacoes-detalhes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /internacoes-detalhes} : Updates an existing internacoesDetalhes.
     *
     * @param internacoesDetalhes the internacoesDetalhes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated internacoesDetalhes,
     * or with status {@code 400 (Bad Request)} if the internacoesDetalhes is not valid,
     * or with status {@code 500 (Internal Server Error)} if the internacoesDetalhes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/internacoes-detalhes")
    public ResponseEntity<InternacoesDetalhes> updateInternacoesDetalhes(@Valid @RequestBody InternacoesDetalhes internacoesDetalhes) throws URISyntaxException {
        log.debug("REST request to update InternacoesDetalhes : {}", internacoesDetalhes);
        if (internacoesDetalhes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        InternacoesDetalhes result = internacoesDetalhesService.save(internacoesDetalhes);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, internacoesDetalhes.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /internacoes-detalhes} : get all the internacoesDetalhes.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of internacoesDetalhes in body.
     */
    @GetMapping("/internacoes-detalhes")
    public List<InternacoesDetalhes> getAllInternacoesDetalhes() {
        log.debug("REST request to get all InternacoesDetalhes");
        return internacoesDetalhesService.findAll();
    }

    /**
     * {@code GET  /internacoes-detalhes/:id} : get the "id" internacoesDetalhes.
     *
     * @param id the id of the internacoesDetalhes to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the internacoesDetalhes, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/internacoes-detalhes/{id}")
    public ResponseEntity<InternacoesDetalhes> getInternacoesDetalhes(@PathVariable String id) {
        log.debug("REST request to get InternacoesDetalhes : {}", id);
        Optional<InternacoesDetalhes> internacoesDetalhes = internacoesDetalhesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(internacoesDetalhes);
    }

    /**
     * {@code DELETE  /internacoes-detalhes/:id} : delete the "id" internacoesDetalhes.
     *
     * @param id the id of the internacoesDetalhes to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/internacoes-detalhes/{id}")
    public ResponseEntity<Void> deleteInternacoesDetalhes(@PathVariable String id) {
        log.debug("REST request to delete InternacoesDetalhes : {}", id);
        internacoesDetalhesService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }

    /**
     * {@code SEARCH  /_search/internacoes-detalhes?query=:query} : search for the internacoesDetalhes corresponding
     * to the query.
     *
     * @param query the query of the internacoesDetalhes search.
     * @return the result of the search.
     */
    @GetMapping("/_search/internacoes-detalhes")
    public List<InternacoesDetalhes> searchInternacoesDetalhes(@RequestParam String query) {
        log.debug("REST request to search InternacoesDetalhes for query {}", query);
        return internacoesDetalhesService.search(query);
    }
}
