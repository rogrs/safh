package br.com.rogrs.web.rest;

import br.com.rogrs.domain.Internacoes;
import br.com.rogrs.repository.InternacoesRepository;
import br.com.rogrs.repository.search.InternacoesSearchRepository;
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
 * REST controller for managing {@link br.com.rogrs.domain.Internacoes}.
 */
@RestController
@RequestMapping("/api")
public class InternacoesResource {

    private final Logger log = LoggerFactory.getLogger(InternacoesResource.class);

    private static final String ENTITY_NAME = "internacoes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InternacoesRepository internacoesRepository;

    private final InternacoesSearchRepository internacoesSearchRepository;

    public InternacoesResource(InternacoesRepository internacoesRepository, InternacoesSearchRepository internacoesSearchRepository) {
        this.internacoesRepository = internacoesRepository;
        this.internacoesSearchRepository = internacoesSearchRepository;
    }

    /**
     * {@code POST  /internacoes} : Create a new internacoes.
     *
     * @param internacoes the internacoes to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new internacoes, or with status {@code 400 (Bad Request)} if the internacoes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/internacoes")
    public ResponseEntity<Internacoes> createInternacoes(@Valid @RequestBody Internacoes internacoes) throws URISyntaxException {
        log.debug("REST request to save Internacoes : {}", internacoes);
        if (internacoes.getId() != null) {
            throw new BadRequestAlertException("A new internacoes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Internacoes result = internacoesRepository.save(internacoes);
        internacoesSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/internacoes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /internacoes} : Updates an existing internacoes.
     *
     * @param internacoes the internacoes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated internacoes,
     * or with status {@code 400 (Bad Request)} if the internacoes is not valid,
     * or with status {@code 500 (Internal Server Error)} if the internacoes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/internacoes")
    public ResponseEntity<Internacoes> updateInternacoes(@Valid @RequestBody Internacoes internacoes) throws URISyntaxException {
        log.debug("REST request to update Internacoes : {}", internacoes);
        if (internacoes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Internacoes result = internacoesRepository.save(internacoes);
        internacoesSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, internacoes.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /internacoes} : get all the internacoes.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of internacoes in body.
     */
    @GetMapping("/internacoes")
    public List<Internacoes> getAllInternacoes() {
        log.debug("REST request to get all Internacoes");
        return internacoesRepository.findAll();
    }

    /**
     * {@code GET  /internacoes/:id} : get the "id" internacoes.
     *
     * @param id the id of the internacoes to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the internacoes, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/internacoes/{id}")
    public ResponseEntity<Internacoes> getInternacoes(@PathVariable String id) {
        log.debug("REST request to get Internacoes : {}", id);
        Optional<Internacoes> internacoes = internacoesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(internacoes);
    }

    /**
     * {@code DELETE  /internacoes/:id} : delete the "id" internacoes.
     *
     * @param id the id of the internacoes to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/internacoes/{id}")
    public ResponseEntity<Void> deleteInternacoes(@PathVariable String id) {
        log.debug("REST request to delete Internacoes : {}", id);
        internacoesRepository.deleteById(id);
        internacoesSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }

    /**
     * {@code SEARCH  /_search/internacoes?query=:query} : search for the internacoes corresponding
     * to the query.
     *
     * @param query the query of the internacoes search.
     * @return the result of the search.
     */
    @GetMapping("/_search/internacoes")
    public List<Internacoes> searchInternacoes(@RequestParam String query) {
        log.debug("REST request to search Internacoes for query {}", query);
        return StreamSupport
            .stream(internacoesSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
