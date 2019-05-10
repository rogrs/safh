package br.com.rogrs.web.rest;

import br.com.rogrs.domain.Pacientes;
import br.com.rogrs.repository.PacientesRepository;
import br.com.rogrs.repository.search.PacientesSearchRepository;
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
 * REST controller for managing {@link br.com.rogrs.domain.Pacientes}.
 */
@RestController
@RequestMapping("/api")
public class PacientesResource {

    private final Logger log = LoggerFactory.getLogger(PacientesResource.class);

    private static final String ENTITY_NAME = "pacientes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PacientesRepository pacientesRepository;

    private final PacientesSearchRepository pacientesSearchRepository;

    public PacientesResource(PacientesRepository pacientesRepository, PacientesSearchRepository pacientesSearchRepository) {
        this.pacientesRepository = pacientesRepository;
        this.pacientesSearchRepository = pacientesSearchRepository;
    }

    /**
     * {@code POST  /pacientes} : Create a new pacientes.
     *
     * @param pacientes the pacientes to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pacientes, or with status {@code 400 (Bad Request)} if the pacientes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pacientes")
    public ResponseEntity<Pacientes> createPacientes(@Valid @RequestBody Pacientes pacientes) throws URISyntaxException {
        log.debug("REST request to save Pacientes : {}", pacientes);
        if (pacientes.getId() != null) {
            throw new BadRequestAlertException("A new pacientes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Pacientes result = pacientesRepository.save(pacientes);
        pacientesSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/pacientes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pacientes} : Updates an existing pacientes.
     *
     * @param pacientes the pacientes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pacientes,
     * or with status {@code 400 (Bad Request)} if the pacientes is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pacientes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pacientes")
    public ResponseEntity<Pacientes> updatePacientes(@Valid @RequestBody Pacientes pacientes) throws URISyntaxException {
        log.debug("REST request to update Pacientes : {}", pacientes);
        if (pacientes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Pacientes result = pacientesRepository.save(pacientes);
        pacientesSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pacientes.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /pacientes} : get all the pacientes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pacientes in body.
     */
    @GetMapping("/pacientes")
    public ResponseEntity<List<Pacientes>> getAllPacientes(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of Pacientes");
        Page<Pacientes> page = pacientesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pacientes/:id} : get the "id" pacientes.
     *
     * @param id the id of the pacientes to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pacientes, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pacientes/{id}")
    public ResponseEntity<Pacientes> getPacientes(@PathVariable Long id) {
        log.debug("REST request to get Pacientes : {}", id);
        Optional<Pacientes> pacientes = pacientesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(pacientes);
    }

    /**
     * {@code DELETE  /pacientes/:id} : delete the "id" pacientes.
     *
     * @param id the id of the pacientes to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pacientes/{id}")
    public ResponseEntity<Void> deletePacientes(@PathVariable Long id) {
        log.debug("REST request to delete Pacientes : {}", id);
        pacientesRepository.deleteById(id);
        pacientesSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/pacientes?query=:query} : search for the pacientes corresponding
     * to the query.
     *
     * @param query the query of the pacientes search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/pacientes")
    public ResponseEntity<List<Pacientes>> searchPacientes(@RequestParam String query, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to search for a page of Pacientes for query {}", query);
        Page<Pacientes> page = pacientesSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
