package br.com.rogrs.safh.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.rogrs.safh.domain.Pacientes;

import br.com.rogrs.safh.repository.PacientesRepository;
import br.com.rogrs.safh.repository.search.PacientesSearchRepository;
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
 * REST controller for managing Pacientes.
 */
@RestController
@RequestMapping("/api")
public class PacientesResource {

    private final Logger log = LoggerFactory.getLogger(PacientesResource.class);

    private static final String ENTITY_NAME = "pacientes";

    private final PacientesRepository pacientesRepository;

    private final PacientesSearchRepository pacientesSearchRepository;

    public PacientesResource(PacientesRepository pacientesRepository, PacientesSearchRepository pacientesSearchRepository) {
        this.pacientesRepository = pacientesRepository;
        this.pacientesSearchRepository = pacientesSearchRepository;
    }

    /**
     * POST  /pacientes : Create a new pacientes.
     *
     * @param pacientes the pacientes to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pacientes, or with status 400 (Bad Request) if the pacientes has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pacientes")
    @Timed
    public ResponseEntity<Pacientes> createPacientes(@Valid @RequestBody Pacientes pacientes) throws URISyntaxException {
        log.debug("REST request to save Pacientes : {}", pacientes);
        if (pacientes.getId() != null) {
            throw new BadRequestAlertException("A new pacientes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Pacientes result = pacientesRepository.save(pacientes);
        pacientesSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/pacientes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pacientes : Updates an existing pacientes.
     *
     * @param pacientes the pacientes to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pacientes,
     * or with status 400 (Bad Request) if the pacientes is not valid,
     * or with status 500 (Internal Server Error) if the pacientes couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pacientes")
    @Timed
    public ResponseEntity<Pacientes> updatePacientes(@Valid @RequestBody Pacientes pacientes) throws URISyntaxException {
        log.debug("REST request to update Pacientes : {}", pacientes);
        if (pacientes.getId() == null) {
            return createPacientes(pacientes);
        }
        Pacientes result = pacientesRepository.save(pacientes);
        pacientesSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pacientes.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pacientes : get all the pacientes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pacientes in body
     */
    @GetMapping("/pacientes")
    @Timed
    public ResponseEntity<List<Pacientes>> getAllPacientes(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Pacientes");
        Page<Pacientes> page = pacientesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pacientes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /pacientes/:id : get the "id" pacientes.
     *
     * @param id the id of the pacientes to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pacientes, or with status 404 (Not Found)
     */
    @GetMapping("/pacientes/{id}")
    @Timed
    public ResponseEntity<Pacientes> getPacientes(@PathVariable Long id) {
        log.debug("REST request to get Pacientes : {}", id);
        Pacientes pacientes = pacientesRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pacientes));
    }

    /**
     * DELETE  /pacientes/:id : delete the "id" pacientes.
     *
     * @param id the id of the pacientes to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pacientes/{id}")
    @Timed
    public ResponseEntity<Void> deletePacientes(@PathVariable Long id) {
        log.debug("REST request to delete Pacientes : {}", id);
        pacientesRepository.delete(id);
        pacientesSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/pacientes?query=:query : search for the pacientes corresponding
     * to the query.
     *
     * @param query the query of the pacientes search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/pacientes")
    @Timed
    public ResponseEntity<List<Pacientes>> searchPacientes(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Pacientes for query {}", query);
        Page<Pacientes> page = pacientesSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/pacientes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
