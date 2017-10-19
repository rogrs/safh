package br.com.rogrs.safh.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.rogrs.safh.domain.Especialidades;

import br.com.rogrs.safh.repository.EspecialidadesRepository;
import br.com.rogrs.safh.repository.search.EspecialidadesSearchRepository;
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
 * REST controller for managing Especialidades.
 */
@RestController
@RequestMapping("/api")
public class EspecialidadesResource {

    private final Logger log = LoggerFactory.getLogger(EspecialidadesResource.class);

    private static final String ENTITY_NAME = "especialidades";

    private final EspecialidadesRepository especialidadesRepository;

    private final EspecialidadesSearchRepository especialidadesSearchRepository;

    public EspecialidadesResource(EspecialidadesRepository especialidadesRepository, EspecialidadesSearchRepository especialidadesSearchRepository) {
        this.especialidadesRepository = especialidadesRepository;
        this.especialidadesSearchRepository = especialidadesSearchRepository;
    }

    /**
     * POST  /especialidades : Create a new especialidades.
     *
     * @param especialidades the especialidades to create
     * @return the ResponseEntity with status 201 (Created) and with body the new especialidades, or with status 400 (Bad Request) if the especialidades has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/especialidades")
    @Timed
    public ResponseEntity<Especialidades> createEspecialidades(@Valid @RequestBody Especialidades especialidades) throws URISyntaxException {
        log.debug("REST request to save Especialidades : {}", especialidades);
        if (especialidades.getId() != null) {
            throw new BadRequestAlertException("A new especialidades cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Especialidades result = especialidadesRepository.save(especialidades);
        especialidadesSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/especialidades/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /especialidades : Updates an existing especialidades.
     *
     * @param especialidades the especialidades to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated especialidades,
     * or with status 400 (Bad Request) if the especialidades is not valid,
     * or with status 500 (Internal Server Error) if the especialidades couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/especialidades")
    @Timed
    public ResponseEntity<Especialidades> updateEspecialidades(@Valid @RequestBody Especialidades especialidades) throws URISyntaxException {
        log.debug("REST request to update Especialidades : {}", especialidades);
        if (especialidades.getId() == null) {
            return createEspecialidades(especialidades);
        }
        Especialidades result = especialidadesRepository.save(especialidades);
        especialidadesSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, especialidades.getId().toString()))
            .body(result);
    }

    /**
     * GET  /especialidades : get all the especialidades.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of especialidades in body
     */
    @GetMapping("/especialidades")
    @Timed
    public ResponseEntity<List<Especialidades>> getAllEspecialidades(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Especialidades");
        Page<Especialidades> page = especialidadesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/especialidades");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /especialidades/:id : get the "id" especialidades.
     *
     * @param id the id of the especialidades to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the especialidades, or with status 404 (Not Found)
     */
    @GetMapping("/especialidades/{id}")
    @Timed
    public ResponseEntity<Especialidades> getEspecialidades(@PathVariable Long id) {
        log.debug("REST request to get Especialidades : {}", id);
        Especialidades especialidades = especialidadesRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(especialidades));
    }

    /**
     * DELETE  /especialidades/:id : delete the "id" especialidades.
     *
     * @param id the id of the especialidades to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/especialidades/{id}")
    @Timed
    public ResponseEntity<Void> deleteEspecialidades(@PathVariable Long id) {
        log.debug("REST request to delete Especialidades : {}", id);
        especialidadesRepository.delete(id);
        especialidadesSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/especialidades?query=:query : search for the especialidades corresponding
     * to the query.
     *
     * @param query the query of the especialidades search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/especialidades")
    @Timed
    public ResponseEntity<List<Especialidades>> searchEspecialidades(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Especialidades for query {}", query);
        Page<Especialidades> page = especialidadesSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/especialidades");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
