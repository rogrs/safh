package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Especialidades;
import com.mycompany.myapp.repository.EspecialidadesRepository;
import com.mycompany.myapp.repository.search.EspecialidadesSearchRepository;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
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
        
    @Inject
    private EspecialidadesRepository especialidadesRepository;
    
    @Inject
    private EspecialidadesSearchRepository especialidadesSearchRepository;
    
    /**
     * POST  /especialidades : Create a new especialidades.
     *
     * @param especialidades the especialidades to create
     * @return the ResponseEntity with status 201 (Created) and with body the new especialidades, or with status 400 (Bad Request) if the especialidades has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/especialidades",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Especialidades> createEspecialidades(@Valid @RequestBody Especialidades especialidades) throws URISyntaxException {
        log.debug("REST request to save Especialidades : {}", especialidades);
        if (especialidades.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("especialidades", "idexists", "A new especialidades cannot already have an ID")).body(null);
        }
        Especialidades result = especialidadesRepository.save(especialidades);
        especialidadesSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/especialidades/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("especialidades", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /especialidades : Updates an existing especialidades.
     *
     * @param especialidades the especialidades to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated especialidades,
     * or with status 400 (Bad Request) if the especialidades is not valid,
     * or with status 500 (Internal Server Error) if the especialidades couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/especialidades",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Especialidades> updateEspecialidades(@Valid @RequestBody Especialidades especialidades) throws URISyntaxException {
        log.debug("REST request to update Especialidades : {}", especialidades);
        if (especialidades.getId() == null) {
            return createEspecialidades(especialidades);
        }
        Especialidades result = especialidadesRepository.save(especialidades);
        especialidadesSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("especialidades", especialidades.getId().toString()))
            .body(result);
    }

    /**
     * GET  /especialidades : get all the especialidades.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of especialidades in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/especialidades",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Especialidades>> getAllEspecialidades(Pageable pageable)
        throws URISyntaxException {
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
    @RequestMapping(value = "/especialidades/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Especialidades> getEspecialidades(@PathVariable Long id) {
        log.debug("REST request to get Especialidades : {}", id);
        Especialidades especialidades = especialidadesRepository.findOne(id);
        return Optional.ofNullable(especialidades)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /especialidades/:id : delete the "id" especialidades.
     *
     * @param id the id of the especialidades to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/especialidades/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEspecialidades(@PathVariable Long id) {
        log.debug("REST request to delete Especialidades : {}", id);
        especialidadesRepository.delete(id);
        especialidadesSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("especialidades", id.toString())).build();
    }

    /**
     * SEARCH  /_search/especialidades?query=:query : search for the especialidades corresponding
     * to the query.
     *
     * @param query the query of the especialidades search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/especialidades",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Especialidades>> searchEspecialidades(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Especialidades for query {}", query);
        Page<Especialidades> page = especialidadesSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/especialidades");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
